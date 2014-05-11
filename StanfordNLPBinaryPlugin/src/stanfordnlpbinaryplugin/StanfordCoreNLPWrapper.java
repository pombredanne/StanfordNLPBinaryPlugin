package stanfordnlpbinaryplugin;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * Runs the StanfordNLPUtility and obtains results via consuming stdout.
 * 
 * @author Peter Dimou
 */
public class StanfordCoreNLPWrapper {

	/**
	 * Executes the utility to obtain part of speech results.
	 * 
	 * @param file
	 *            The file to analyze.
	 * @return A list of the tokens paired with resulting tags.
	 */
	public static List<Entry<String, String>> runPartOfSpeechAnalysis(File file) {
		// Locate our local plugin resources
		Bundle bundle = Platform.getBundle("StanfordCoreNLPBinaryPlugin");
		URL utilityURL = bundle.getEntry("lib/StanfordCoreNLPUtility.jar");

		File utilityExec = null;
		try {
			utilityExec = new File(FileLocator.resolve(utilityURL).toURI());
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Build the command and run it
		String[] command = new String[] { "java", "-jar",
				utilityExec.getAbsolutePath(), "pos", file.getAbsolutePath() };
		String results = runCommand(command, null);

		// Get rid of empty lines.
		results = results.replaceAll("(?m)^\\s", "");

		// We get TSV values from the wrapper, so split by tab and add to the
		// return value
		List<Entry<String, String>> retVal = new LinkedList<Entry<String, String>>();
		for (String line : results.split("\\r?\\n")) {
			String[] record = line.split("\\t");
			SimpleEntry<String, String> newEntry = new SimpleEntry<>(record[0],
					record[1]);
			retVal.add(newEntry);
		}
		return retVal;
	}

	/**
	 * Executes the utility to obtain tokens.
	 * 
	 * @param file
	 *            The file to tokenize.
	 * @return An array of tokens.
	 */
	public static String[] runTokenizer(File file) {
		// Locate our local plugin resources
		Bundle bundle = Platform.getBundle("StanfordCoreNLPBinaryPlugin");
		URL utilityURL = bundle.getEntry("lib/StanfordCoreNLPUtility.jar");

		File utilityExec = null;
		try {
			utilityExec = new File(FileLocator.resolve(utilityURL).toURI());
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Build the command and run it
		String[] command = new String[] { "java", "-jar",
				utilityExec.getAbsolutePath(), "tokenize",
				file.getAbsolutePath() };
		String results = runCommand(command, null);
		
		return results.split("\\r?\\n");
	}

	/**
	 * Runs the command in a separate process and returns all of the output
	 * (excluding stderr) as a single String.
	 * 
	 * @param command
	 *            The fully built command to execute.
	 * @param input
	 *            A string that should be fed to the external command. Null
	 *            means the command will not receive any input.
	 * @return The results of running the command (stdout).
	 */
	private static String runCommand(String[] command, String input) {
		Runtime rt = Runtime.getRuntime();
		StringWriter sw = new StringWriter();
		Process proc = null;
		try {

			proc = rt.exec(command);

			if (input != null) {
				StreamProducer stdInProducer = new StreamProducer(
						proc.getOutputStream(), input);
				stdInProducer.start();
			}

			StreamConsumer stdOutConsumer = new StreamConsumer(
					proc.getInputStream(), "stdout", sw);

			StreamConsumer stdErrConsumer = new StreamConsumer(
					proc.getErrorStream(), "stderr", null);

			stdOutConsumer.start();
			stdErrConsumer.start();

			int exitVal = proc.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String retVal = sw.toString();
		return retVal;
	}
}
