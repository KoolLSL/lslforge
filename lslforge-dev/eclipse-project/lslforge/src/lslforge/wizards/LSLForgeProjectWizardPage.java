package lslforge.wizards;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import lslforge.Messages;

public class LSLForgeProjectWizardPage extends WizardPage implements ModifyListener {
    private static final int SIZING_TEXT_FIELD_WIDTH = 250;
	private Text nameCtrl;
	private Label labelCtrl;
	protected LSLForgeProjectWizardPage(String pageName) {
		super(pageName);
		setTitle(Messages.LSLForgeProjectWizardPage_CreateAnLSLForgeProject);
		setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		Composite pageControl = new Composite(parent, SWT.NONE);
		pageControl.setLayout(new GridLayout());
		pageControl.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
				| GridData.HORIZONTAL_ALIGN_FILL));
		pageControl.setFont(parent.getFont());
		labelCtrl = new Label(pageControl, SWT.HORIZONTAL | SWT.LEFT | SWT.SHADOW_NONE);
		labelCtrl.setText(Messages.LSLForgeProjectWizardPage_ProjectName);
		labelCtrl.setEnabled(true);
		nameCtrl = new Text(pageControl, SWT.SINGLE | SWT.BORDER);
	    nameCtrl.setEnabled(true);
	    nameCtrl.addModifyListener(this);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        nameCtrl.setLayoutData(data);

	    setControl(pageControl);
	}

	protected GridLayout initGridLayout(GridLayout layout, boolean margins) {
		layout.horizontalSpacing= convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.verticalSpacing= convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		if (margins) {
			layout.marginWidth= convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
			layout.marginHeight= convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		} else {
			layout.marginWidth= 0;
			layout.marginHeight= 0;
		}
		return layout;
	}

	public String getProjectName() {
		return nameCtrl.getText();
	}

	@Override
	public void modifyText(ModifyEvent e) {
		if (e.getSource() == nameCtrl) {
			String name = nameCtrl.getText();
			if (name.trim().length() == 0) {
				setPageComplete(false);
				setErrorMessage(null);
				setMessage(Messages.LSLForgeProjectWizardPage_ENTER_PROJECT_NAME);
			} else {
				IStatus status = ResourcesPlugin.getWorkspace().validateName(name, IResource.PROJECT);
				if (!status.isOK()) {
					setErrorMessage(status.getMessage());
					setPageComplete(false);
				} else if (ResourcesPlugin.getWorkspace().getRoot().getProject(name).exists()) {
					setErrorMessage(Messages.LSLForgeProjectWizardPage_PROJECT_ALREADY_EXISTS);
					setPageComplete(false);
				} else if (Platform.getLocation().append(name).toFile().exists()) {
					setErrorMessage(Messages.LSLForgeProjectWizardPage_FOLDER_EXISTS_IN_WORKSPACE);
					setPageComplete(false);
				} else {
					setPageComplete(true);
				}
			}
		}
	}
}
