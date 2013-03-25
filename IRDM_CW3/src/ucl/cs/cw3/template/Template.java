package ucl.cs.cw3.template;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import ucl.cs.cw3.io.InvertedIndex;
import ucl.cs.cw3.io.PairOfStrings;
import ucl.cs.cw3.utils.CW3Tools;

public class Template {

	private String originalSentence;
	private String generatedSentence;
	private String inputSentence;
	public void setGeneratedSentence(String generatedSentence) {
		this.generatedSentence = generatedSentence;
	}

	private String bigramPath;

	private String basePath;

	public Template(String sentence, String bigramPath, String basePath) {
		setOriginalSentence(sentence);
		setBigramPath(bigramPath);
		setBasePath(basePath);

	}

	public void rndStr() {

		String[] tokens = originalSentence.split("[^a-zA-Z0-9']");
		String result = "";
		Collections.shuffle(Arrays.asList(tokens));
		for (int i = 0; i < tokens.length; i++) {
			result = result + " " + tokens[i].toString();
		}
		this.generatedSentence = result;
	}

	public float getScore() throws Exception {
		if (null == inputSentence || "".equals(inputSentence)) {
			return 0;
		} else {
			Iterator<PairOfStrings> io = CW3Tools
					.bigramGenerator(originalSentence);
			Iterator<PairOfStrings> ig = CW3Tools
					.bigramGenerator(inputSentence);

			return calProbability(ig)/calProbability(io);

		}
	}

	public float calProbability(Iterator<PairOfStrings> i) {
		float originalscore = 1.0f;
		while (i.hasNext()) {
			try {
				PairOfStrings bigram = i.next();
				InvertedIndex temp;
				temp = (InvertedIndex) CW3Tools.findValueByKey(bigram,
						getBigramPath());
				if (!(null == temp)) {
					originalscore = originalscore * temp.getTermfreq();
				}else{
					System.out.println("nullbigram");
				}

				bigram.set(bigram.getLeftElement(), "*");
				temp = (InvertedIndex) CW3Tools.findValueByKey(bigram,
						getBasePath());
				originalscore = originalscore / temp.getTermfreq();
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
		return originalscore;
	}

	public String getBigramPath() {
		return bigramPath;
	}

	public void setBigramPath(String bigramPath) {
		this.bigramPath = bigramPath;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getGeneratedSentence() {
		return generatedSentence;
	}

	public String getOriginalSentence() {
		return originalSentence;
	}

	public void setOriginalSentence(String originalSentence) {
		this.originalSentence = originalSentence;
	}

	public static void main(String[] args) throws Exception {
		Template t = new Template("I believe I have said too much",
				"data/output/bigram", "data/output/base");
		t.rndStr();
		System.out.println("randomized  sentence:");
		System.out.println(t.getGeneratedSentence());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
		String str = null; 
		System.out.println("Enter your sentence:"); 
		str = br.readLine(); 
		t.setInputSentence(str);
		
		
		System.out.println("Your sentence got "+t.getScore()*100+"% mark");
	}

	public String getInputSentence() {
		return inputSentence;
	}

	public void setInputSentence(String inputSentence) {
		this.inputSentence = inputSentence;
	}

}
