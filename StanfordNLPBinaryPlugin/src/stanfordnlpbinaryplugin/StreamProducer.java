package stanfordnlpbinaryplugin;

import java.io.*;

/**
 * Thread the writes to a specified output stream. Meant to be used in
 * conjunction with StreamConsumers to send input to programs executed outside
 * of the JVM.
 * 
 * @author Peter Dimou
 */
public class StreamProducer extends Thread {
	OutputStream os;
	String message;

	/**
	 * Constructs the Thread with the output stream to write to and the message
	 * to send during the thread's lifetime.
	 * 
	 * @param os
	 *            The output stream to write to.
	 * @param message
	 *            The string to send to the external program.
	 */
	StreamProducer(OutputStream os, String message) {
		this.os = os;
		this.message = message;
	}

	/**
	 * This Thread writes to the specified output stream through a buffer. Once
	 * the message has finished sending, all output streams are closed.
	 */
	public void run() {
		try {
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(message);
			bw.close();
			osw.close();
			os.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
