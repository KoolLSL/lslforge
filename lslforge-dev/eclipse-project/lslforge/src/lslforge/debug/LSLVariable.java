package lslforge.debug;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

import lslforge.LSLForgePlugin;

public class LSLVariable implements IVariable {

    private String name;
    private String typeName;
    private LSLDebugValue value;
    private LSLDebugTarget target;

    public LSLVariable(String name, String typeName, String valueString,
            LSLDebugTarget target) {
        this.name = name;
        this.typeName = typeName;
        this.value = new LSLDebugValue(typeName, valueString, target);
        this.target = target;
    }
    @Override
	public String getName() throws DebugException {
        return name;
    }

    @Override
	public String getReferenceTypeName() throws DebugException {
        return typeName;
    }

    @Override
	public IValue getValue() throws DebugException {
        return value;
    }

    @Override
	public boolean hasValueChanged() throws DebugException {
        return true;
    }

    @Override
	public IDebugTarget getDebugTarget() {
        return target;
    }

    @Override
	public ILaunch getLaunch() {
        return target.getLaunch();
    }

    @Override
	public String getModelIdentifier() {
        return target.getModelIdentifier();
    }

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
        return null;
    }

    @Override
	public void setValue(String expression) throws DebugException {
        throw new DebugException(new Status(IStatus.ERROR, LSLForgePlugin.PLUGIN_ID,
                DebugException.NOT_SUPPORTED, "",null)); //$NON-NLS-1$
    }

    @Override
	public void setValue(IValue value) throws DebugException {
        throw new DebugException(new Status(IStatus.ERROR, LSLForgePlugin.PLUGIN_ID,
                DebugException.NOT_SUPPORTED, "",null)); //$NON-NLS-1$
    }

    @Override
	public boolean supportsValueModification() {
        return false;
    }

    @Override
	public boolean verifyValue(String expression) throws DebugException {
        throw new DebugException(new Status(IStatus.ERROR, LSLForgePlugin.PLUGIN_ID,
                DebugException.NOT_SUPPORTED, "",null)); //$NON-NLS-1$
    }

    @Override
	public boolean verifyValue(IValue value) throws DebugException {
        throw new DebugException(new Status(IStatus.ERROR, LSLForgePlugin.PLUGIN_ID,
                DebugException.NOT_SUPPORTED, "",null)); //$NON-NLS-1$
    }

}
