package algo.sched.offline;
// Java program for implementation of Ford Fulkerson algorithm

import java.util.LinkedList;

import exceptions.InvalidFlowNetworkException;
import ds.*;

public class MaxFlow
{
	//static final int graphDim = 6; //Number of vertices in graph

	/* Returns true if there is a path from source 's' to sink
	't' in residual graph. Also fills parent[] to store the
	path */
	private static boolean bfs(int rGraph[][], int source, int sink, int path[], int graphDim)
	{
		// Create a visited array and mark all vertices as not
		// visited
		boolean visited[] = new boolean[graphDim];
		for(int i=0; i<graphDim; ++i)
			visited[i]=false;

		// Create a queue, enqueue source vertex and mark
		// source vertex as visited
		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.add(source);
		visited[source] = true;
		path[source]=-1;

		// Standard BFS Loop
		while (queue.size()!=0)
		{
			int u = queue.poll();

			for (int v=0; v<graphDim; v++)
			{
				if (visited[v]==false && rGraph[u][v] > 0)
				{
					queue.add(v);
					path[v] = u;
					visited[v] = true;
				}
			}
		}

		// If we reached sink in BFS starting from source, then
		// return true, else false
		return (visited[sink] == true);
	}

	// Returns the maximum flow 
	public static Flow fordFulkerson(FlowNetwork fn, int graphDim) throws InvalidFlowNetworkException {
		if (TimeLine.isValidFlowNetwork(fn)) 
			throw new InvalidFlowNetworkException("Impossible to apply flow network algorithm in an invalid flow network!");
		
		int u, v;

		// Create a residual graph and fill the residual graph
		// with given capacities in the original graph as
		// residual capacities in residual graph
		// Residual graph where rGraph[i][j] indicates
		// residual capacity of edge from i to j (if there
		// is an edge. If rGraph[i][j] is 0, then there is
		// not)
		Flow f = new Flow(graphDim);

		for (u = 0; u < graphDim; u++)
			for (v = 0; v < graphDim; v++)
				f.residualGraph[u][v] = fn.residualGraph[u][v];

		// This array is filled by BFS and to store path
		int parent[] = new int[graphDim];

		// Augment the flow while tere is path from source
		// to sink
		while (bfs(f.residualGraph, fn.source, fn.sink, parent, graphDim))
		{
			// Find minimum residual capacity of the edhes
			// aint the path filled by BFS. Or we can say
			// find the maximum flow through the path found.
			int path_flow = Integer.MAX_VALUE;
			for (v = fn.sink; v != fn.source; v=parent[v]) {
				u = parent[v];
				path_flow = (int) Math.min(path_flow, f.residualGraph[u][v]);
			}

			// update residual capacities of the edges and
			// reverse edges aint the path
			for (v = fn.sink; v != fn.source; v=parent[v]) {
				u = parent[v];
				f.residualGraph[u][v] -= path_flow;
				f.residualGraph[v][u] += path_flow;
			}

			// Add path flow to overall flow
			f.flow += path_flow;
		}
	

		// Return the overall flow
		return f;
	}
}
