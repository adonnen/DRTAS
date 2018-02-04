package ds;

public class FlowNetwork extends Graph {
	public int sink;
	public int source;
	public int numJobs;
	public int numFrames;

	public FlowNetwork(int size) {
		super(size);
		this.sink = 0;
		this.source = 0;
		this.numJobs = 0;
		this.numFrames = 0;
	}
	
	public FlowNetwork (int sink, int source, int numJobs, int numFrames) {
		super(numJobs + numFrames + 2);
		this.sink = sink;
		this.source = source;
		this.numJobs = numJobs;
		this.numFrames = numFrames;
	}
}
