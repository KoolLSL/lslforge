package lslforge.debug;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;

public class LSLStackFrame implements IStackFrame{
    private static final IRegisterGroup[] EMPTY_REGISTER_GROUP =
        new IRegisterGroup[0];

    private boolean top;
    private String name;
    private String file;
    private IThread thread;
    private IVariable[] variables;
    //private boolean stepping;
    private int line;
    public LSLStackFrame(
            String name,
            String file,
            IThread thread,
            IDebugTarget debugTarget,
            IVariable[] variables,
            int line,
            boolean top) {
        this.name = name;
        this.thread = thread;
        this.file = file;
        this.variables = variables;
        this.line = line;
        this.top = top;
    }

    @Override
	public int getCharEnd() throws DebugException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
	public int getCharStart() throws DebugException {
        return -1;
    }

    @Override
	public int getLineNumber() throws DebugException {
        return line;
    }

    @Override
	public String getName() throws DebugException {
        return name;
    }

    @Override
	public IRegisterGroup[] getRegisterGroups() throws DebugException {
        return EMPTY_REGISTER_GROUP;
    }

    @Override
	public IThread getThread() {
        return thread;
    }

    @Override
	public IVariable[] getVariables() throws DebugException {
        return variables;
    }

    @Override
	public boolean hasRegisterGroups() throws DebugException {
        return false;
    }

    @Override
	public boolean hasVariables() throws DebugException {
        return variables != null && variables.length > 0;
    }

    @Override
	public IDebugTarget getDebugTarget() {
        return thread.getDebugTarget();
    }

    @Override
	public ILaunch getLaunch() {
        return thread.getLaunch();
    }

    @Override
	public String getModelIdentifier() {
        return getDebugTarget().getModelIdentifier();
    }

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
        //Log.log("LSLStackFrame - asked to adapt to: " + adapter);
        return null;
    }

    @Override
	public boolean canStepInto() {
        return top && isSuspended();
    }

    @Override
	public boolean canStepOver() {
        return top && isSuspended();
    }

    @Override
	public boolean canStepReturn() {
        return top && isSuspended();
    }

    @Override
	public boolean isStepping() {
        return !isSuspended();
    }

    @Override
	public void stepInto() throws DebugException {
        thread.stepInto();
    }

    @Override
	public void stepOver() throws DebugException {
        thread.stepOver();
    }

    @Override
	public void stepReturn() throws DebugException {
        thread.stepReturn();
    }

    @Override
	public boolean canResume() {
        return thread.canResume();
    }

    @Override
	public boolean canSuspend() {
        return false;
    }

    @Override
	public boolean isSuspended() {
        return thread.isSuspended();
    }

    @Override
	public void resume() throws DebugException {
        thread.resume();
    }

    @Override
	public void suspend() throws DebugException {
    }

    @Override
	public boolean canTerminate() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
	public boolean isTerminated() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
	public void terminate() throws DebugException {
        // TODO Auto-generated method stub

    }

    public String getFile() {
        return file;
    }

}
