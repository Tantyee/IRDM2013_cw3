package ucl.cs.cw3.corpus;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.SequenceFile;
import ucl.cs.cw3.io.InvertedIndex;
import ucl.cs.cw3.io.PairOfStrings;

public class IndexCollection extends GenericSequenceCollection implements
		ICorpusCollection<PairOfStrings, InvertedIndex> {

	public IndexCollection(String collectionPath) {
		super(collectionPath);
		
	}

	@Override
	public InvertedIndex findValueByKey(PairOfStrings key) {
		FileSystem fs = this.getFileSystem();
		try {
			for (FileStatus f : fs.listStatus(getCollectionPath())) {
				if(f.getPath().getName().startsWith(".")||f.getPath().getName().startsWith("_")){
					continue;
				}
				SequenceFile.Reader reader = new SequenceFile.Reader(
						fs, f.getPath(), new Configuration());
				
				PairOfStrings tempkey = (PairOfStrings) reader.getKeyClass()
						.newInstance();
				InvertedIndex value = (InvertedIndex) reader.getValueClass().newInstance();
				while (reader.next(tempkey)) {

					if (tempkey.equals(key)) {
						reader.getCurrentValue(value);
						reader.close();
						return value;
					}
				}

				reader.close();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	public InvertedIndex findValueByKey(String key){
		return findValueByKey(new PairOfStrings(key,"*"));
	}
}
