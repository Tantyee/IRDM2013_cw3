package ucl.cs.cw3.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import ucl.cs.cw3.io.PairOfStrings;

public class CW3Tools {
	@SuppressWarnings("rawtypes")
	public static Writable findValueByKey(WritableComparable key,
			String seqOutputPath) throws Exception {

		Configuration conf = new Configuration();
		Path path = new Path(seqOutputPath);
		FileSystem fs = FileSystem.get(conf);
		for (FileStatus f : fs.listStatus(path)) {
			if(f.getPath().getName().startsWith(".")){
				continue;
			}
			SequenceFile.Reader reader = new SequenceFile.Reader(
					fs, f.getPath(), conf);
			WritableComparable tempkey = (WritableComparable) reader.getKeyClass()
					.newInstance();
			Writable value = (Writable) reader.getValueClass().newInstance();
			while (reader.next(tempkey)) {
	
				if (tempkey.equals(key)) {
					reader.getCurrentValue(value);
					reader.close();
					return value;
				}
			}

			reader.close();
			
		}
		return null;

//		SequenceFile.Reader reader = new SequenceFile.Reader(
//				FileSystem.get(conf), path, conf);
//		WritableComparable tempkey = (WritableComparable) reader.getKeyClass()
//				.newInstance();
//		Writable value = (Writable) reader.getValueClass().newInstance();
//
//		while (reader.next(tempkey)) {
//			if (tempkey.equals(key)) {
//				reader.getCurrentValue(value);
//				break;
//			}
//		}
//
//		reader.close();
//		return value;

	}

	public static Iterator<PairOfStrings> bigramGenerator(String line) {
		// Using regular expression to tokenize sentences.
		List<PairOfStrings> bi = new ArrayList<PairOfStrings>();

		String terms[] = line.split("[^a-zA-Z0-9']");
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
