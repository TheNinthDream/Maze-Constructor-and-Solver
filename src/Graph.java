/**
 * Author Abeer Khurram
 * Date: 2024-11-28
 * Graph class, implements the GraphADT using adjacency list
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Graph implements GraphADT {
	private final Map<GraphNode, List<GraphEdge>> adjacencyList;

	/**
	 * Constructor for graph
	 * @param n - number of nodes
	 */
	public Graph(int n) {
		//create a map
		this.adjacencyList = new HashMap<>();

		//in the map, store graph nodes with names from i to n, as keys
		//with an array list of GraphEdge objects as values
		for (int i = 0; i < n; i++) {
			adjacencyList.put(new GraphNode(i), new ArrayList<>());
		}
	}

	/**
	 * inserts edges to both graph edge lists (for u and v)
	 * @param u GraphNode
	 * @param v GraphNode
	 * @param type int
	 * @param label String
	 * @throws GraphException if nodes u, or v not found
	 */
	@Override
	public void insertEdge(GraphNode u, GraphNode v, int type, String label) throws GraphException {
		//check if node exist
		if (!adjacencyList.containsKey(u) || !adjacencyList.containsKey(v)) {
			throw new GraphException("Node u or v DNE");
		}

		//check if edge exist
		//it is sufficient to simply go over one (as inserting edge adds to both)
		//and sufficient to check if one of the endpoints is v, as the other is definitely u
		for (GraphEdge edge : adjacencyList.get(u)) {
			if (edge.firstEndpoint().equals(v) || edge.secondEndpoint().equals(v)) {
				throw new GraphException("Edge already exists");
			}
		}

		//create an edge
		//Insert the edge into the adjacency lists for both
		GraphEdge newEdge = new GraphEdge(u, v, type, label);
		adjacencyList.get(u).add(newEdge);
		adjacencyList.get(v).add(newEdge);
	}

	/**
	 * Gets the node by its name
	 * @param u GraphNode
	 * @return GraphNode
	 * @throws GraphException if node u not found
	 */
	@Override
	public GraphNode getNode(int u) throws GraphException {
		//Use for each loop to iterate over the keys
		for (GraphNode node : adjacencyList.keySet()) {
			//check if any node has name u
			if (node.getName() == u) {return node;}
		}
		throw new GraphException("Node not found");
	}

	/**
	 * Returns an iterator object for iterating through the incident edges of node u
	 * @param u GraphNode
	 * @return iterator
	 * @throws GraphException if node u not found
	 */
	@Override
	public Iterator<GraphEdge> incidentEdges(GraphNode u) throws GraphException {
		//check for node u
		if (!adjacencyList.containsKey(u)) {
			throw new GraphException("Node not found");
		}

		//return the list value, of node u key pair
		//as it is an array list, it has an iterator
		//if the list is empty, return null as per specification
		if (adjacencyList.get(u).isEmpty()) {return null;}
		return adjacencyList.get(u).iterator();
	}

	/**
	 * returns an Edge between nodes u, and v
	 * @param u GraphNode
	 * @param v GraphNode
	 * @return GraphEdge
	 * @throws GraphException if nodes u, or v not found
	 */
	@Override
	public GraphEdge getEdge(GraphNode u, GraphNode v) throws GraphException {
		//check if nodes exists, and if so, which has list of lower size
		boolean uSize = false;
		if (adjacencyList.containsKey(u) && adjacencyList.containsKey(v)) {
			uSize = adjacencyList.get(u).size() < adjacencyList.get(v).size();
		}
		else {throw new GraphException("Node u or v DNE");}

		//based on the bool val (whichever node has list of smaller size), iterate through to find the edge
		//ternary op to avoid duplication, if u is smaller (uSize is true), then target is v and vice versa
		GraphNode sub = uSize ? u : v;
		GraphNode target = uSize ? v : u;
		for (GraphEdge edge : adjacencyList.get(sub)) {
				if (edge.firstEndpoint().equals(target) || edge.secondEndpoint().equals(target)) {return edge;}
			}
		return null;
	}

	/**
	 * uses getEdge method to check for exception, and checking for adjacency
	 * @param u GraphNode
	 * @param v GraphNode
	 * @return boolean
	 * @throws GraphException if nodes u, or v not found
	 */
	@Override
	public boolean areAdjacent(GraphNode u, GraphNode v) throws GraphException {
		//nodes are adjacent if there is an edge connecting them
		GraphEdge edge = getEdge(u, v);

		//if null, returns false, otherwise true
		return edge != null;
	}

}
