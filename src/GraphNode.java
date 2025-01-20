/**
 * Author Abeer Khurram
 * Date: 2024-11-28
 * Node class for Graph ADT
 */

public class GraphNode {
	private final int name;
	private boolean mark;

	/**
	 * Constructor
	 * @param name
	 */
	public GraphNode(int name) {
		this.name = name;
	}

	//Accessor and Mutator methods
	public void mark(boolean mark) {
		this.mark = mark;
	}
	
	public boolean isMarked() {
		return mark;
	}
	
	public int getName() {
		return name;
	}
	
}
