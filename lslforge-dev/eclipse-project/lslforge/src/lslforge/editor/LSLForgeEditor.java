package lslforge.editor;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import lslforge.LSLForgePlugin;
import lslforge.LSLProjectNature;
import lslforge.debug.LSLLineBreakpoint;
import lslforge.generated.ErrInfo;
import lslforge.generated.ErrInfo_ErrInfo;
import lslforge.generated.Maybe_Just;
import lslforge.generated.TextLocation;
import lslforge.generated.TextLocation_TextLocation;
import lslforge.outline.LSLForgeOutlinePage;
import lslforge.util.LSLColorProvider;
import lslforge.util.Log;
import lslforge.util.Util;

/**
 * LSLForge text editor.
 */
public class LSLForgeEditor extends TextEditor implements SourceViewerConfigurationListener, LSLProjectNature.RecompileListener {
	@Deprecated
	@Override
	public void gotoMarker(IMarker marker) {
    	IResource markerFile = marker.getResource();
    	IResource editorFile = getEditorInput().getAdapter(IResource.class);
    	if(markerFile.equals(editorFile)) {
    		super.gotoMarker(marker);
    		if(editorFile instanceof IFile)
    			if(parentEditor != null)
    				parentEditor.setActiveByFile((IFile)editorFile);

    	} else {
    		//Try to pass it to the multipage editor to sort out
    		if(parentEditor != null)
    			parentEditor.gotoMarker(marker);
    	}
	}

	public static final String ID = "lslforge.editor.LSLForgeEditor"; //$NON-NLS-1$

    /** The projection support */
    private ProjectionSupport fProjectionSupport;

    private LSLMultiPageEditor parentEditor = null;
    private LSLForgeOutlinePage outlinePage = null;

    private boolean forceReadOnly = false;


    /**
     * Create an instance of the editor.
     */
    public LSLForgeEditor() {
        super();
    }
    
    //Kool added 06/04/2024
    public final static String EDITOR_MATCHING_BRACKETS = "matchingBrackets";
    public final static String EDITOR_MATCHING_BRACKETS_COLOR= "matchingBracketsColor";
    
    @Override
    protected void configureSourceViewerDecorationSupport (SourceViewerDecorationSupport support) {
    	super.configureSourceViewerDecorationSupport(support);		
    	
    	char[] matchChars = {'(', ')', '{', '}', '[', ']'}; //which brackets to match		
    	ICharacterPairMatcher matcher = (ICharacterPairMatcher) new DefaultCharacterPairMatcher(matchChars ,
    			IDocumentExtension3.DEFAULT_PARTITIONING);
    	support.setCharacterPairMatcher((org.eclipse.jface.text.source.ICharacterPairMatcher) matcher);    	
    	//We copy our plugin store value to the general store value. Not ideal but is there another way ? 
    	Color col = LSLForgePlugin.getDefault().getLSLColorProvider().getColor(LSLColorProvider.BRACKET_COLOR);
    	String colStr =  new String(col.getRed() + ", " + col.getGreen() + ", " + col.getBlue());
    	//Ex: String colStr = "0,255,0"; 
    	//Enable bracket highlighting in the preference store
    	IPreferenceStore store = getPreferenceStore();
    	store.setDefault(EDITOR_MATCHING_BRACKETS, true);
    	store.setDefault(EDITOR_MATCHING_BRACKETS_COLOR, colStr);	
    	support.setMatchingCharacterPainterPreferenceKeys(EDITOR_MATCHING_BRACKETS, EDITOR_MATCHING_BRACKETS_COLOR
    				, EDITOR_MATCHING_BRACKETS, EDITOR_MATCHING_BRACKETS);    	   	
    }          
    //endof Kool
    
    @Override
	public boolean isEditable() {
    	if(forceReadOnly) return false;
		return super.isEditable();
	}

    public void setReadOnly() {
    	forceReadOnly = true;
    }

