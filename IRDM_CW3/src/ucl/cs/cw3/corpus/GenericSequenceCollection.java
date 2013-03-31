package ucl.cs.cw3.corpus;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class GenericSequenceCollection {
	private String collectionPath;

	public GenericSequenceCollection(String collectionPath){
		setCollectionPath(collectionPath);
		
	}
	protected FileSystem getFileSystem(){
		Configuration conf = new Configuration();
		if(null!=getCollectionPath()){
			Path path = new Path(collectionPath);
			try {
				return  FileSystem.get(path.toUri(),conf);
				 
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}else{
			System.out.println("collection path is not set.");
			return null;
		}
		
		
		return null;
		
	}
	
	public Path getCollectionPath() {
		return new Path(this.collectionPath);
	}

	public void setCollectionPath(String collectionPath) {
		this.collectionPath = collectionPath;
	}
}
