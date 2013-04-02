package ucl.cs.cw3.corpus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ucl.cs.cw3.io.PairOfStrings;

public class Sentence {
	private String id;
	

	private String content;

	public Sentence(String line) {
		int divider = line.indexOf("\t");
		id = line.substring(0, divider);
		content = line.substring(divider + 1);
	}
	public Sentence (String id, String content){
		setId(id);
		setContent(content);
	}
	

	public Sentence() {
		id = new String();
		content = new String();
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return id + "\t" + content;
	}

	public String getContent() {
		return content;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return number of terms in sentence
	 */
	public int getLength() {

		String terms[] = content.split("[^a-zA-Z0-9']");
		return terms.length;

	}
	
	public String randomTerm() {

		String[] tokens = content.split("[^a-zA-Z0-9']");
		StringBuffer result = new StringBuffer();
		Collections.shuffle(Arrays.asList(tokens));
		for (int i = 0; i < tokens.length; i++) {
			result.append(" ").append(tokens[i]) ;
		}
		return result.toString();
	}
	
	public Iterator<PairOfStrings> getBigram() {
		// Using regular expression to tokenize sentences.
		List<PairOfStrings> bi = new ArrayList<PairOfStrings>();

		String terms[] = content.split("[^a-zA-Z0-9']");
		if (terms.length < 2) {
			return bi.iterator();
		}
		int i = 0;
		while (i < terms.length - 1) {
			if (terms[i].isEmpty()) { // skip the "" tokens
				i++;
				continue;
			} else {
				int j = i + 1;
				while (j < terms.length && terms[j].isEmpty()) {
					// find next unempty  token
					j++; 
				}
				if (j > terms.length - 1) {
					return bi.iterator();
				} else {
					PairOfStrings item = new PairOfStrings();
					item.set(terms[i], terms[j]);
					bi.add(item);
					i = j;

				}
			}

		}
		return bi.iterator();

	}

}
