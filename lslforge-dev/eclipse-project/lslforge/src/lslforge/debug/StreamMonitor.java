/**
 *
 */
package lslforge.debug;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;

import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IStreamMonitor;

public class StreamMonitor implements IStreamMonitor {
	private HashSet<IStreamListener> listeners = new HashSet<>();
	private StringBuffer buf = new StringBuffer();
	private Reader reader;

	public StreamMonitor(Reader reader) {
		this.reader = reader;
		t.start();
	}

	@Override
	public void addListener(IStreamListener listener) {
		listeners.add(listener);
	}

	@Override
	public String getContents() {
		synchronized (buf) {
			String s = buf.toString();
			buf.setLength(0);
			return s;
		}
	}

	@Override
	public void removeListener(IStreamListener listener) {
		listeners.remove(listener);
	}

	private Thread t = new Thread() {
		@Override
		public void run() {
			char[] cbuf = new char[512];
			int count = 0;
			try {
				while ((count = reader.read(cbuf)) >= 0) {
					if (count > 0) {
						String s = new String(cbuf, 0, count);
						buf.append(cbuf, 0, count);
						notifyListeners(s);
					}
				}
			} catch (IOException e) {
			}
		}
	};

	private void notifyListeners(String text) {
		for (IStreamListener listener : listeners) {
			listener.streamAppended(text, this);
		}
	}
}