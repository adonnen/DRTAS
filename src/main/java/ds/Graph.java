package ds;

public abstract class Graph {
	public int [][] residualGraph;
	
	public Graph(int size) {
		residualGraph = new int[size][size];
		for (int i = 0; i < size; ++i)
			for (int j = 0; j < size; ++j)
				residualGraph[i][j] = 0;
	}
}
