package ucl.cs.cw3.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import ucl.cs.cw3.corpus.IndexCollection;
import ucl.cs.cw3.corpus.Sentence;
import ucl.cs.cw3.corpus.SentenceCollection;
import ucl.cs.cw3.io.InvertedIndex;
import ucl.cs.cw3.io.PairOfStringInt;
import ucl.cs.cw3.template.Template;

public class Main {

	public static void main(String[] args) throws Exception {
		
		DecimalFormat df = new DecimalFormat("###.0"); //keep 1 decimal digit
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//get the target word
		System.out.println("Enter the word you want to practise");
		String target = br.readLine();
		while (target.contains(" ")){
			System.out.println("Sorry , you can only enter one word.Please enter again :");
			target = br.readLine();
		}
			
		
		
		
		System.out.println("Searching......");
		
		//Create SentenceCollection in order to find sentence
		SentenceCollection sentenceCollection = new SentenceCollection(
				"data/input");
		//Create IndexCollection in order to find index
		IndexCollection indexCollection = new IndexCollection(
				"data/output/bigram");
		
		//look up index of target word in indexCollection 
		InvertedIndex index = indexCollection.findValueByKey(target);
		if (index == null) {
			System.out.println("Sorry, the word \"" + target
					+ "\" is not found in the corpus.");
			System.exit(0);
		}
		System.out.println("Found " + index.getDocfreq()
				+ " sentences including \"" + target
				+ "\" , randomly pick one.");
		//Shaffle the sentence list for ramdom picking 
		List<PairOfStringInt> sentencelist = index.getIndexList();
		Collections.shuffle(sentencelist);
		
		//get sentence by id
		Sentence sentencefound;
		int i = 0;
		do{
			sentencefound = sentenceCollection.findValueByKey(sentencelist
					.get(i++).getLeftElement());
		}while (sentencefound.getLength()>10);
		
		
		//create Template object to start the game
		
		Template t = new Template(sentencefound, "data/output/bigram",
				"data/output/base");
		//print the randomized sentence
		System.out.println("randomized  sentence:");
		System.out.println(t.getGeneratedSentence().getContent());
		
		//User input the sentence 
		String str ;
		System.out.println("Enter your sentence:");
		str = br.readLine();
		t.setInputSentence(new Sentence("", str));
		
		//calculate the perplexity of original sentence , randomized sentence and input sentence respectively
		double massp = t.calPerplexity(t.getGeneratedSentence());
		double inputp = t.calPerplexity(t.getInputSentence());
		double originalp = t.calPerplexity(t.getOriginalSentence());
		
		//calculate the improvement and final score
		double improv = massp / inputp - 1;
		double finalscore = originalp / inputp * 100 + improv * 2;
		
		//score is put into a logistic sigmoid function to get a final score between 0 to 100
		finalscore = 100 / (1 + Math.exp(-0.15 * (finalscore - 50)));
		
		//print the results
		System.out.println("The perplexity of mass sentence is "
				+ df.format(massp));
		System.out.println("The perplexity of your sentence is "
				+ df.format(inputp));
		System.out.println("You've improved " + df.format(improv * 100) + "%");
		System.out.println("The orignal sentence is ["+t.getOriginalSentence().getContent()+"]");

		System.out.println("Your sentence got " + df.format(finalscore));
	}

}
