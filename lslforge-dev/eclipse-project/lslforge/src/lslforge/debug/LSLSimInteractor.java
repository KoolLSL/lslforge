package lslforge.debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IBreakpoint;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import lslforge.LSLForgePlugin;
import lslforge.SimManager;
import lslforge.sim.SimEvent;
import lslforge.sim.SimEventListener;
import lslforge.sim.SimStatuses;
import lslforge.sim.SimStatuses.SimEnded;
import lslforge.sim.SimStatuses.SimInfo;
import lslforge.sim.SimStatuses.SimStatus;
import lslforge.sim.SimStatuses.SimSuspended;
import lslforge.util.Log;
import lslforge.util.Util;

/**
 * Interact with a running test session.
 */
public class LSLSimInteractor implements Runnable, Interactor, SimEventListener {
    private static class BreakpointData {
        private static XStream xstream = new XStream(new DomDriver());
        public String file;
        public int line;
        public BreakpointData(String file, int line) {
            this.file = file;
            this.line = line;
        }

        public static void configureXStream(XStream xstream) {
            xstream.allowTypesByWildcard(new String[] { "lslforge.debug.**" });
            xstream.alias("breakpoint", BreakpointData.class); //$NON-NLS-1$
        }

        static {
            configureXStream(xstream);
        }
    }

    private abstract static class SimCommand {
        protected BreakpointData[] breakpoints = null;
        protected SimEvent[] events;
        protected SimCommand(BreakpointData[] breakpoints, SimEvent[] events) {
            this.breakpoints = breakpoints;
            this.events = events;
        }
    }

    private static class ContinueCommand extends SimCommand {
        private static XStream xstream = new XStream(new DomDriver());
        public ContinueCommand(BreakpointData[] breakpoints, SimEvent[] events) {
            super(breakpoints, events);
        }

        static {
            xstream.allowTypesByWildcard(new String[] { "lslforge.debug.**" });
            xstream.alias("sim-continue", ContinueCommand.class); //$NON-NLS-1$
            BreakpointData.configureXStream(xstream);
            SimEvent.configureXStream(xstream);
        }

        public static String toXML(ContinueCommand cmd) {
            return xstream.toXML(cmd);
        }
    }

    private static class StepCommand extends SimCommand {
        private static XStream xstream = new XStream(new DomDriver());
        public StepCommand(BreakpointData[] breakpoints, SimEvent[] events) {
            super(breakpoints, events);
        }

        static {
            xstream.allowTypesByWildcard(new String[] { "lslforge.debug.**" });
            xstream.alias("sim-step", StepCommand.class); //$NON-NLS-1$
            BreakpointData.configureXStream(xstream);
        }

        public static String toXML(StepCommand cmd) {
            return xstream.toXML(cmd);
        }
    }

    private static class StepOverCommand extends SimCommand {
        private static XStream xstream = new XStream(new DomDriver());
        public StepOverCommand(BreakpointData[] breakpoints, SimEvent[] events) {
            super(breakpoints, events);
        }

        static {
            xstream.allowTypesByWildcard(new String[] { "lslforge.debug.**" });
            xstream.alias("sim-step-over", StepOverCommand.class); //$NON-NLS-1$
            BreakpointData.configureXStream(xstream);
        }

        public static String toXML(StepOverCommand cmd) {
            return xstream.toXML(cmd);
        }
    }

    private static class StepOutCommand extends SimCommand {
        private static XStream xstream = new XStream(new DomDriver());
        public StepOutCommand(BreakpointData[] breakpoints, SimEvent[] events) {
            super(breakpoints, events);
        }

        static {
            xstream.allowTypesByWildcard(new String[] { "lslforge.debug.**" });
            xstream.alias("sim-step-out", StepOutCommand.class); //$NON-NLS-1$
            BreakpointData.configureXStream(xstream);
        }

        public static String toXML(StepOutCommand cmd) {
            return xstream.toXML(cmd);
        }
    }


    private HashSet<InteractorListener> listeners = new HashSet<>();
    private BufferedReader reader;
    private PrintStream writer;
    private String simDescriptor;
    private Thread thread;
    private boolean done = false;
    private boolean debugMode;
    private LinkedList<SimEvent> eventQueue = new LinkedList<>();

    public LSLSimInteractor(String launchMode, String simDescriptor, InputStream in, OutputStream out) {
        reader = new BufferedReader(new InputStreamReader(in));
        writer = new PrintStream(out);

        this.simDescriptor = simDescriptor;
        this.debugMode = ILaunchManager.DEBUG_MODE.equals(launchMode);
    }

    @Override
	public void start() {
        if (done || thread != null && thread.isAlive()) return;
        simManager().addSimEventListener(this);
        writeOut(simDescriptor);
        writeOut(continueText());
        thread = new Thread(this);
        thread.start();
    }

    @Override
	public void stop() {
        simManager().removeSimEventListener(this);
    }

    private String continueText() {
        BreakpointData[] bpData = null;
        if (debugMode) {
            bpData = createBreakpointData();
        }
        ContinueCommand cmd = new ContinueCommand(bpData,getAllPendingEvents());
        return ContinueCommand.toXML(cmd);
    }

