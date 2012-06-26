package dperry.aoede.music.factory;

public enum OutputLength {
	MEASURE(1),
	LINE(4),
	HYPERMEASURE(16);
	
	private int measures;
	
	private OutputLength( int measures ) {
		this.measures = measures;
	}
	
	public int getMeasures() {
		return measures;
	}
}
