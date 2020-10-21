import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BPlusTree {
	// Max number of keys = n
	// Max number of pointers = n+1
	private int n;
	public Node root;// root of BplusTree
	public static int counter=0;
	int heightOfTree=0;
	int numofNodes=0;

	public BPlusTree(int max_child) {
		this.n = max_child;
	}

	public void insert(float key, Records values){
		// If empty tree, create root node
		if(null== this.root){
			Node newNode = new Node();
			newNode.getKeys().add(new Keys(key, values));
			this.root = newNode;
			this.root.setParent(null);
		}
		// If leaf node, insert
		else if(this.root.getChildren().isEmpty() && 
				this.root.getKeys().size() < (this.n -1)){
			insertWithinExternalNode(key, values, this.root);
		}
		//  Else, traverse to leaf node and insert
		//  If node is full, split node
		else{
			Node current = this.root;
			while (!current.getChildren().isEmpty()) {
				current = current.getChildren().get(binarySearchWithinInternalNode(key, current.getKeys()));
			}
			insertWithinExternalNode(key, values, current);
			if (current.getKeys().size() == this.n) {
				splitExternalNode(current, this.n);
			}
		}
		
	}

	private void insertWithinExternalNode(float key, Records value, Node node) {
		int indexOfKey = binarySearchWithinInternalNode(key, node.getKeys());
		// Handle duplicate keys
		if (indexOfKey != 0 && node.getKeys().get(indexOfKey - 1).getKey() == key) {
			node.getKeys().get(indexOfKey - 1).getValues().add(value);
		} 
		else {
			Keys newKey = new Keys(key, value);
			node.getKeys().add(indexOfKey, newKey);
		}
	}
	
	private void splitExternalNode(Node curr, int m) {
		int midIndex = m / 2;
		Node middle = new Node();
		Node rightPart = new Node();

		// Set the right part to have middle element and the elements right to
		// the middle element
		rightPart.setKeys(curr.getKeys().subList(midIndex, curr.getKeys().size()));
		rightPart.setParent(middle);
		
		// While making middle as the internal node, we add only the key since
		// internal nodes of bplus tree do not contain values
		middle.getKeys().add(new Keys(curr.getKeys().get(midIndex).getKey()));
		middle.getChildren().add(rightPart);
		
		// Curr holds the left part, so update the split node to contain just
		// the left part
		curr.getKeys().subList(midIndex, curr.getKeys().size()).clear();

		boolean firstSplit = true;
		// propogate the middle element up the tree and merge with parent of
		// previously overfull node
		splitInternalNode(curr.getParent(), curr, m, middle, firstSplit);

	}
	
	private void splitInternalNode(Node curr, Node prev, int m, Node toBeInserted, boolean firstSplit) {
		if (null == curr) {
			// if we split the root before, then a new root has to be created
			this.root = toBeInserted;
			// we find where the child has to be inserted by doing a binary
			// search on keys
			int indexForPrev = binarySearchWithinInternalNode(prev.getKeys().get(0).getKey(), toBeInserted.getKeys());
			prev.setParent(toBeInserted);
			toBeInserted.getChildren().add(indexForPrev, prev);
			if (firstSplit) {
				// update the linked list only for first split (for external node)
				if (indexForPrev == 0) {
					toBeInserted.getChildren().get(0).setNext(toBeInserted.getChildren().get(1));
					toBeInserted.getChildren().get(1).setPrev(toBeInserted.getChildren().get(0));
				} else {
					toBeInserted.getChildren().get(indexForPrev + 1)
							.setPrev(toBeInserted.getChildren().get(indexForPrev));
					toBeInserted.getChildren().get(indexForPrev - 1)
							.setNext(toBeInserted.getChildren().get(indexForPrev));
				}
			}
		} else {
			// merge the internal node with the mid + right of previous split
			mergeInternalNodes(toBeInserted, curr);
			if (curr.getKeys().size() == m) {
				// do a split again if the internal node becomes full
				int midIndex = (int) Math.ceil(m / 2.0) - 1;
				Node middle = new Node();
				Node rightPart = new Node();

				// since internal nodes follow a split like the b tree, right
				// part contains elements right of the mid element, and the
				// middle becomes parent of right part
				rightPart.setKeys(curr.getKeys().subList(midIndex + 1, curr.getKeys().size()));
				rightPart.setParent(middle);

				middle.getKeys().add(curr.getKeys().get(midIndex));
				middle.getChildren().add(rightPart);

				List<Node> childrenOfCurr = curr.getChildren();
				List<Node> childrenOfRight = new ArrayList<>();

				int lastChildOfLeft = childrenOfCurr.size() - 1;

				// update the children that have to be sent to the right part
				// from the split node
				for (int i = childrenOfCurr.size() - 1; i >= 0; i--) {
					List<Keys> currKeysList = childrenOfCurr.get(i).getKeys();
					if (middle.getKeys().get(0).getKey() <= currKeysList.get(0).getKey()) {
						childrenOfCurr.get(i).setParent(rightPart);
						childrenOfRight.add(0, childrenOfCurr.get(i));
						lastChildOfLeft--;
					} else {
						break;
					}
				}

				rightPart.setChildren(childrenOfRight);

				// update the overfull node to contain just the left part and
				// update its children
				curr.getChildren().subList(lastChildOfLeft + 1, childrenOfCurr.size()).clear();
				curr.getKeys().subList(midIndex, curr.getKeys().size()).clear();

				// propogate split one level up
				splitInternalNode(curr.getParent(), curr, m, middle, false);
			}
		}
	}
	
	private void mergeInternalNodes(Node mergeFrom, Node mergeInto) {
		Keys keyToBeInserted = mergeFrom.getKeys().get(0);
		Node childToBeInserted = mergeFrom.getChildren().get(0);
		// Find the index where the key has to be inserted to by doing a binary search
		int indexToBeInsertedAt = binarySearchWithinInternalNode(keyToBeInserted.getKey(), mergeInto.getKeys());
		int childInsertPos = indexToBeInsertedAt;
		if (keyToBeInserted.getKey() <= childToBeInserted.getKeys().get(0).getKey()) {
			childInsertPos = indexToBeInsertedAt + 1;
		}
		childToBeInserted.setParent(mergeInto);
		mergeInto.getChildren().add(childInsertPos, childToBeInserted);
		mergeInto.getKeys().add(indexToBeInsertedAt, keyToBeInserted);

		// Update Linked List of external nodes
		if (!mergeInto.getChildren().isEmpty() && mergeInto.getChildren().get(0).getChildren().isEmpty()) {

			// If merge is happening at the last element, then only pointer between new node and previously last element needs to be updated
			if (mergeInto.getChildren().size() - 1 != childInsertPos
					&& mergeInto.getChildren().get(childInsertPos + 1).getPrev() == null) {
				mergeInto.getChildren().get(childInsertPos + 1).setPrev(mergeInto.getChildren().get(childInsertPos));
				mergeInto.getChildren().get(childInsertPos).setNext(mergeInto.getChildren().get(childInsertPos + 1));
			}
			// If merge is happening at the last element, then only pointer between new node and previously last element needs to be updated
			else if (0 != childInsertPos && mergeInto.getChildren().get(childInsertPos - 1).getNext() == null) {
				mergeInto.getChildren().get(childInsertPos).setPrev(mergeInto.getChildren().get(childInsertPos - 1));
				mergeInto.getChildren().get(childInsertPos - 1).setNext(mergeInto.getChildren().get(childInsertPos));
			}
			// If merge is happening in between, then the next element and the previous element's prev and next pointers have to be updated
			else {
				mergeInto.getChildren().get(childInsertPos)
						.setNext(mergeInto.getChildren().get(childInsertPos - 1).getNext());
				mergeInto.getChildren().get(childInsertPos).getNext()
						.setPrev(mergeInto.getChildren().get(childInsertPos));
				mergeInto.getChildren().get(childInsertPos - 1).setNext(mergeInto.getChildren().get(childInsertPos));
				mergeInto.getChildren().get(childInsertPos).setPrev(mergeInto.getChildren().get(childInsertPos - 1));
			}
		}
	}
	
	public int printTree() {
		int numofNodes=0;
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(this.root);
		queue.add(null);
		Node curr = null;
		int levelNumber=2;
		System.out.println("LEVEL 1: ");
		while (!queue.isEmpty()) {
			curr = queue.poll();
			if (null == curr) {
				queue.add(null);
				if (queue.peek() == null) {
					break;
				}
				System.out.println("\n" + "LEVEL " + levelNumber++ + ": ");
				continue;
			}
			printNode(curr);
			numofNodes++;
			if (curr.getChildren().isEmpty()) {
				break;
			}
			for (int i = 0; i < curr.getChildren().size(); i++) {
				queue.add(curr.getChildren().get(i));
			}
		}
		curr = curr.getNext();
		while (null != curr) {
			printNode(curr);
			curr = curr.getNext();
		}
		System.out.println("\nEND OF TREE");
		heightOfTree = levelNumber -  1;
		return numofNodes;
	}

	// print the nodes of the tree
	private void printNode(Node curr) {
		for (int i = 0; i < curr.getKeys().size(); i++) {
			System.out.print("|");
			System.out.print(curr.getKeys().get(i).getKey());
			String values = "";
			for (int j = 0; j < curr.getKeys().get(i).getValues().size(); j++) {
				values = values + curr.getKeys().get(i).getValues().get(j) + ",";
				counter+=1;
			}
			//System.out.print(values.isEmpty() ? ");" : values.substring(0, values.length() - 1) + ");");
		}
		System.out.print("| ");
		// System.out.println("The number of nodes is "+counter);
	}

	public int binarySearchWithinInternalNode(float key, List<Keys> keyList) {
		int st = 0;
		int end = keyList.size() - 1;
		int mid;
		int index = -1;
		// Return first index if key is less than the first element
		if (key < keyList.get(st).getKey()) {
			return 0;
		}
		// Return array size + 1 as the new positin of the key if greater thanlast element
		if (key >= keyList.get(end).getKey()) {
			return keyList.size();
		}
		while (st <= end) {
			mid = (st + end) / 2;
			// Following condition ensures that we find a location s.t. key is
			// smaller than element at that index and is greater than or equal
			// to the element at the previous index. This location is where the
			// key would be inserted
			if (key < keyList.get(mid).getKey() && key >= keyList.get(mid - 1).getKey()) {
				index = mid;
				break;
			} // Following conditions follow normal Binary Search
			else if (key >= keyList.get(mid).getKey()) {
				st = mid + 1;
			} else {
				end = mid - 1;
			}
		}
		return index;
	}

	public List<Records> search(float key) {
		List<Records> searchValues = null;
		Node curr = this.root;
		int numOfNodesSearched = 1;
		System.out.println("Printing the nodes accessed:");
		System.out.print("Level "+numOfNodesSearched+" node: |");
		for (Keys k : curr.getKeys()){
			System.out.print(k.key+"|");
		}
		// iterate thru keys in the node to find which key to access its pointer to get next node (child)
		// repeat with the child node until we obtain the leaf node
		while (curr.getChildren().size() != 0) {
			int idx = binarySearchWithinInternalNode(key, curr.getKeys());
			curr = curr.getChildren().get(idx);
			System.out.println("\nAccessing right ptr of the "+idx+"th key of Level "+numOfNodesSearched+"...");
			numOfNodesSearched++;
			System.out.print("Level "+numOfNodesSearched+" node: |");
			for (Keys k : curr.getKeys()){
				System.out.print(k.key+"|");
			}
		}

		// Obtain keys of the leaf node
		List<Keys> keyList = curr.getKeys();
		// If key matches our target key, append its value to Records list
		for (int i = 0; i < keyList.size(); i++) {
			if (key == keyList.get(i).getKey()) {
				searchValues = keyList.get(i).getValues();
			}
			if (key < keyList.get(i).getKey()) {
				break;
			}
		}
		System.out.println("\n\n=> The number of nodes accessed = " + numOfNodesSearched);
		return searchValues;
	}

	public int getNumOfBlocksAccessed(List<Records> searchValues, Disk disk){
		int numBlocksAccessed = 0;
		boolean isBlockAccessed;
		for (Records r : searchValues){
			for (Blocks b : disk.diskBlocks){
				isBlockAccessed = false;
				for (Records records : b.recordlist){
					if (r == records){
						if (!isBlockAccessed) {
							isBlockAccessed = true;
							numBlocksAccessed++;
							break;
						}	
						else{
							break;
						}
					}
				}
			}
		}
		return numBlocksAccessed;
	}
	

	/**
	 * Search for all key values pairs between key1 and key2
	 *
	 * @param key1 - the starting key
	 * @param key2 - the ending key
	 * @return list of key value pairs between the two keys
	 */
	public List<Keys> search(float key1, float key2) {
		//System.out.println("Searching between keys " + key1 + ", " + key2);
		List<Keys> searchKeys = new ArrayList<>();
		Node currNode = this.root;
		int numOfNodesSearched = 1;

		while (currNode.getChildren().size() != 0) {
			System.out.println("Is accessing nodes: "+ currNode.getKeys());
			numOfNodesSearched++;
			currNode = currNode.getChildren().get(binarySearchWithinInternalNode(key1, currNode.getKeys()));
		}
		
		boolean endSearch = false;
		while (null != currNode && !endSearch) {
			for (int i = 0; i < currNode.getKeys().size(); i++) {
				if (currNode.getKeys().get(i).getKey() >= key1 && currNode.getKeys().get(i).getKey() <= key2)
					searchKeys.add((currNode.getKeys().get(i)));
				if (currNode.getKeys().get(i).getKey() > key2) {
					endSearch = true;
				}
			}
			currNode = currNode.getNext();
		}
		return searchKeys;
	}

	// Build a tree from a disk
	public void buildTreeWithFromDisk(Disk disk){
		for (Blocks b : disk.diskBlocks){
			for(Records r : b.recordlist){
				ByteArray br = r.diskRecord.recordArray;
				br.setPos(0);              
				ByteArray header = br.readBytes(new byte[4]);
				String tconst = br.readString();
				float averageRating = br.readFloat();
				int numVotes = br.readInt();
				br.setPos(0);              
				this.insert(averageRating, r);
				this.numofNodes++;
			}
		}
	}
}