    private String stepText() {
        BreakpointData[] bpData = null;
        if (debugMode) {
            bpData = createBreakpointData();
        }
        StepCommand cmd = new StepCommand(bpData, getAllPendingEvents());
        return StepCommand.toXML(cmd);
    }

    private String stepOverText() {
        BreakpointData[] bpData = null;
        if (debugMode) {
            bpData = createBreakpointData();
        }
        StepOverCommand cmd = new StepOverCommand(bpData,getAllPendingEvents());
        return StepOverCommand.toXML(cmd);
    }

    private String stepOutText() {
        BreakpointData[] bpData = null;
        if (debugMode) {
            bpData = createBreakpointData();
        }
        StepOutCommand cmd = new StepOutCommand(bpData, getAllPendingEvents());
        return StepOutCommand.toXML(cmd);
    }

    private BreakpointData[] createBreakpointData() {
        IBreakpointManager bpm = getBreakpointManager();
        IBreakpoint[] breakpoints = bpm.getBreakpoints(LSLDebugTarget.LSLFORGE);
        LinkedList<BreakpointData> list = new LinkedList<>();
        for (IBreakpoint breakpoint : breakpoints) {
            try {
                if (breakpoint instanceof LSLLineBreakpoint) {
                    LSLLineBreakpoint bp = (LSLLineBreakpoint) breakpoint;
                    int line = bp.getLineNumber();
                    IMarker marker = bp.getMarker();
                    IResource resource = marker.getResource();
                    IFile file = resource.getAdapter(IFile.class);
                    if ((file == null) || !marker.getAttribute(IBreakpoint.ENABLED,false)) continue;
                    IPath fullPath = file.getLocation();
                    list.add(new BreakpointData(fullPath.toOSString(), line));
                }
            } catch (CoreException e) {
                Log.error(e);
            }
        }
        return list.toArray(new BreakpointData[list.size()]);
    }

    @Override
	public void continueExecution() {
        sendCommand(continueText());
    }

    private void sendCommand(String commandText) {
        if (done || thread != null && thread.isAlive()) return;
        writeOut(commandText);
        thread = new Thread(this);
        thread.start();
    }

    @Override
	public void step() {
        sendCommand(stepText());
    }

    @Override
	public void stepOver() {
        sendCommand(stepOverText());
    }

    @Override
	public void stepOut() {
        sendCommand(stepOutText());
    }

    @Override
	public void addListener(InteractorListener listener) { listeners.add(listener); }
    @Override
	public void removeListener(InteractorListener listener) { listeners.remove(listener); }

    public void close() {
        writer.close();
    }

    private void fireSuspended(LSLScriptExecutionState state) {
        for (Iterator<InteractorListener> i = listeners.iterator(); i.hasNext();) {
            i.next().suspended(state);
        }
    }

    private void fireComplete() {
        for (Iterator<InteractorListener> i = listeners.iterator(); i.hasNext();) {
            i.next().completed();
        }
    }
    @Override
	public void run() {
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                SimStatus status = SimStatuses.fromXML(Util.URIDecode(line));

                // kludge for the mo'
                if (status instanceof SimInfo) {
                    String cmd = continueText();
                    SimInfo info = (SimInfo)status;
                    simManager().setSimState(info.getState(), info.getMessages());
                    //Log.debug("writing: " + cmd); //$NON-NLS-1$
                    writeOut(cmd);
                } else if (status instanceof SimEnded) {
                    Log.debug(Util.URIDecode(line));
                    SimEnded ended = (SimEnded) status;
                    simManager().setSimState(ended.getState(), ended.getMessages());
                    endSession();
                    fireComplete();
                } else if (status instanceof SimSuspended) {
                    Log.debug("hit a breakpoint... suspending!"); //$NON-NLS-1$
                    simManager().setSimState(status.getState(), status.getMessages());
                    fireSuspended(((SimSuspended)status).getScriptState());
                    return;
                } else {
                    Log.error("Unrecognized status: " + status); //$NON-NLS-1$
                }
            }
        } catch (IOException e) {
            Log.error(e);
        } catch (RuntimeException e) {
            Log.error(e);
            if (line != null) {
                Log.debug("input was: " + Util.URIDecode(line)); //$NON-NLS-1$
            }
            try {
                endSession();
            } catch (Exception e1) {
            }
        }
    }

    private SimManager simManager() {
        return LSLForgePlugin.getDefault().getSimManager();
    }

    private DebugPlugin getDebugPlugin() { return DebugPlugin.getDefault(); }
    private IBreakpointManager getBreakpointManager() { return getDebugPlugin().getBreakpointManager(); }

    private void writeOut(String cmd) {
        writer.println(Util.URIEncode(cmd));
        writer.flush();
    }

    private void endSession() {
        writer.println("quit"); //$NON-NLS-1$
        writer.flush();
        writer.close();
    }

    @Override
	public void putEvent(SimEvent event) {
        synchronized (eventQueue) {
            eventQueue.add(event);
        }
    }

    private SimEvent[] getAllPendingEvents() {
        synchronized (eventQueue) {
            SimEvent[] events = eventQueue.toArray(new SimEvent[eventQueue.size()]);
            eventQueue.clear();
            return events;
        }
    }
}
