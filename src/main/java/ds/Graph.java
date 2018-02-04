package ds;

public abstract class Graph {
	public int [][] residualGraph;
	
	public Graph(int size) {
		residualGraph = new int[size][size];
	}
}
