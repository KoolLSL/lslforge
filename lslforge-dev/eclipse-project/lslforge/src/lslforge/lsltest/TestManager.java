package lslforge.lsltest;

import java.util.HashSet;
import java.util.LinkedList;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import lslforge.testview.TestRunnerViewPart;
import lslforge.util.Log;

public class TestManager {

    private HashSet<ITestListener> listeners = new HashSet<>();
    private ILaunchConfiguration currentConfig = null;
    private ILaunch launch;
    private LinkedList<TestResult> testResults = new LinkedList<>();
    private boolean active = false;
    private int numTests = 0;
    private int numRun = 0;
    private int numFailures;
    private int numErrors;
    public TestManager() {
        DebugPlugin.getDefault().addDebugEventListener(new IDebugEventSetListener() {
            @Override
			public void handleDebugEvents(DebugEvent[] events) {
                if (events != null && events.length > 0) {
                    if (events[0].getKind() == DebugEvent.TERMINATE) {
                        active = false;

                        for (ITestListener listener : listeners) {
                            listener.testFinished();
                        }
                    }
                }
            }
        });
    }

    public void postTestResult(TestResult result) {
        numRun++;
        if (result.getResultInfo().getResultCode() == TestResult.ERROR) numErrors++;
        if (result.getResultInfo().getResultCode() == TestResult.FAILURE) numFailures++;
        for (ITestListener listener : listeners) {
            listener.newTestResult(result);
        }
    }

    public void addResultListener(ITestListener listener) {
        listeners.add(listener);
    }

    public void removeResultListener(ITestListener listener) {
        listeners.remove(listener);
    }

    public boolean hasCurrentConfig() {
        return currentConfig != null;
    }

    public void rerunCurrentTests() {
        if (!active && hasCurrentConfig())   DebugUITools.launch(currentConfig, launch.getLaunchMode());
    }

    public int getNumTests() {
        return numTests;
    }

    public void testLaunched(ILaunchConfiguration config, ILaunch launch, int numTests) {

        testResults.clear();
        currentConfig = config;
        this.launch = launch;
        active = true;
        this.numTests = numTests;
        this.numRun = 0;
        this.numFailures = 0;
        this.numErrors = 0;
        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
            @Override
			public void run() { showTestRunnerViewPartInActivePage(findTestRunnerViewPartInActivePage());}
        });
        for (ITestListener listener : listeners) {
            listener.testLaunched(numTests);
        }
    }

    public boolean canLaunch() {
        return !active;
    }

    private TestRunnerViewPart showTestRunnerViewPartInActivePage(TestRunnerViewPart testRunner) {
        IWorkbenchPage page= null;
        try {
            try {
                page= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            } catch (NullPointerException e) {
            }

            if (page == null)
                return null;

            if (testRunner != null && testRunner.isCreated()) {
                page.bringToTop(testRunner);
                return testRunner;
            }
            //  show the result view if it isn't shown yet
            return (TestRunnerViewPart) page.showView(TestRunnerViewPart.ID);
        } catch (PartInitException pie) {
            Log.error(pie);
            return null;
        } finally{
            //restore focus stolen by the creation of the result view
//            if (page != null && activePart != null)
//                page.activate(activePart);
        }
    }

    private TestRunnerViewPart findTestRunnerViewPartInActivePage() {
        IWorkbenchPage page= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page == null)
            return null;
        return (TestRunnerViewPart) page.findView(TestRunnerViewPart.ID);
    }

    public int getNumIgnored() {
        return 0;
    }

    public int getNumRun() {
        return numRun;
    }

    public int getNumErrors() {
        return numErrors;
    }

    public int getNumFailures() {
        return numFailures;
    }
}
