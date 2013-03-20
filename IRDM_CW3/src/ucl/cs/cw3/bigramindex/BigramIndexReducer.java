package ucl.cs.cw3.bigramindex;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.mapreduce.Reducer;

import ucl.cs.cw3.io.ArrayListWritable;
import ucl.cs.cw3.io.InvertedIndex;
import ucl.cs.cw3.io.PairOfStringInt;
import ucl.cs.cw3.io.PairOfStrings;

/**
 * @author Tianyi Xiong This is the reducer used for building bigram inverted
 *         index. For each bigram , <code>emit(bigram, invertedindex)</code> and
 *         the List of (docid,tf) is sorted. The count of bigram which start
 *         with word w is stored in an temporary folder in order to calculate
 *         probability of each bigram in anther MapReduce job.
 */
public class BigramIndexReducer extends
		Reducer<PairOfStrings, PairOfStringInt, PairOfStrings, InvertedIndex> {

	private static InvertedIndex invertedindex = new InvertedIndex();

	private static int indexType;

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		indexType = context.getConfiguration().getInt("indextype", -1);

	}

	@Override
	protected void reduce(PairOfStrings key, Iterable<PairOfStringInt> value,
			Context context) throws IOException, InterruptedException {
		int termfreq = 0;
		if (indexType == 1) { // if is a (aaa,*) bigram
			for (PairOfStringInt v : value) {
				termfreq += v.getRightElement();
			}
			invertedindex.setTermfreq(termfreq);
			// don't generate indexlist
			invertedindex.setIndex(null);
			context.write(key, invertedindex);

		} else if(indexType == 0){
			Map<String, Integer> indexitems = new HashMap<String, Integer>();
			for (PairOfStringInt v : value) {
				if (!indexitems.containsKey(v.getLeftElement())) {
					indexitems.put(v.getLeftElement(), v.getRightElement());
				} else {
					int tf = indexitems.get(v.getLeftElement());
					indexitems
							.put(v.getLeftElement(), tf + v.getRightElement());
				}
				termfreq += v.getRightElement();

			}

			invertedindex.setTermfreq(termfreq);

			ArrayListWritable<PairOfStringInt> indexlist = new ArrayListWritable<PairOfStringInt>();
			for (Entry<String, Integer> e : indexitems.entrySet()) {
				indexlist.add(new PairOfStringInt(e.getKey(), e.getValue()));
			}
			// sort
			Collections.sort(indexlist);
			invertedindex.setIndex(indexlist);
			context.write(key, invertedindex);
		}

	}

}
