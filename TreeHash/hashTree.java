// hashTree.java
// demonstrates hash table with separate chaining
// to run this program: C:>java HashTreeApp
import java.io.*;

////////////////////////////////////////////////////////////////
class Node
{
	public int iData; // data item (key)
	public Node leftChild; // this node's left child
	public Node rightChild; // this node's right child
	
	public Node(int key) { // constructor
		iData = key;
	}
} // end class Node
////////////////////////////////////////////////////////////////
class Tree
{
	private Node root; // first node of tree
	// -------------------------------------------------------------
	public Tree() // constructor
	{ root = null; } // no nodes in tree yet
	//-------------------------------------------------------------
	public Node find(int key) // find node with given key. can handle empty tree
	{
		if (root == null) // tree is empty
			return null;
		Node current = root; // start at root
		while(current.iData != key) // while no match,
		{
			if(key < current.iData) // go left?
				current = current.leftChild;
			else // or go right?
				current = current.rightChild;
			if(current == null) // if no child,
				return null; // didn't find it
		}
		return current; // found it
	} // end find()
	// -------------------------------------------------------------
	public void insert(int id) // insert Node with given key. ignores duplicates
	{
		Node newNode = new Node(id); // make new node
		if(root==null) // no node in root
			root = newNode;
		else // root occupied
		{
			Node current = root; // start at root
			Node parent;
			while(true) // (exits internally)
			{
				parent = current;
				if(id < current.iData) // go left?
				{
					current = current.leftChild;
					if(current == null) // if end of the line,
					{ // insert on left
						parent.leftChild = newNode;
						return;
					}
				} // end if go left
				else if(id > current.iData) // or go right?
				{
					current = current.rightChild;
					if(current == null) // if end of the line
					{ // insert on right
						parent.rightChild = newNode;
						return;
					}
				} // end else go right
				else // duplicate key, ignore
					return;
			} // end while
		} // end else not root
	} // end insert()
	// -------------------------------------------------------------
	private void inOrder(Node localRoot)
	{
		if(localRoot != null)
		{
			inOrder(localRoot.leftChild);
			System.out.print(localRoot.iData + " ");
			inOrder(localRoot.rightChild);
		}
	}
	// -------------------------------------------------------------
	public void displayTree()
	{
		inOrder(root);
	} // end displayTree()
	// -------------------------------------------------------------
} // end class Tree
// //////////////////////////////////////////////////////////////

class HashTable {
	private Tree[] hashArray; // array of trees
	private int arraySize;

	// -------------------------------------------------------------
	public HashTable(int size) // constructor
	{
		arraySize = size;
		hashArray = new Tree[arraySize]; // create array
		for (int j = 0; j < arraySize; j++)
			// fill array
			hashArray[j] = new Tree(); // with trees
	}

	// -------------------------------------------------------------
	public void displayTable() {
		for (int j = 0; j < arraySize; j++) // for each cell,
		{
			System.out.print(j + ". "); // display cell number
			hashArray[j].displayTree(); // display tree
			System.out.println();
		}
	}

	// -------------------------------------------------------------
	public int hashFunc(int key) // hash function
	{
		return key % arraySize;
	}

	// -------------------------------------------------------------
	public void insert(int key) // insert a Node
	{
		int hashVal = hashFunc(key); // hash the key
		hashArray[hashVal].insert(key); // insert at hashVal
	} // end insert()
		// -------------------------------------------------------------

	public Node find(int key) // find Node
	{
		int hashVal = hashFunc(key); // hash the key
		Node theNode = hashArray[hashVal].find(key); // get Node
		return theNode; // return Node
	}
	// -------------------------------------------------------------
} // end class HashTable
// //////////////////////////////////////////////////////////////

class HashTreeApp {
	public static void main(String[] args) throws IOException
	{
		int aKey;
		Node aDataItem;
		int size, n, keysPerCell = 100;
		// get sizes
		System.out.print("Enter size of hash table: ");
		size = getInt();
		System.out.print("Enter initial number of items: ");
		n = getInt();
		// make table
		HashTable theHashTable = new HashTable(size);
		for(int j=0; j<n; j++) // insert data
		{
			aKey = (int)(java.lang.Math.random() *
					keysPerCell * size);
			theHashTable.insert(aKey);
		}
		while(true) // interact with user
		{
			System.out.print("Enter first letter of ");
			System.out.print("show, find, insert, or quit: ");
			char choice = getChar();
			switch(choice)
			{
			case 's':
				theHashTable.displayTable();
				break;
			case 'i':
				System.out.print("Enter key value to insert: ");
				aKey = getInt();
				theHashTable.insert(aKey);
				break;
			case 'f':
				System.out.print("Enter key value to find: ");
				aKey = getInt();
				aDataItem = theHashTable.find(aKey);
				if(aDataItem != null)
					System.out.println("Found " + aKey);
				else
					System.out.println("Could not find " + aKey);
				break;
			case 'q':
				return;
			default:
				System.out.print("Invalid entry\n");
			} // end switch
		} // end while
	} // end main()

	// --------------------------------------------------------------
	public static String getString() throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String s = br.readLine();
		return s;
	}

	// -------------------------------------------------------------
	public static char getChar() throws IOException {
		String s = getString();
		return s.charAt(0);
	}

	// -------------------------------------------------------------
	public static int getInt() throws IOException {
		String s = getString();
		return Integer.parseInt(s);
	}
	// --------------------------------------------------------------
} // end class HashTreeApp
// //////////////////////////////////////////////////////////////