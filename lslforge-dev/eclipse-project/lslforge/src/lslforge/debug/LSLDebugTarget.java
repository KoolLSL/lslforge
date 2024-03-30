package lslforge.debug;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IThread;

import lslforge.LSLForgePlugin;

public class LSLDebugTarget implements IDebugTarget, IProcessListener {
    public static final String LSLFORGE = "lslforge"; //$NON-NLS-1$
    private String name;
    private LSLProcess process;
    private LSLThread thread;
    private IThread[] threads;
    private ILaunch launch;
    private boolean suspended = false;
    private boolean terminated;
    public LSLDebugTarget(String name, ILaunch launch, LSLProcess process) {
        this.name = name;
        this.process = process;
        this.launch = launch;
        thread = new LSLThread(this);
        threads = new LSLThread[] { thread };
        process.setThread(thread);
        process.addListener(this);
    }

    @Override
	public String getName() throws DebugException {
        return name;
    }

    @Override
	public IProcess getProcess() {
        return process;
    }

    @Override
	public IThread[] getThreads() throws DebugException {
        return threads;
    }

    @Override
	public boolean hasThreads() throws DebugException {
        return true;
    }

    @Override
	public boolean supportsBreakpoint(IBreakpoint breakpoint) {
        return (breakpoint instanceof ILineBreakpoint);
    }

    @Override
	public String getModelIdentifier() {
        return LSLFORGE;
    }

    @Override
	public boolean canTerminate() {
        return false;
    }

    @Override
	public boolean isTerminated() {
        return terminated;
    }

    public void setTerminated() {
        this.terminated = true;
        DebugPlugin.getDefault().fireDebugEventSet(
                new DebugEvent[] {
                        new DebugEvent(this, DebugEvent.TERMINATE)
                });
    }

    @Override
	public void terminate() throws DebugException {
    }

    @Override
	public boolean canResume() {
        return suspended;
    }

    @Override
	public boolean canSuspend() {
        return false;
    }

    @Override
	public boolean isSuspended() {
        return suspended;
    }

    @Override
	public void resume() throws DebugException {
    }

    @Override
	public void suspend() throws DebugException { }
    void setSuspended() {
        suspended = true;
        DebugPlugin.getDefault().fireDebugEventSet(
            new DebugEvent[] {
                    new DebugEvent(this, DebugEvent.SUSPEND, DebugEvent.BREAKPOINT)
            });
    }

    @Override
	public void breakpointAdded(IBreakpoint breakpoint) {
        // TODO Auto-generated method stub

    }

    @Override
	public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
        // TODO Auto-generated method stub

    }

    @Override
	public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
        // TODO Auto-generated method stub

    }

    @Override
	public boolean canDisconnect() {
        return false;
    }

    @Override
	public void disconnect() throws DebugException {
        throw notSupported();
    }

    private DebugException notSupported() throws DebugException {
        return new DebugException(
                new Status(IStatus.ERROR,LSLForgePlugin.PLUGIN_ID,
                        DebugException.NOT_SUPPORTED,"",null)); //$NON-NLS-1$
    }

    @Override
	public boolean isDisconnected() {
        return false;
    }

    @Override
	public IMemoryBlock getMemoryBlock(long startAddress, long length) throws DebugException {
        throw notSupported();
    }

    @Override
	public boolean supportsStorageRetrieval() {
        return false;
    }

    @Override
	public IDebugTarget getDebugTarget() {
        return this;
    }

    @Override
	public ILaunch getLaunch() {
        return launch;
    }

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {

        if (ILaunch.class.equals(adapter)) {
            return getLaunch();
        }
        return null;
    }

    @Override
	public void processTerminated(LSLProcess p) {
        setTerminated();
        p.removeListener(this);
    }

}
