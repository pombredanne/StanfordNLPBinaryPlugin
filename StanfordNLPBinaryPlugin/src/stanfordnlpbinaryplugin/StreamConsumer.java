package stanfordnlpbinaryplugin;

import java.io.*;

/**
 * Thread that consumes InputStreams to prevent external programs
 * from deadlocking due to buffered output concerns.
 * 
 * @author Peter Dimou
 */
public class StreamConsumer extends Thread {
	InputStream is;
	String type;
	StringWriter sw;

	/**
	 * Builds a StreamConsumer with no redirect.
	 * 
	 * @param is - InputStream to consume
	 * @param type - User-defined type of stream
	 */
	StreamConsumer(InputStream is, String type) {
		this(is, type, null);
	}

	/**
	 * Builds a StreamConsumer with a redirect to a StringWriter to capture
	 * all output.
	 * 
	 * @param is - InputStream to consume
	 * @param type - User-defined type of stream
	 * @param redirect - The StringWriter to send all output to
	 */
	StreamConsumer(InputStream is, String type, StringWriter sw) {
		this.is = is;
		this.type = type;
		this.sw = sw;
	}

	/**
	 * This Thread consumes the InputStream with a buffered reader and then
	 * redirects the output if this object was built with a redirect.
	 */
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				if (sw != null)
					sw.write(line + "\n");
			}
			if (sw != null)
				sw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
