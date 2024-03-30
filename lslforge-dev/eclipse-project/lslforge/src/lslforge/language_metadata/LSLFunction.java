/**
 *
 */
package lslforge.language_metadata;



public class LSLFunction {
	private String name;
	private LSLParam[] params;
	private String returns;
	private boolean stateless;
	private String description;
	public String getName() { return name; }
	public String getReturns() { return returns; }
	public LSLParam[] getParams() { return params; }
	public String getDescription() { return description; }
	public boolean isStateless() { return stateless; }
	public String fullSignature() {
		return getReturns() + " " + partialSignature(); //$NON-NLS-1$
	}

	public String partialSignature() {
		StringBuilder buf = new StringBuilder(name + "("); //$NON-NLS-1$
		String sep = ""; //$NON-NLS-1$
		for (LSLParam param : params) {
			buf.append(sep).append(param.getType()).append(" "). //$NON-NLS-1$
			  append(param.getName());
			sep = ","; //$NON-NLS-1$
		}
		buf.append(")"); //$NON-NLS-1$
		return buf.toString();
	}

	public String fullDescription() {
		return fullSignature() + "\n\n" + description; //$NON-NLS-1$
	}
}