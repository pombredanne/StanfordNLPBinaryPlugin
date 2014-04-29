package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import org.junit.Test;

import stanfordnlpbinaryplugin.StanfordCoreNLPWrapper;

/**
 * Unit test suite.
 * 
 * @author Peter Dimou
 */
public class StanfordCoreNLPWrapperTest {

	@Test
	public void testPartOfSpeech() throws IOException {
		// Setup
		File testData = new File("test data/Sample Text.txt");
		File expectedResults = new File(
				"test data/Sample Text (POS Expected Result).txt");
		BufferedReader br = new BufferedReader(new FileReader(expectedResults));
		String currentLine;
		List<Entry<String, String>> expectedResult = new LinkedList<Entry<String, String>>();

		while ((currentLine = br.readLine()) != null) {
			String[] testRecord = currentLine.split("\\t");
			expectedResult.add(new SimpleEntry<>(testRecord[0], testRecord[1]));
		}
		br.close();

		// Execution
		List<Entry<String, String>> retVal = StanfordCoreNLPWrapper
				.runPartOfSpeechAnalysis(testData);

		// Compare results
		assertEquals(expectedResult, retVal);
	}

	@Test
	public void testTokenizer() throws IOException {
		// Setup
		File testData = new File("test data/Sample Text.txt");
		File expectedResults = new File(
				"test data/Sample Text (Tokenizer Expected Result).txt");
		BufferedReader br = new BufferedReader(new FileReader(expectedResults));
		String currentLine;
		ArrayList<String> expectedResult = new ArrayList<String>();
		
		while ((currentLine = br.readLine()) != null) {
			expectedResult.add(currentLine);
		}
		br.close();
		
		// Execution
		String[] retVal = StanfordCoreNLPWrapper.runTokenizer(testData);
		List<String> retValCompare = Arrays.asList(retVal);
		
		
		// Compare results
		assertEquals(expectedResult, retValCompare);
	}
}
