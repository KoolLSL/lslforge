package lslforge.debug;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;

import lslforge.debug.LSLScriptExecutionState.Binding;
import lslforge.debug.LSLScriptExecutionState.Frame;

public class LSLThread implements IThread, InteractorListener {

    private LSLStackFrame[] stackFrames;
    private LSLDebugTarget target;
    private boolean active;
    private boolean suspended;
    private boolean stepping;
    private boolean terminated;
    private Interactor interactor;
    private String name = "LSLThread"; //$NON-NLS-1$

    public LSLThread(LSLDebugTarget target) {
        this.target = target;
        this.active = true;
        this.suspended = false;
        this.stepping = false;
        stackFrames = new LSLStackFrame[0];
    }

    @Override
	public IBreakpoint[] getBreakpoints() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
	public String getName() throws DebugException {
        return name;
    }

    @Override
	public int getPriority() throws DebugException {
        return 0;
    }

    @Override
	public IStackFrame[] getStackFrames() throws DebugException {
        return stackFrames;
    }

    @Override
	public IStackFrame getTopStackFrame() throws DebugException {
        if (stackFrames != null && stackFrames.length > 0) return stackFrames[0];
        return null;
    }

    @Override
	public boolean hasStackFrames() throws DebugException {
        return (stackFrames != null && stackFrames.length > 0);
    }

    public void setStackFrames(LSLStackFrame[] stackFrames) {
        if (stackFrames == null) stackFrames = new LSLStackFrame[0];
        this.stackFrames = stackFrames;
    }

    @Override
	public IDebugTarget getDebugTarget() {
        return target;
    }

    @Override
	public ILaunch getLaunch() {
        return getDebugTarget().getLaunch();
    }

    @Override
	public String getModelIdentifier() {
        return getDebugTarget().getModelIdentifier();
    }

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
	public boolean canResume() {
        return active && suspended;
    }

    @Override
	public boolean canSuspend() {
        return active && !suspended;
    }

    @Override
	public boolean isSuspended() {
        return suspended;
    }

    @Override
	public void resume() throws DebugException {
        doResume();
        interactor.continueExecution();
    }

    @Override
	public void suspend() throws DebugException {
        // TODO Auto-generated method stub

    }

    @Override
	public boolean canStepInto() {
        return canResume();
    }

    @Override
	public boolean canStepOver() {
        return canResume();
    }

    @Override
	public boolean canStepReturn() {
        return canResume();
    }

    @Override
	public boolean isStepping() {
        return stepping;
    }

    @Override
	public void stepInto() throws DebugException {
        doResume();
        interactor.step();
    }

    private void doResume() {
        suspended = false;
        debugPlugin().fireDebugEventSet(new DebugEvent[] {
                new DebugEvent(this, DebugEvent.RESUME, DebugEvent.BREAKPOINT)
        });
    }

    @Override
	public void stepOver() throws DebugException {
        doResume();
        interactor.stepOver();
    }

    @Override
	public void stepReturn() throws DebugException {
        doResume();
        interactor.stepOut();
    }

    @Override
	public boolean canTerminate() {
        return false;
    }

    @Override
	public boolean isTerminated() {
        return terminated;
    }

    @Override
	public void terminate() throws DebugException {
    }

    public void setSuspended(boolean b) {
        this.suspended = b;
        stepping = false;
    }

    @Override
	public void completed() {
        this.terminated = true;
        debugPlugin().fireDebugEventSet(new DebugEvent[] {
                new DebugEvent(this, DebugEvent.TERMINATE)
        });
        target.setTerminated();
    }

    @Override
	public void suspended(LSLScriptExecutionState state) {
        setSuspended(true);
        this.name = state.getThreadInfo().getName();

        this.stackFrames = new LSLStackFrame[state.getThreadInfo().getFrames().length];

        for (int i = 0; i < this.stackFrames.length; i++) {
            Frame frame = state.getThreadInfo().getFrames()[i];
            LSLVariable[] variables = new LSLVariable[frame.getBindings().length];

            int line = 0;

            if (i == 0) line = state.getCurrentLine();
            for (int j = 0; j < variables.length; j++) {
                Binding b = frame.getBindings()[j];
                variables[j] = new LSLVariable(b.getName(), b.getVal().typeString(),
                        b.getVal().toString(), this.target);
            }
            this.stackFrames[i] = new LSLStackFrame(frame.getName(), frame.getFile(),
                    this, this.getDebugTarget(), variables, line, i == 0);
        }

        debugPlugin().fireDebugEventSet(new DebugEvent[] {
                new DebugEvent(this, DebugEvent.SUSPEND,
                        DebugEvent.BREAKPOINT)
        });

        target.setSuspended();
    }

    private DebugPlugin debugPlugin() {
        return DebugPlugin.getDefault();
    }

    public void setInteractor(Interactor interactor) {
        this.interactor = interactor;
    }
}
