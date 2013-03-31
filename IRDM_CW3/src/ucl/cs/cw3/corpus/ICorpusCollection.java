package ucl.cs.cw3.corpus;



public interface ICorpusCollection<KEY,VALUE> {
	public VALUE findValueByKey(KEY key);
	
	
}
