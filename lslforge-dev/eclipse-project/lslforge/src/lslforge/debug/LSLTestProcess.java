package lslforge.debug;

import org.eclipse.debug.core.ILaunch;

import lslforge.LSLForgePlugin;
import lslforge.launching.Messages;

public class LSLTestProcess extends LSLProcess {
    private static final String UNIT_TESTER = "UnitTester"; //$NON-NLS-1$
    private String descriptor;
    public LSLTestProcess(String descriptor, ILaunch launch) {
        super(launch);
        this.descriptor = descriptor;
    }

    @Override
	protected Interactor createInteractor(Process p) {
        return new LSLTestInteractor(launch.getLaunchMode(),LSLForgePlugin.getDefault().getTestManager(),
                descriptor, p.getInputStream(), p.getOutputStream());
    }

    @Override
	protected Process launchExecutable() {
        return LSLForgePlugin.launchCoreCommand(UNIT_TESTER, false);
    }

    @Override
	public String getLabel() {
        return Messages.getString("TestLaunchDelegate.TEST"); //$NON-NLS-1$
    }

}
