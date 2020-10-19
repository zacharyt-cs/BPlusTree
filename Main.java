import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class test {

	private int m=3;// degree of the tree
	
	public Node root;// root of BplusTree
	public static  int counter=0;
	
	//constuctor for b plus tree
	public test() {
		
	}
	
	
	public void insert(double key, Records values)
	{
		if(null== this.root)
		{
			// For an empty B Tree, root is created and its key is set as the
			// newly inserted key
			Node newNode = new Node();
			newNode.getKeys().add(new Keys(key, values));
			this.root = newNode;
			// Since the root has no parent, parent set to null
			this.root.setParent(null);
		}
		
		else if(this.root.getChildren().isEmpty() && this.root.getKeys().size()< (this.m -1))
		{
			insertWithinExternalNode(key, values, this.root);
		}
		else
		{
			Node current = this.root;
//			Traverse to the last level since we are inserting at the external node
			while (!current.getChildren().isEmpty()) {
				current = current.getChildren().get(binarySearchWithinInternalNode(key, current.getKeys()));
			}
			insertWithinExternalNode(key, values, current);
			if (current.getKeys().size() == this.m) {
				// If the external node becomes full, we split it
				splitExternalNode(current, this.m);
			}
		}
		
	}
	private void insertWithinExternalNode(double key, Records value, Node node) {
		// A binary search is executed to find the correct place where the node
		// is to be inserted
		int indexOfKey = binarySearchWithinInternalNode(key, node.getKeys());
		if (indexOfKey != 0 && node.getKeys().get(indexOfKey - 1).getKey() == key) {
			//This part handle duplicates
			// Key already exists. Add the new value to the list
			node.getKeys().get(indexOfKey - 1).getValues().add(value);
		} else {
			// Key doesn't exist. Add key and value
			Keys newKey = new Keys(key, value);
			node.getKeys().add(indexOfKey, newKey);
		}
	}
	
	private void splitExternalNode(Node curr, int m) {

		// Find the middle index
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
	
	/**
	 * Merge internal nodes.
	 *
	 * @param mergeFrom
	 *            to part from which we have to merge (middle of the previous
	 *            split node)
	 * @param mergeInto
	 *            the internal node to be merged to
	 */
	private void mergeInternalNodes(Node mergeFrom, Node mergeInto) {
		Keys keyToBeInserted = mergeFrom.getKeys().get(0);
		Node childToBeInserted = mergeFrom.getChildren().get(0);
		// Find the index where the key has to be inserted to by doing a binary
		// search
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

			// If merge is happening at the last element, then only pointer
			// between new node and previously last element
			// needs to be updated
			if (mergeInto.getChildren().size() - 1 != childInsertPos
					&& mergeInto.getChildren().get(childInsertPos + 1).getPrev() == null) {
				mergeInto.getChildren().get(childInsertPos + 1).setPrev(mergeInto.getChildren().get(childInsertPos));
				mergeInto.getChildren().get(childInsertPos).setNext(mergeInto.getChildren().get(childInsertPos + 1));
			}
			// If merge is happening at the last element, then only pointer
			// between new node and previously last element
			// needs to be updated
			else if (0 != childInsertPos && mergeInto.getChildren().get(childInsertPos - 1).getNext() == null) {
				mergeInto.getChildren().get(childInsertPos).setPrev(mergeInto.getChildren().get(childInsertPos - 1));
				mergeInto.getChildren().get(childInsertPos - 1).setNext(mergeInto.getChildren().get(childInsertPos));
			}
			// If merge is happening in between, then the next element and the
			// previous element's prev and next pointers have to be updated
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
		System.out.println("Printing level 1");
		while (!queue.isEmpty()) {
			curr = queue.poll();
			if (null == curr) {
				queue.add(null);
				if (queue.peek() == null) {
					break;
				}
				System.out.println("\n" + "Printing level " + levelNumber++);
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
		System.out.println();
		System.out.println("The height of the tree is " +(levelNumber-1));
		
		return numofNodes;
		
	}
//	print the nodes of the tree
	private void printNode(Node curr) {
	
		for (int i = 0; i < curr.getKeys().size(); i++) {
			System.out.print(curr.getKeys().get(i).getKey() + ":(");
			String values = "";
			for (int j = 0; j < curr.getKeys().get(i).getValues().size(); j++) {
				values = values + curr.getKeys().get(i).getValues().get(j) + ",";
				counter+=1;
			}
			//System.out.print(values.isEmpty() ? ");" : values.substring(0, values.length() - 1) + ");");
		}
		System.out.print("||");
		
//		System.out.println("The number of nodes is "+counter);
	}
//	public void deleteKey(double key)
//	{
////		case 1:there is more than minimum number of keys in the node. simpply delete the keys
//		List<Records> deleteList = 
//		Node curr = this.root;
//		// Traverse to the corresponding external node that would 'should'
//		// contain this key
//		while (curr.getChildren().size() != 0) {
//			curr = curr.getChildren().get(binarySearchWithinInternalNode(key, curr.getKeys()));
//		}
//		
//	}


	/**
	 * Modified Binary search within internal node.
	 *
	 * @param key
	 *            the key to be searched
	 * @param keyList
	 *            the list of keys to be searched
	 * @return the first index of the list at which the key which is greater
	 *         than the input key
	 */
	public int binarySearchWithinInternalNode(double key, List<Keys> keyList) {
		int st = 0;
		int end = keyList.size() - 1;
		int mid;
		int index = -1;
		// Return first index if key is less than the first element
		if (key < keyList.get(st).getKey()) {
			return 0;
		}
		// Return array size + 1 as the new positin of the key if greater than
		// last element
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

	/**
	 * Search values for a key
	 *
	 * @param key
	 *            the key to be searched
	 * @return the list of values for the key
	 */
	public List<Records> search(double key) {
		List<Records> searchValues = null;
		int numNodes=0;
		Node curr = this.root;
		// Traverse to the corresponding external node that would 'should'
		// contain this key
		while (curr.getChildren().size() != 0) {
			curr = curr.getChildren().get(binarySearchWithinInternalNode(key, curr.getKeys()));
		}
		
		List<Keys> keyList = curr.getKeys();
		// Do a linear search in this node for the key. Set the parameter
		// 'searchValues' only if success
		for (int i = 0; i < keyList.size(); i++) {
			if (key == keyList.get(i).getKey()) {
				searchValues = keyList.get(i).getValues();
				numNodes++;
				System.out.println("The node it is accessing is "+ searchValues);
				System.out.println("The number of node it is accessing is "+ numNodes);
			}
			if (key < keyList.get(i).getKey()) {
				break;
			}
		}

		return searchValues;
	}
	

	/**
	 * Search for all key values pairs between key1 and key2.
	 *
	 * @param key1
	 *            the starting key
	 * @param key2
	 *            the ending key
	 * @return the list of key value pairs between the two keys
	 */
	public List<Keys> search(double key1, double key2) {
		//System.out.println("Searching between keys " + key1 + ", " + key2);
		List<Keys> searchKeys = new ArrayList<>();
		Node currNode = this.root;
		// Traverse to the corresponding external node that would 'should'
		// contain starting key (key1)
		while (currNode.getChildren().size() != 0) {
			currNode = currNode.getChildren().get(binarySearchWithinInternalNode(key1, currNode.getKeys()));
		}
		
		// Start from current node and add keys whose value lies between key1 and key2 with their corresponding pairs
		// Stop if end of list is encountered or if value encountered in list is greater than key2
		
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

	
	
	
	public List<Records> retrieveTconstantwithAverageRating(int avgRating)
	{
		List<Records> searchValues = null;
		return searchValues;
	}
	
	public static void main(String[] args) 
	 {
		test tree=new test();		
			
		System.out.println("--------------------Experiment 1 and 2----------------------");
		List<blocks> blo = new ArrayList<blocks>();
		
		ArrayList<String> Data = new ArrayList<>(); //initializing a new ArrayList out of String[]'s
	    try (BufferedReader TSVReader = new BufferedReader(new FileReader("D:\\Year 3 Sem 1\\Database System\\data.tsv")))
	    {
	        String line = null;
	        blocks rblock= new blocks();
		    blo.add(rblock);
		    int i=0;
	        while ((line = TSVReader.readLine()) != null) 
	        {
		        if(i!=0)
		        {
		       		String[] lineItems = line.split("\t"); //splitting the line and adding its items in String[]
			        Records rdata=new Records(lineItems[0],Double.parseDouble(lineItems[1]), Integer.parseInt(lineItems[2]));
			        tree.insert(Double.parseDouble(lineItems[1]), rdata);
				        
			        if(rblock.recordlist.size()==4)
			         {
			            rblock=new blocks();
				    
			            blo.add(rblock);
			           	rblock.recordlist.add(rdata);
			         }
			         else
			         {
			        	 rblock.recordlist.add(rdata);
			         }
				           
	        	}
		        i++;

//		            Data.add(lineItems); //adding the splitted line array to the ArrayList
		        }
//			    tree.printTree();
		        
		  }catch (Exception e)
	    {
		    e.printStackTrace();
		}
//	
//		    one data block is equal to 100
//	    System.out.println("The number of blocks "+ blo.size());
//	    System.out.println("The size of database "+blo.size()*100);
		    

//			int No_of_nodes= tree.printTree();
////			need multply by node size
//			int calculateSize;
////			my degree is 4 so 8 *4 bytes, Since is 8
//			calculateSize= No_of_nodes* 96;
			
			
//			Experiment 3
//		    System.out.println("------------------Experiment 3-----------------");
//			List<Records> tconstValue= tree.search(8);
//			for(int i= 0; i<tconstValue.size();i++)
//			{
//				//limit the max output to be 20 entries due to the huge amount of data
//				if(i < 20)
//				{
//					//printing of tconst value
//					System.out.println(" The t constant value is " + tconstValue.get(i).tconstant);
//				}
//				else 
//				{
//					break;
//				}
//			}
////			Print out the number of blocks and the block data
//			int blockCounter=0;
//			for(int j=0; j<blo.size();j++)
//			{
//				//put the block into the dataBlo arraylist
//				List<Records> dataBlo =	blo.get(j).recordlist;
//				
//				for(int a=0;a<dataBlo.size();a++)
//				{
//					if(dataBlo.get(a).averagerating==8)
//					{
//						blockCounter++;
//						
//						for(int b=0; b<dataBlo.size();b++)
//						{
//									
//							Records r = dataBlo.get(b);
//							System.out.println("Tconst is "+ r.tconstant + " Average Rating is "+ r.averagerating + " Number of votes is "+ r.numofvote);
//							
//						}
//						break;
//
//					}
//				}
//			}
//			
//			System.out.print("Number of blocks: " + blockCounter);
			
			System.out.println("------Experiement 4--------------");
			List<Keys> test= tree.search(7,9); 
			List<String> testing= new ArrayList<String>();
			
			for(int d=0; d<test.size(); d++)
			{
//				Print out 20 records of the tconstant
				System.out.println(test.get(d));
			}
	  }

}

class Records
{
	String tconstant;
	double averagerating;
	int numofvote;
	
	public double getAveragerating() {
		return averagerating;
	}
	public void setAveragerating(double averagerating) {
		this.averagerating = averagerating;
	}
	public int getNumofvote() {
		return numofvote;
	}
	public void setNumofvote(int numofvote) {
		this.numofvote = numofvote;
	}
	public void setTconstant(String tconstant) {
		this.tconstant = tconstant;
	}
	
	
	Records(String constant, double avg, int vote)
	{
		this.tconstant=constant;
		this.averagerating=avg;
		this.numofvote=vote;
	}

}
class blocks
{
	public List<Records> recordlist= new ArrayList<Records>();
	public void addRecord(Records data)
	{
		recordlist.add(data);
	}
	
}
