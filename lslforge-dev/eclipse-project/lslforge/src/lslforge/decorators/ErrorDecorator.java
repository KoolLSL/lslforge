package lslforge.decorators;

import java.util.LinkedList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;

import lslforge.LSLForgePlugin;
import lslforge.LSLProjectNature;
import lslforge.util.Log;
import lslforge.util.Util;

/**
 * Decorate the navigator with error indications.
 */
public class ErrorDecorator implements ILightweightLabelDecorator {
    private static final String ICON_PATH = "icons/error_decorator.gif"; //NON-NLS-1 //$NON-NLS-1$

    private LinkedList<ILabelProviderListener> labelProviderListeners = new LinkedList<>();

	private ImageDescriptor descriptor;

	public ErrorDecorator() {
		LSLForgePlugin.getDefault().setErrorDecorator(this);
		descriptor = Util.findDescriptor(ICON_PATH);
	}

	@Override
	public void decorate(Object element, IDecoration decoration) {
		if (! (element instanceof IResource)) return;
		IResource resource = (IResource) element;
		if(!resource.exists() || (resource instanceof IWorkspaceRoot)) return;		//Skip the workspace root (not in a project anyways)
		IProject project = resource.getProject();
		if (project == null) {
			Log.error(Messages.getString("ErrorDecorator.PROJECT_FOR") + resource.getName() + Messages.getString("ErrorDecorator.IS_NULL"), new Throwable()); //$NON-NLS-1$ //$NON-NLS-2$
			return;
		}
		try {
		    if (!project.isOpen()) return;
			//project.open(null);
			if (project.hasNature(LSLProjectNature.ID)) {
				LSLProjectNature nature = (LSLProjectNature) project.getNature(LSLProjectNature.ID);

				if (nature == null) return;

				IMarker[] m = resource.findMarkers("lslforge.problem", true, IResource.DEPTH_INFINITE); //$NON-NLS-1$

				if (m == null || m.length == 0) return;
			} else {
				return;
			}
		} catch (CoreException e) {
			Log.error("exception caught trying to determine project nature!", e); //$NON-NLS-1$
			return;
		}

		decoration.addOverlay(descriptor,IDecoration.BOTTOM_LEFT);
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		labelProviderListeners.add(listener);
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
		labelProviderListeners.remove(listener);
	}

	public void errorStatusChanged() {
		for (ILabelProviderListener listener : labelProviderListeners) {
			listener.labelProviderChanged(new LabelProviderChangedEvent(this));
		}
	}
}