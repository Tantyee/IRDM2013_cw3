package ucl.cs.cw3.corpus;

public class Sentence {
	private String id;
	private String content;

	public Sentence(String line) {
		int divider = line.indexOf("\t");
		id = line.substring(0, divider);
		content = line.substring(divider + 1);
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

	/**
	 * @return number of terms in sentence
	 */
	public int getLength() {

		String terms[] = content.split("[^a-zA-Z0-9']");
		return terms.length;

	}

}
