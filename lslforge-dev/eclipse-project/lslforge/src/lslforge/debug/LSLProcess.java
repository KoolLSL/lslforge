/**
 *
 */
package lslforge.debug;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.core.model.IStreamsProxy;

import lslforge.LSLForgePlugin;
import lslforge.launching.Messages;
import lslforge.util.Log;

public abstract class LSLProcess extends Thread implements IProcess {
	protected Reader reader1;
	protected Reader reader2;
	protected ILaunch launch;
	protected Thread processMonitor;
	protected IStreamsProxy proxy = null;
	protected Process p = null;
    private boolean terminated = false;
    protected Interactor interactor;
    private HashSet<IProcessListener> listeners = new HashSet<>();
    protected LSLThread thread;
    protected LSLProcess() { }

    protected LSLProcess(ILaunch launch) {
        this.launch = launch;
    }

    protected Thread createProcessMonitor() {
        return new Thread() {
            @Override
			public void run() {

                try {
                    LSLProcess.this.p.waitFor();
                    onTerminate();
                } catch (InterruptedException e) {
                    LSLProcess.this.p.destroy();
                    onTerminate();
                } catch (Exception e) {
                    Log.error(e);
                    onTerminate();
                }
            }
	    };
    }

	public void go() {
	    p = launchExecutable();
        this.reader1 = new StringReader(""); //$NON-NLS-1$
        this.reader2 = new InputStreamReader(p.getErrorStream());
	    processMonitor = createProcessMonitor();
        interactor = createInteractor(p);
        interactor.addListener(thread);
        thread.setInteractor(interactor);
	    interactor.start();
	    processMonitor.start();
	}

    abstract protected Interactor createInteractor(Process p);
	abstract protected Process launchExecutable();

    @Override
	public String getAttribute(String key) {
		return null;
	}

	@Override
	public int getExitValue() throws DebugException {
		try {
            return p.exitValue();
        } catch (IllegalThreadStateException e) {
            throw new DebugException(new Status(IStatus.ERROR, LSLForgePlugin.PLUGIN_ID, Messages.getString("TestLaunchDelegate.NOT_TERMINATED"))); //$NON-NLS-1$
        }
	}

	@Override
	public ILaunch getLaunch() {
		return launch;
	}

	@Override
	public IStreamsProxy getStreamsProxy() {
		if (proxy == null) {

			final StreamMonitor errorStreamMonitor = new StreamMonitor(reader2);
			final StreamMonitor outputStreamMonitor = new StreamMonitor(reader1);
			proxy = new IStreamsProxy() {
				@Override
				public IStreamMonitor getErrorStreamMonitor() {
					return errorStreamMonitor;
				}

				@Override
				public IStreamMonitor getOutputStreamMonitor() {
					return outputStreamMonitor;
				}

				@Override
				public void write(String input) throws IOException {
				}

			};
		}
		return proxy;
	}

	@Override
	public void setAttribute(String key, String value) {
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	@Override
	public synchronized boolean canTerminate() {
		return !terminated;
	}

	@Override
	public synchronized boolean isTerminated() {
		return terminated;
	}

	@Override
	public void terminate() throws DebugException {
	    processMonitor.interrupt();
	}

	protected void onTerminate() {
	    fireTerminated();
	    interactor.stop();
	}

    public void setThread(LSLThread thread) {
        this.thread = thread;
    }

    public synchronized void addListener(IProcessListener l) {
        this.listeners.add(l);
    }

    public synchronized void removeListener(IProcessListener l) {
        this.listeners.remove(l);
    }

    private void fireTerminated() {
       DebugPlugin.getDefault().fireDebugEventSet(new DebugEvent[]{new DebugEvent(this, DebugEvent.TERMINATE)});
       IProcessListener[] listenerArray;
       synchronized (this) {
           terminated = true;
           listenerArray = listeners.toArray(new IProcessListener[listeners.size()]);
       }
       for (IProcessListener element : listenerArray) {
            element.processTerminated(this);
        }
    }
}