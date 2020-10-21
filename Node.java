import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Node {

	// The list of key values pairs in the node.
	private List<Keys> keys;

	// The children of this node. Set only for internal Nodes
	private List<Node> children;

	// The previous element in the linked list. Set only for external Nodes.
	private Node prev;

	// The next element in the linked list. Set only for external Nodes.
	private Node next;

	// The parent of this node. NULL for root */
	private Node parent;

	public Node() {
		this.keys = new ArrayList<>();
		this.children = new ArrayList<>();
		this.prev = null;
		this.next = null;
	}

	public List<Keys> getKeys() {
		return keys;
	}

	public void setKeys(List<Keys> keys) {
		Iterator<Keys> iter = keys.iterator();
		while (iter.hasNext()) {
			this.keys.add(iter.next());
		}
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public Node getPrev() {
		return prev;
	}

	public void setPrev(Node prev) {
		this.prev = prev;
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "Keys =" + keys.toString();
	}

}
