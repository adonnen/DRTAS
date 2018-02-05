package ds;

public class Flow extends Graph {
	public long flow;
	
	public Flow() {
		super(0);
		flow = 0;
	}
	
	public Flow (int size) {
		super(size);
		flow = 0;
	}
	
	
}
