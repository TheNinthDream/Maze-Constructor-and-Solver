/**
 * Author Abeer Khurram
 * Date: 2024-11-28
 * Edge class for Graph ADT
 */

public class GraphEdge {
	private final GraphNode node1;
	private final GraphNode node2;
	private int weight;
	private String label;

	/**
	 * Constructor, initializes all the instance variables
	 * @param u GraphNode
	 * @param v GraphNode
	 * @param type int
	 * @param label String
	 */
	public GraphEdge(GraphNode u, GraphNode v, int type, String label) {
		this.node1 = u;
		this.node2 = v;
		this.weight = type;
		this.label = label;
	}

	//Accessor and Mutator basic methods
	public GraphNode firstEndpoint() {
		return this.node1;
	}
	
	public GraphNode secondEndpoint() {
		return this.node2;
	}
	
	public int getType() {
		return this.weight;
	}
	
	public void setType(int type) {
		this.weight = type;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
}
