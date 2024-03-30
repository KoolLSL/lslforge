package lslforge.project;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import lslforge.LSLProjectNature;
import lslforge.util.Log;

public class RemoveSupportHandler extends AbstractHandler
{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IProject project = ProjectContributionItem.getSelectedProject();
		if(project != null) {
			try {
				LSLProjectNature.removeProjectNature(project);
				MessageDialog.openInformation(HandlerUtil.getActiveShellChecked(event), Messages.RemoveSupportHandler_removeSupport, Messages.RemoveSupportHandler_removeSupportReply);
			} catch (CoreException e) {
				Log.error(e);
				MessageDialog.openInformation(
						HandlerUtil.getActiveShellChecked(event),
						Messages.RemoveSupportHandler_removeSupport,
						Messages.RemoveSupportHandler_removeSupportErr + e.getMessage()
				);
			}
		}
		return null;
	}

}
