package ucl.cs.cw3.bigramindex;

import java.io.IOException;
import org.apache.hadoop.mapreduce.Mapper;

import ucl.cs.cw3.io.InvertedIndex;
import ucl.cs.cw3.io.PairOfStrings;

/**
 * @author Tianyi Xiong
 * 
 */
public class BigramProbMapper extends
		Mapper<PairOfStrings, InvertedIndex, PairOfStrings, InvertedIndex> {

	@Override
	protected void map(PairOfStrings key, InvertedIndex value, Context context)
			throws IOException, InterruptedException {

		
		
		context.write(key, value);

	}

	

}
