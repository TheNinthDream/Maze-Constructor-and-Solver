/**
 * Author: Abeer Khurram
 * date: 2024-11-28
 * Maze class, constructs a maze from a text file, and is dependent on graph class
 */

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;


public class Maze {
	Graph labyrinth;
	GraphNode startNode;
	GraphNode endNode;
	int coins;

	/**
	 * Constructor, delegates to readInput method, mainly handles exceptions
	 * @param inputFile text file
	 * @throws MazeException custom exception
	 */
	public Maze(String inputFile) throws MazeException {
		try (BufferedReader inputReader = new BufferedReader(new FileReader(inputFile))) {
        readInput(inputReader); //main processing and parsing in readInput method

		} catch (FileNotFoundException e) {
        throw new MazeException("The input file was not found.");
    	} catch (IOException e) {
        throw new MazeException("Error while reading the input file");
    	} catch (GraphException e) {
        throw new MazeException("Error while constructing the graph from the input file.");
    	}
	}

	/**
	 * accessor method
	 * @return Graph
	 */
	public Graph getGraph() {
		return labyrinth;
	}

	public Iterator<GraphNode> solve() {
		List<GraphNode> path = new ArrayList<>();
		try{
			if (DFS(this.coins, this.startNode, path)) {
				return path.iterator();
			}
		} catch (GraphException e) {
            throw new RuntimeException(e);
        }
		return null;
    }

	/**
	 * DFS method using recursion
	 * Tried using Stack but got stuck with infinite loops
	 * @param k coins
	 * @param go current node, at start -> startNode
	 * @param path iterator
	 * @return boolean, True if path found, false otherwise
	 * @throws GraphException custom exception
	 */
	private boolean DFS(int k, GraphNode go, List<GraphNode> path) throws GraphException {
		//Add current node to the path, and mark it as exploring
		path.add(go);
		go.mark(true);

		//if current node is end-node, return true
		if (go.getName() == endNode.getName()) {
			this.coins -= k;//tryna update the coins, failing miserably }-(
			return true;
		}

		//create an iterator to iterate through all the incident edges of current node
		Iterator<GraphEdge> edges = labyrinth.incidentEdges(go);
		while (edges.hasNext()) {
			GraphEdge edge = edges.next();
			GraphNode next;

			//next node to be explored is the node incident to node go, on next edge
			if (edge.firstEndpoint().equals(go)) {next = edge.secondEndpoint();}
			else {next = edge.firstEndpoint();}

			//skip nodes that have been marked, or that have a cost that is not affordable
			//if this node has already been marked, then it's already been explored
			if (next.isMarked()) {continue;}

			//otherwise get the cost to traverse through this door
			int price = edge.getType();
        	if (!(k < price)) {
				//as long as the coins are greater than or equal to it,
				//traverse through with subtracting the coins
            	if (DFS(k - price, next, path)) {return true;}
					//if the path through this node led to the end node
			}
		}

		//backtrace to previous state as the node didn't lead to the path
		path.remove(path.size() - 1);
		go.mark(false); //unmark the node as finished exploration
		return false;
	}

	/**
	 * Main method for parsing and constructing maze
	 * @param inputReader bufferedReader
	 * @throws IOException if file problems
	 * @throws GraphException custom exception
	 */
	private void readInput(BufferedReader inputReader) throws IOException, GraphException {

		//parse the first four lines, s, a, l, k, for scale, width, length and coins respectively
		int s = Integer.parseInt(inputReader.readLine().trim());
    	int a = Integer.parseInt(inputReader.readLine().trim());
    	int l = Integer.parseInt(inputReader.readLine().trim());
    	this.coins = Integer.parseInt(inputReader.readLine().trim());

		this.labyrinth = new Graph(a * l);//initialize graph by nodes given by w*l

		//read maze
    	String[] mazeGrid = new String[l + l - 1]; //vertical and horizontal links
    	for (int i = 0; i < mazeGrid.length; i++) {
			mazeGrid[i] = inputReader.readLine().trim();
			System.out.println(mazeGrid[i]);
		}
		//maze grid stores each line separately, even lines are R+H, odd ones are V+W

		int nodeName = 0;
		for (int i = 0; i < l; i++) {

			String evenRow = mazeGrid[i * 2];//row at even indices are R + H (that is room + c/d/w)
			String oddRow = (i * 2 + 1 < mazeGrid.length) ? mazeGrid[i * 2 + 1] : null;//row at indices odd are V + W (that is d/c + w)

			for (int j = 0; j < a; j++) {
				char r = evenRow.charAt(j * 2);//In even rows, rooms are at even indices

				//find start and end nodes
				if (r == 's') {this.startNode = labyrinth.getNode(nodeName);}
				else if (r == 'x') {this.endNode = labyrinth.getNode(nodeName);}

				if (j < a - 1 && j * 2 + 1 < evenRow.length()) { //if not the last room
            		char link = evenRow.charAt(j * 2 + 1); //get the odd index (for H)
					if (link == 'w') {
						//increment nodeName manually as it skips otherwise
						nodeName++;
						continue;
					}
					insertEdge(nodeName, nodeName + 1, link); //create an edge between H and R
				}
				nodeName++; //after processing the current room
			}

			if (oddRow != null) {
				for (int j = 0; j < a; j++) {
					if (j * 2 < oddRow.length()) {
						char link = oddRow.charAt(j * 2); //those at even indices in even rows are walls that don't need to be processed
						if (link == 'w') {
							continue;
						}
						insertEdge(nodeName - a + j, nodeName + j, link); //create an edge between R and V
						//nodeName - a because I'm accessing the row behind me
						//nodeName + j, refs the current node by column
					}
				}
			}


		}
	}

	/**
	 * Helper method to insert edges
	 * @param node1 u
	 * @param node2 v
	 * @param label door val/corridor val
	 * @throws GraphException custom exception
	 */
	private void insertEdge(int node1, int node2, char label) throws GraphException {
		//get graph nodes object by name
		GraphNode u = labyrinth.getNode(node1);
		GraphNode v = labyrinth.getNode(node2);

		//default set to string to corridor, with weight 0
		String sub = "c";
		int weight = 0;

		//if digit, then set string to "d" and weight with numeric val of label
		if (Character.isDigit(label)) {
			sub = "d";
			weight = Integer.parseInt(String.valueOf(label));
		}

		//insert into the graph
		labyrinth.insertEdge(u, v, weight, sub);
	}

}
