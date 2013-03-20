package ucl.cs.cw3.bigramindex;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import ucl.cs.cw3.io.PairOfStringInt;
import ucl.cs.cw3.io.PairOfStrings;

/**
 * @author Tianyi Xiong
 * This is the mapper used for building inverted index 
 * for each bigram <code>emit(bigram , {docid , 1 })</code> 
 */
public class BigramIndexMapper extends
		Mapper<Object, Text, PairOfStrings,PairOfStringInt> {

	private static PairOfStrings bigram = new PairOfStrings();
	private static PairOfStringInt indexitem = new PairOfStringInt();

	@Override
	protected void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		// Using regular expression to tokenize sentences.
		String terms[] = value.toString().split("[^a-zA-Z0-9']");
		if (terms.length < 2) {
			return;
		}
		int i = 0;
		while (i < terms.length - 1) {
			if (terms[i].isEmpty()) { // skip the "" tokens
				i++;
				continue;
			} else {
				int j = i + 1;
				while (j < terms.length && terms[j].isEmpty()) { // find next
																	// unempty
																	// token
					j++;
				}
				if (j > terms.length - 1) {
					return;
				} else {
					// set bigram key
					bigram.set(terms[i], terms[j]);
					// set docid
					indexitem.set(key.toString(),1);
					// emit the data
					context.write(bigram, indexitem);
					bigram.set(terms[i], "*");
					context.write(bigram, indexitem);
					i = j;
				}
			}

		}

	}
}
