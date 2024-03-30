package lslforge.editor;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import lslforge.gentree.Node;

public class GentreeContentProvider implements ITreeContentProvider {
    private Node root;
    public GentreeContentProvider(Node root) {
        this.root = root;
    }

    @Override
	public Object[] getChildren(Object parentElement) {
        Node node = (Node) parentElement;

        return node.getChildren().toArray();
    }

    @Override
	public Object getParent(Object element) {
        Node node = (Node) element;
        return node.getParent();
    }

    @Override
	public boolean hasChildren(Object element) {
        Node node = (Node) element;
        return node.getChildren().size() > 0;
    }

    @Override
	public Object[] getElements(Object inputElement) {
        return new Node[] { root };
    }

    @Override
	public void dispose() {
    }

    @Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}
