package lslforge;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.PlatformObject;

/**
 * Represents a resource that is a derived script (i.e. an LSL script
 * that was generated by 'compiling' an LSLForge script).
 *
 * @author rgreayer
 *
 */
public class LSLDerivedScript extends PlatformObject {
	private IFile f;

	public LSLDerivedScript(IFile f) {
		this.f = f;
	}

	public IResource getResource() {
		return f;
	}
}