    public void setParent(LSLMultiPageEditor parent) {
    	this.parentEditor = parent;
    }
	/**
     * The <code>LSLForgeEditor</code> implementation of this
     * <code>AbstractTextEditor</code> method extend the actions to add those
     * specific to the receiver
     */
    @Override
	protected void createActions() {
        super.createActions();

        IAction a = new TextOperationAction(Messages.getResourceBundle(),
                "ContentAssistProposal.", this, ISourceViewer.CONTENTASSIST_PROPOSALS); //$NON-NLS-1$
        a.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
        setAction("ContentAssistProposal", a); //$NON-NLS-1$

        a = new TextOperationAction(Messages.getResourceBundle(),
                "ContentAssistTip.", this, ISourceViewer.CONTENTASSIST_CONTEXT_INFORMATION); //$NON-NLS-1$
        a.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_CONTEXT_INFORMATION);
        setAction("ContentAssistTip", a); //$NON-NLS-1$

    }

    /**
     * Save the contents of the editor.
     *
     * @param monitor the progress monitor
     */
    @Override
	public void doSave(IProgressMonitor monitor) {
        setCharSet();
        super.doSave(monitor);
    }

	/**
     * doSaveAs specialization of the AbstractTextEditor's doSaveAs()...
     */
    @Override
	public void doSaveAs() {
        setCharSet();
        super.doSaveAs();
    }

    private void setCharSet() {
        IFile f = getEditorInput().getAdapter(IFile.class);
        try {
            f.setCharset("UTF-8", null); //$NON-NLS-1$
        } catch (CoreException e) {
            Log.error("can't set charset",e); //$NON-NLS-1$
        }
    }
    @Override
	protected void editorContextMenuAboutToShow(IMenuManager menu) {
        super.editorContextMenuAboutToShow(menu);
        addAction(menu, "ContentAssistProposal"); //$NON-NLS-1$
        addAction(menu, "ContentAssistTip"); //$NON-NLS-1$
        addAction(menu, "DefineFoldingRegion"); //$NON-NLS-1$
    }

    /**
     * Adapt this editor to the required type, if possible.
     * @param required the required type
     * @return an adapter for the required type or <code>null</code>
     */
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class required) {
        if (IContentOutlinePage.class.equals(required)) {
            return getOutlinePage();
        }

        if (fProjectionSupport != null) {
            Object adapter = fProjectionSupport.getAdapter(getSourceViewer(), required);
            if (adapter != null)
                return adapter;
        }

        return super.getAdapter(required);
    }

    public IContentOutlinePage getOutlinePage() {
        if (outlinePage == null) {
        	IEditorInput input = this.getEditorInput();
        	if(input instanceof IFileEditorInput) {
        		IFileEditorInput feInput = (IFileEditorInput)input;
        		try {
					if(LSLProjectNature.hasProjectNature(feInput.getFile().getProject())) {
						outlinePage = new LSLForgeOutlinePage(this);
					}
				} catch (CoreException e) { }	//Do nothing and let the outline page stay null
        	}
        }

    	return outlinePage;
    }

	@Override
	protected void initializeEditor() {
        super.initializeEditor();

        LSLSourceViewerConfiguration config = new LSLSourceViewerConfiguration(this);
        setSourceViewerConfiguration(config);
        outlinePage = new LSLForgeOutlinePage(this);
        config.addListener(this);
    }

    private LSLProjectNature nature() {
        IResource resource = getEditorInput().getAdapter(IResource.class);
        if (resource != null) {
        	try {
				if (resource.getProject().isOpen()) {
        		return (LSLProjectNature) resource.getProject().getNature(LSLProjectNature.ID);
				}
			} catch (CoreException e) {
				Log.error("can't get project nature", e); //$NON-NLS-1$
			}
        }
        return null;
    }

    @Override
	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
        fAnnotationAccess = createAnnotationAccess();
        fOverviewRuler = createOverviewRuler(getSharedColors());

        ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(),
                isOverviewRulerVisible(), styles);

        // ensure decoration support has been created and configured.
        getSourceViewerDecorationSupport(viewer);

        return viewer;
    }

    @Override
	public void dispose() {
        LSLProjectNature n = nature();
        if (n != null) n.removeRecompileListener(this);
        ((LSLSourceViewerConfiguration)this.getSourceViewerConfiguration()).dispose();
        super.dispose();
    }

    @Override
	public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();
        fProjectionSupport = new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
        fProjectionSupport
                .addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.error"); //$NON-NLS-1$
        fProjectionSupport
                .addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.warning"); //$NON-NLS-1$
        fProjectionSupport.install();
        viewer.doOperation(ProjectionViewer.TOGGLE);


        if(forceReadOnly)
        	getSourceViewer().getTextWidget().setBackground(new Color(getSourceViewer().getTextWidget().getDisplay(), 230, 230, 230));

        LSLProjectNature n = nature();
        if (n != null) n.addRecompileListener(this);

        this.getVerticalRuler().getControl().addMouseListener(new MouseListener() {

            @Override
			public void mouseDoubleClick(MouseEvent e) {
                IResource resource = getEditorInput().getAdapter(IResource.class);

                if (resource != null) {
                    Integer line = Integer.valueOf(getVerticalRuler().toDocumentLineNumber(e.y) + 1);
                    try {
                        IMarker m = null;
                        IMarker[] markers = resource.findMarkers(LSLLineBreakpoint.MARKER_ID, true, 0);
                        for (IMarker marker : markers) {
                            if (line.equals(marker.getAttribute(IMarker.LINE_NUMBER))) {
                                m = marker;
                            }
                        }
                        if (m == null) {
                            new LSLLineBreakpoint(resource,line.intValue());
                        } else {
                            IBreakpoint bp = breakpointManager().getBreakpoint(m);
                            breakpointManager().removeBreakpoint(bp, true);
                        }
                    } catch (CoreException e1) {
                        Log.error(e1);
                    }

                } else {
                    Log.debug("resource is null, can't create breakpoint"); //$NON-NLS-1$
                }
            }

            private IBreakpointManager breakpointManager() {
                return DebugPlugin.getDefault().getBreakpointManager();
            }

            @Override
			public void mouseDown(MouseEvent e) {
            }

            @Override
			public void mouseUp(MouseEvent e) {
            }

        });
    }

    public void clearProjections() {
        ProjectionAnnotationModel pm = getProjectionModel();
        if (pm==null) return;
        pm.removeAllAnnotations();
    }
    public void addProjection(int start, int length) {
        ProjectionAnnotationModel pm = getProjectionModel();
        if (pm==null) return;
        pm.addAnnotation(new ProjectionAnnotation(), new Position(start, length));
    }

    private ProjectionAnnotationModel getProjectionModel() {
    	ISourceViewer sourceViewer = getSourceViewer();
    	if (sourceViewer == null) return null;
        return ((ProjectionViewer)sourceViewer).getProjectionAnnotationModel();
    }

    public void annotateErrs(List<ErrInfo> errs) {
    	if (errs == null) return;
    	ISourceViewer sourceViewer = getSourceViewer();
    	if (sourceViewer == null) return;
        IAnnotationModel am = sourceViewer.getAnnotationModel();
        //if (am == null) return;

        Iterator<?> ai = am.getAnnotationIterator();
        while (ai.hasNext()) {
            Annotation ann = (Annotation) ai.next();
            if (ann.getType().equals("org.eclipse.ui.workbench.texteditor.error")) //$NON-NLS-1$
                am.removeAnnotation(ann);
        }


        for (ErrInfo err : errs) {
            ErrInfo_ErrInfo e = (ErrInfo_ErrInfo) err;
            if (e.el1 instanceof Maybe_Just) {
                Maybe_Just<TextLocation> mt = (Maybe_Just<TextLocation>) e.el1;
                TextLocation_TextLocation t = (TextLocation_TextLocation) mt.el1;
                int offs[] = Util.findOffsetsFor(
                        new int[] { t.textLine0 - 1, t.textLine1 - 1},
                        new int[] { t.textColumn0 - 1, t.textColumn1 - 1},
                        getDocumentProvider().getDocument(getEditorInput()).get());
                if (offs[0] == offs[1]) offs[1]++;
                Position pos = new Position(offs[0], offs[1] - offs[0]);
                Annotation ann = new Annotation(
                        "org.eclipse.ui.workbench.texteditor.error", true, e.el2); //$NON-NLS-1$
                am.addAnnotation(ann, pos);
            }
        }

    }

    @Override
	protected void adjustHighlightRange(int offset, int length) {
        ISourceViewer viewer = getSourceViewer();
        if (viewer instanceof ITextViewerExtension5) {
            ITextViewerExtension5 extension = (ITextViewerExtension5) viewer;
            extension.exposeModelRange(new Region(offset, length));
        }
    }

    @Override
	public void configurationChanged() {
    	ISourceViewer sourceViewer = this.getSourceViewer();
    	if (sourceViewer == null) return;
    	sourceViewer.invalidateTextPresentation();
    }

    public void updateOutline() {
    	if(outlinePage != null) {
    		asyncExec(new Runnable() {
    			@Override
				public void run() {
					outlinePage.update();
    			}
    		});
    	}
    }

    private void asyncExec(Runnable r) {
        PlatformUI.getWorkbench().getDisplay().asyncExec(r);
    }

	@Override
	public void recompile() {
		updateOutline();
	}

	@Override
	protected void rememberSelection() {
		if(parentEditor != null) parentEditor.rememberSelection();
		super.rememberSelection();
	}

	@Override
	protected void restoreSelection() {
		super.restoreSelection();
		if(parentEditor != null) parentEditor.restoreSelection();
	}

}
