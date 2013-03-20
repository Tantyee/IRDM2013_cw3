package ucl.cs.cw3.bigramindex;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.log4j.Logger;

import ucl.cs.cw3.io.InvertedIndex;
import ucl.cs.cw3.io.PairOfStringInt;
import ucl.cs.cw3.io.PairOfStrings;



/**
 * @author Tianyi Xiong
 * <b>Part of this class is untested!</b>
 * The Driver of MapReduce to build inverted index.
 * There are two phases before the job finished 
 * Phase1 : Build inverted index for each unique bigram, 
 *          in which termfreq, docfreq and List of (docid.tf) is already set,
 *          but the probability of the bigram are all set to 1.0
 * Phase2<i>(untested)</i> : Calculate and set the probability for each bigram .
 * 			No reducer or combiner for this job.
 */
public class IndexDriver {

	private static final Logger sLogger = Logger.getLogger(IndexDriver.class);
	/**Phase1
	 * @param conf the Configuration for this job
	 * @param isSeq set <code>false</code> to get Text output , otherwise Sequence output
	 * @throws Exception 
	 */
	private static void phase1(Configuration conf,Boolean isSeq) throws Exception{
		Job job = new Job(conf, "BuildInvertedIndex");
		job.setNumReduceTasks(conf.getInt("numReducers",1));
		job.setJarByClass(IndexDriver.class);
		job.setMapperClass(BigramIndexMapper.class);
		job.setReducerClass(BigramIndexReducer.class);
		job.setCombinerClass(BigramIndexCombiner.class);
		job.setMapOutputKeyClass(PairOfStrings.class);
		job.setMapOutputValueClass(PairOfStringInt.class);
		job.setOutputKeyClass(PairOfStrings.class);
		job.setOutputValueClass(InvertedIndex.class);

		
		if (isSeq) {
			job.setOutputFormatClass(SequenceFileOutputFormat.class);
		}

		

		FileInputFormat.addInputPath(job, new Path(conf.get("input")));
		FileOutputFormat.setOutputPath(job, new Path(conf.get("output")));

		Path outputDir = new Path(conf.get("output"));
		FileSystem.get(conf).delete(outputDir, true);

		long startTime = System.currentTimeMillis();
		job.waitForCompletion(true);
		sLogger.info(job.getJobName() + " Finished in "
				+ (System.currentTimeMillis() - startTime) / 1000.0
				+ " seconds");
		
	}
	/**Phase2
	 * @param conf the Configuration for this job
	 * @param isSeq set <code>false</code> to get Text output , otherwise Sequence output
	 * @throws Exception 
	 */
//	private static void phase2(Configuration conf,Boolean isSeq) throws Exception{
//		Job job = new Job(conf, "Calculate Prob");
//		job.setNumReduceTasks(conf.getInt("numReducers",1));
//		job.setJarByClass(IndexDriver.class);
//		job.setMapperClass(BigramProbMapper.class);
//		
//		
//		job.setOutputKeyClass(PairOfStrings.class);
//		job.setOutputValueClass(InvertedIndex.class);
//
//		
//		if (isSeq) {
//			job.setOutputFormatClass(SequenceFileOutputFormat.class);
//		}
//
//		
//
//		FileInputFormat.addInputPath(job, new Path(conf.get("input")));
//		FileOutputFormat.setOutputPath(job, new Path(conf.get("output")));
//
//		Path outputDir = new Path(conf.get("output"));
//		FileSystem.get(conf).delete(outputDir, true);
//
//		long startTime = System.currentTimeMillis();
//		job.waitForCompletion(true);
//		sLogger.info(job.getJobName() + " Finished in "
//				+ (System.currentTimeMillis() - startTime) / 1000.0
//				+ " seconds");
//		
//	}
	public static void main(String[] args) throws Exception {
		
		if(args.length!=3){
			System.out.println("[input] [output] [numberofreudcers]");
			return;
		}
		Configuration p1conf = new Configuration();
		p1conf.set("input", args[0]);
		p1conf.set("output",args[1]);
		p1conf.setInt("numReducers", Integer.parseInt(args[2]));
//		String totalpath = "data/"+String.valueOf(System.currentTimeMillis());
//		p1conf.set("totalpath",totalpath);
		phase1(p1conf,false);
		
//		Configuration p2conf = new Configuration();
//		p2conf.set("input", args[1]+"-temp");
//		p2conf.set("output",args[1]);
//		p2conf.setInt("numReducers", Integer.parseInt(args[2]));
//		p2conf.set("totalpath", totalpath);
//		phase2(p2conf,false);
		
		

	}

}
