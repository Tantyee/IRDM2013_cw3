package ucl.cs.cw3.corpus;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;


public class SentenceCollection extends GenericSequenceCollection implements
		ICorpusCollection<String, Sentence> {

	public SentenceCollection(String collectionPath) {
		super(collectionPath);

	}

	@Override
	public Sentence findValueByKey(String key) {
		
		FileSystem fs = this.getFileSystem();
		try {
			for(FileStatus f : fs.listStatus(getCollectionPath())){
				if(f.getPath().getName().startsWith(".")||f.getPath().getName().startsWith("_")){
					
					continue;
				}
				String line ;
				DataInputStream d = new DataInputStream(fs.open((f.getPath())));
				BufferedReader reader = new BufferedReader(new InputStreamReader(d));
				while ((line = reader.readLine()) != null){
					if(line.startsWith(key+"\t")){
						Sentence s = new Sentence(line);
						return s;
					}
				
				}//end of while
				reader.close();
				  
			}//end of for
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
}
