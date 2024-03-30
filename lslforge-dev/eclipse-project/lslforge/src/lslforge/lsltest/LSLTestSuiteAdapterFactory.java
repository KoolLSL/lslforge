package lslforge.lsltest;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;

public class LSLTestSuiteAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		if (!(adaptableObject instanceof LSLTestSuite) ||
		    !(IResource.class.equals(adapterType))) return null;
		return ((LSLTestSuite)adaptableObject).getResource();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Class[] getAdapterList() {
		return new Class[] { IResource.class };
	}

}
