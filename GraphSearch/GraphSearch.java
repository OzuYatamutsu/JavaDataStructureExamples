import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class GraphSearch {

	/**
	 * Searches the Graph passed in as an AdjcencyList(adjList) to find if a path exists from the start node to the goal node
	 * using General Graph Search.
	 *
	 * Assume the AdjacencyList contains adjacent nodes of each node in the order they should be added to the Structure.
	 *
	 * The structure(struct) passed in is an empty structure may behave as a Stack or Queue and the
	 * correspondingly search function should execute DFS(Stack) or BFS(Queue) on the graph.
	 *
	 * @param start
	 * @param struct
	 * @param adjList
	 * @param goal
	 * @throws IllegalArgumentException if adjList is null
	 * @return true if path exists false otherwise
	 */
	public static <T> boolean generalGraphSearch(T start, Structure<T> struct, Map<T, List<T>> adjList, T goal) {
		if (adjList == null) {
			throw new IllegalArgumentException("adjList cannot be null!");
		}
		
		boolean found = false;
		T current;
		List<T> immediateAdj;
		List<T> visitedList = new LinkedList<T>();
		
		// Add the starting node first
		struct.add(start);
		
		while (!found && !struct.isEmpty()) {
			current = struct.remove();
			if (current.equals(goal)) {
				found = true;
			} else {
				immediateAdj = adjList.get(current);
				for (T adjacency : immediateAdj) {
					// Add all adjacent nodes to struct, except visited
					if (!visitedList.contains(adjacency)) {
						struct.add(adjacency);
					}
				}
				
				// Current node is done, put it in visited list
				visitedList.add(current);
			}
		}
		
		return found;
	}
	
	/**
	 * Searches the Graph passed in as an AdjcencyList(adjList) to find if a path exists from the start node to the goal node
	 * using Bredth First Search.
	 *
	 * Assume the AdjacencyList contains adjacent nodes of each node in the order they should be added to the Structure.
	 *
	 * @param start
	 * @param adjList
	 * @param goal
	 * @return true if path exists false otherwise
	 */
	public static <T> boolean breadthFirstSearch(T start, Map<T, List<T>> adjList, T goal) {
		boolean result = false;
		
		if (adjList.containsKey(start) && adjList.containsKey(goal)) {
			result = generalGraphSearch(start, new StructureQueue<T>(), adjList, goal);
		} 

		return result;
	}
	
	/**
	 * Searches the Graph passed in as an AdjcencyList(adjList) to find if a path exists from the start node to the goal node
	 * using Depth First Search.
	 *
	 * Assume the AdjacencyList contains adjacent nodes of each node in the order they should be added to the Structure.
	 *
	 * @param start
	 * @param adjList
	 * @param goal
	 * @return true if path exists false otherwise
	 */
	public static <T> boolean depthFirstSearch(T start, Map<T, List<T>> adjList, T goal) {
		boolean result = false;
		
		if (adjList.containsKey(start) && adjList.containsKey(goal)) {
			result = generalGraphSearch(start, new StructureStack<T>(), adjList, goal);
		}
		
		return result;
	}
	
	/**
	 * Find the shortest distance between the start node and the goal node in the given a weighted graph
	 * in the form of an adjacency list where the edges only have positive weights
	 * Return the aforementioned shortest distance if there exists a path between the start and goal,-1
	 * otherwise.
	 *
	 * There are no negative edge weights in the graph.
	 *
	 * @param start
	 * @param adjList
	 * @param goal
	 * @throws IllegalArgumentException if adjList is null
	 * @return the shortest distance between the start and the goal node
	 */
	public static <T> int djikstraShortestPathAlgorithm(T start, Map<T, List<Pair<T, Integer>>> adjList, T goal) {
		if (adjList == null) {
			throw new IllegalArgumentException("adjList cannot be null!");
		} else if (adjList.get(start) == null || adjList.get(goal) == null) {
			// Start and end nodes are not accessible in tree
			return -1;
		}
		
		int cost = 0;
		Pair<T, Integer> current;
		Map<T, Integer> nodeMap = new HashMap<T, Integer>();
		for (T node : adjList.keySet()) {
			nodeMap.put(node, Integer.MAX_VALUE);
		}
		
		Comparator<Pair<T, Integer>> nodeOrder = new Comparator<Pair<T, Integer>>() {
			@Override
			public int compare(Pair<T, Integer> node1, Pair<T, Integer> node2) {
				// Should return the lowest-cost node every time
				return node1.b - node2.b;
			}
		};
		
		PriorityQueue<Pair<T, Integer>> nodeQueue = new PriorityQueue<Pair<T, Integer>>(1, nodeOrder);
		
		// Start node's cost is 0
		nodeMap.remove(start);
		nodeMap.put(start, 0);
		nodeQueue.remove(start);
		nodeQueue.add(new Pair<T, Integer>(start, 0));
		
		while (!nodeQueue.isEmpty()) {
			boolean added = false;
			current = nodeQueue.poll();
			cost += current.b;
			for (Pair<T, Integer> adj : adjList.get(current.a)) {
				if (cost + adj.b < nodeMap.get(adj.a)) {
					nodeMap.remove(adj.a);
					nodeMap.put(adj.a, (cost + adj.b));
					nodeQueue.add(adj);
					added = true;
				}
			}
				
			if (!added) {
				// This is a dead end, go back to previous node
				cost -= current.b;
			}
		}
		
		if (nodeMap.get(goal) < Integer.MAX_VALUE) {
			return nodeMap.get(goal);
		} else {
			// Distance is infinity, return not found
			return -1;
		}
	}
}