package ucl.cs.cw3.template;

import java.util.Iterator;
import ucl.cs.cw3.corpus.IndexCollection;
import ucl.cs.cw3.corpus.Sentence;
import ucl.cs.cw3.io.InvertedIndex;
import ucl.cs.cw3.io.PairOfStrings;

public class Template {
	
	private final static float LAMDA = 0.7f;

	private Sentence originalSentence;
	private Sentence generatedSentence;
	private Sentence inputSentence;
	private String bigramPath;
	private String basePath;
	private IndexCollection bigramcollection,basecollection;
	private double distinctbigramNum; // the total number of distinct bigram , used for interpolate smoothing.

	public Template(Sentence sentence, String bigramPath, String basePath) {
		originalSentence = sentence;
		generatedSentence = new Sentence("",sentence.randomTerm());
		setBigramPath(bigramPath);
		setBasePath(basePath);
		bigramcollection = new IndexCollection(bigramPath);
		basecollection = new IndexCollection(basePath);
		distinctbigramNum = basecollection.findValueByKey(new PairOfStrings("*","*")).getTermfreq()*1.0;
	}

	
	

	

	public double calPerplexity(Sentence sentence) {
		if(null==sentence || "".equals(sentence.getContent())){
			System.out.println("empty sentence!");
			return -1.0;
		}
		Iterator<PairOfStrings> i = sentence.getBigram();
		double score = 1.0;
		int count = 0;
		InvertedIndex bigramindex,baseindex;
		while (i.hasNext()) {
				count++;
				PairOfStrings bigram = i.next();
				bigramindex = bigramcollection.findValueByKey(bigram);
				baseindex = basecollection.findValueByKey(bigram.getLeftElement());
				int bigramNum = 0 , baseNum = 0;
				if(null!=bigramindex){
					bigramNum = bigramindex.getTermfreq();
				}
				if(null!=baseindex){
					baseNum = baseindex.getTermfreq();
				}
				//linear interpolate smoothing
				double prob = LAMDA*bigramNum/baseNum+(1-LAMDA)*baseNum/distinctbigramNum;
				score = score/prob;
				
		}
		score = Math.pow(score, 1.0/count);
		
		return score;
	}

	



	
	
	/**
	 * @return the bigramcollection
	 */
	public IndexCollection getBigramcollection() {
		return bigramcollection;
	}





	/**
	 * @return the basecollection
	 */
	public IndexCollection getBasecollection() {
		return basecollection;
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

	

	public Sentence getOriginalSentence() {
		return originalSentence;
	}



	public void setOriginalSentence(Sentence originalSentence) {
		this.originalSentence = originalSentence;
	}



	public Sentence getGeneratedSentence() {
		return generatedSentence;
	}



	public void setGeneratedSentence(Sentence generatedSentence) {
		this.generatedSentence = generatedSentence;
	}



	public Sentence getInputSentence() {
		return inputSentence;
	}



	public void setInputSentence(Sentence inputSentence) {
		this.inputSentence = inputSentence;
	}

	

}
