package lslforge.debug;

import java.util.HashSet;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.ISourcePresentation;
import org.eclipse.debug.ui.IValueDetailListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;

public class LSLDebugModelPresentation implements IDebugModelPresentation {
    private ISourcePresentation presentation = new LSLSourceLocator();
    private HashSet<ILabelProviderListener> listeners = new HashSet<>();
    @Override
	public void computeDetail(IValue value, IValueDetailListener listener) {
        String result;
        try {
            result = value.getValueString();
        } catch (DebugException e) {
            result = ""; //$NON-NLS-1$
        }
        listener.detailComputed(value, result);
    }

    @Override
	public Image getImage(Object element) {
        return null;
    }

    @Override
	public String getText(Object element) {
        return null;
    }

    @Override
	public void setAttribute(String attribute, Object value) {
    }

    @Override
	public void addListener(ILabelProviderListener listener) {
        listeners.add(listener);
    }

    @Override
	public void dispose() {
    }

    @Override
	public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    @Override
	public void removeListener(ILabelProviderListener listener) {
        listeners.remove(listener);
    }

    @Override
	public String getEditorId(IEditorInput input, Object element) {
        return presentation.getEditorId(input, element);
    }

    @Override
	public IEditorInput getEditorInput(Object element) {
        return presentation.getEditorInput(element);
    }

}
