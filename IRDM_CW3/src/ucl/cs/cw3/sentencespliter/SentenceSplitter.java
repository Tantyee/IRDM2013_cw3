package ucl.cs.cw3.sentencespliter;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceSplitter {

	public static final int KEY_START = 100000;
	public static final String MODEL_PATH = "spiltmodel/en-sent.bin";

	public static void main(String[] args) throws IOException {

		if (args.length != 2) {
			printUsage();
		}

		System.out.println("input path: " + args[0]);
		System.out.println("output path: " + args[1]);
		System.out.println("sentence detector model path: " + MODEL_PATH);

		SentenceDetector sentenceDetector = null;
		InputStream modelIn = null;
		try {
			// Loading sentence detection model
			modelIn = new BufferedInputStream(new FileInputStream(new File(
					MODEL_PATH)));
			final SentenceModel sentenceModel = new SentenceModel(modelIn);
			modelIn.close();

			sentenceDetector = new SentenceDetectorME(sentenceModel);

		} catch (final IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (final IOException e) {
				} // oh well!
			}
		}

		File dir = new File(args[0].toString());
		int doc = 0;
		int sentenceId = KEY_START;
		for (File child : dir.listFiles()) {
			if (".".equals(child.getName()) || "..".equals(child.getName())
					|| ".DS_Store".equals(child.getName())) {
				continue; // Ignore the self and parent aliases. Also ignore
							// .DS_Store files.
			}

			System.out.println("Splitting " + child.getAbsolutePath() + " ...");

			BufferedReader input = new BufferedReader(new FileReader(child));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = input.readLine()) != null) {
				sb.append(line);
			}

			String[] sentences = sentenceDetector.sentDetect(sb.toString());
			FileWriter fout = new FileWriter(args[1] + doc++ + ".txt");
			for (String sentence : sentences) {
				fout.write(sentenceId++ + "\t ");
				fout.write(sentence);
				fout.write("\r\n");
			}
			fout.close();
			input.close();
		}

	}

	public static void printUsage() {
		System.out
				.println("usage: [input-path, output-path, sentence-detector-model-path]");
		System.exit(-1);
	}
}
