package lslforge.preferences;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import lslforge.LSLForgePlugin;
import lslforge.LSLProjectNature;
import lslforge.util.Log;

public class LSLForgePreferencePage extends FieldEditorPreferencePage implements
        IWorkbenchPreferencePage {

    private static final String LSLFORGE_PREFERENCE_PAGE_ENABLE_OPTIMIZATIONS = "LSLForgePreferencePage.ENABLE_OPTIMIZATIONS"; //$NON-NLS-1$
    private static final String LSLFORGE_EXECUTABLE_PATH = "LSLForgePreferencePage.LSLForgeExecutablePath"; //$NON-NLS-1$

    public LSLForgePreferencePage() throws IOException {
        setPreferenceStore(LSLForgePlugin.getDefault().getPreferenceStore());
    }

    @Override
	public void init(IWorkbench workbench) {
    }

    @Override
	protected IPreferenceStore doGetPreferenceStore() {
        return LSLForgePlugin.getDefault().getPreferenceStore();
    }

    @Override
	protected void createFieldEditors() {
        addField(new FileFieldEditor(LSLForgePlugin.LSLFORGE_NATIVE_PATH,
                Messages.getString(LSLFORGE_EXECUTABLE_PATH), getFieldEditorParent()));
        addField(new BooleanFieldEditor(LSLProjectNature.OPTIMIZE,
                Messages.getString(LSLFORGE_PREFERENCE_PAGE_ENABLE_OPTIMIZATIONS), getFieldEditorParent()));
    }

    @Override
	public boolean performOk() {
        if (super.performOk()) {
            IProject[] p = ResourcesPlugin.getWorkspace().getRoot().getProjects();
            for (IProject element : p) {
                try {
                	if (element.isOpen()) {
                      LSLProjectNature nature = (LSLProjectNature) element.getNature(LSLProjectNature.ID);
                      if (nature != null) nature.scheduleBuild(true,null,null);
                	}
                } catch (CoreException e) {
                    Log.error("problem determining project nature", e); //$NON-NLS-1$
                }
            }
            return true;
        } else return false;
    }
}
