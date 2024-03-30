package lslforge.simview;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import lslforge.sim.SimStatuses;

public class LogViewerContentProvider implements ITreeContentProvider {
    private static final int MAX_MSGS = 250;
    private java.util.LinkedList<SimStatuses.Message> logMessages = new LinkedList<>();
    private java.util.LinkedList<SimStatuses.Message> archive = null;
    private HashSet<SimStatuses.Message> archiveSet = new HashSet<>();
    @Override
	public Object[] getChildren(Object parentElement) {
        if ("archive".equals(parentElement)) return archive.toArray(); //$NON-NLS-1$
        return null;
    }

    @Override
	public Object getParent(Object element) {
        if (archiveSet.contains(element)) return "archive"; //$NON-NLS-1$
        return null;
    }

    @Override
	public boolean hasChildren(Object element) {
        return "archive".equals(element); //$NON-NLS-1$
    }

    @Override
	public Object[] getElements(Object inputElement) {
        Object[] os = null;
        if (archive == null) os = logMessages.toArray();
        else {
            os =logMessages.toArray(new Object[logMessages.size() + 1]);
            os[os.length - 1] = "archive"; //$NON-NLS-1$
        }
        return os;
    }

    @Override
	public void dispose() {
        logMessages.clear();  // yes, this is unnecessary...
        archive = null;
        archiveSet.clear();
    }

    @Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    public void addMessages(SimStatuses.Message[] messages) {
        LinkedList<SimStatuses.Message> temp = new LinkedList<>();
        Collections.addAll(temp, messages);
        logMessages.addAll(0, temp);

        if (logMessages.size() > MAX_MSGS) {
            if (archive == null) archive = new LinkedList<>();
            List<SimStatuses.Message> newlyArchived =cut(logMessages, MAX_MSGS, logMessages.size());
            archive.addAll(0, newlyArchived);
            archiveSet.addAll(newlyArchived);
        }
    }

    private List<SimStatuses.Message> cut(List<SimStatuses.Message> l, int start, int end) {
        List<SimStatuses.Message> l1 = new LinkedList<>();
        for (int i = start; i < end; i++) l1.add(l.remove(start));
        return l1;
    }

    public void clear() {
        logMessages.clear();
        archive = null;
        archiveSet.clear();
    }

}
