// HuffmanApp.java
// creates a Huffman Code from an input string
// to run this program: C>java HuffmanApp
import java.io.*;
import java.util.*; // for Stack class
import java.util.zip.DataFormatException;

class PriorityQ {
	// array in sorted order, from max at 0 to min at size-1
	private int maxSize;
	private Node[] queArray;
	private int nItems;

	public PriorityQ(int s) { // constructor
		maxSize = s;
		queArray = new Node[maxSize];
		nItems = 0;
	} // end constructor

	public void insert(Node item) { // insert item
		int j;
		if (nItems == 0) // if no items,
			queArray[nItems++] = item; // insert at 0
		else // if items,
		{
			for (j = nItems - 1; j >= 0; j--) // start at end,
			{
				if (item.key > queArray[j].key) // if new item larger,
					queArray[j + 1] = queArray[j]; // shift upward
				else
					// if smaller,
					break; // done shifting
			} // end for
			queArray[j + 1] = item; // insert it
			nItems++;
		} // end else (nItems > 0)
	} // end insert()

	public Node remove() { // remove minimum item
		return queArray[--nItems];
	} // end remove()

	public boolean isEmpty() { // true if queue is empty
		return (nItems == 0);
	} // end isEmpty()
} // end class PriorityQ

class Node {
	public int key; // key
	public char data; // data
	public Node leftChild; // this node's left child
	public Node rightChild; // this node's right child

	public Node(int kch, char dData) { // constructor
		key = kch;
		data = dData;
	} // end constructor

	public void display() { // display ourself
		// display a character representation of spaces, carriage returns and
		// line feeds
		switch (data) {
		case 10:
			System.out.printf("%2s", "lf");
			break;
		case 13:
			System.out.printf("%2s", "cr");
			break;
		case 32:
			System.out.printf("%2s", "sp");
			break;
		default:
			System.out.printf("%2c", data);
			break;
		} // end switch
		System.out.printf("%2d", key);
	} // end display()

	public boolean hasChildren() { // true if Node has 1 or more children
		return !(leftChild == null && rightChild == null);
	} // end hasChildren()
} // end class Node

class Tree {
	public Node root; // first node of tree

	public Tree(Node top) { // constructor
		root = top;
	} // end constructor

	// Graphically displays the tree
	public void displayTree() {
		Stack globalStack = new Stack();
		globalStack.push(root);
		int nBlanks = 32;
		boolean isRowEmpty = false;
		System.out
				.println("......................................................");
		while (isRowEmpty == false) {
			Stack localStack = new Stack();
			isRowEmpty = true;
			for (int j = 0; j < nBlanks; j++)
				System.out.print(' ');
			while (globalStack.isEmpty() == false) {
				Node temp = (Node) globalStack.pop();
				if (temp != null) {
					temp.display();
					localStack.push(temp.leftChild);
					localStack.push(temp.rightChild);
					if (temp.leftChild != null || temp.rightChild != null)
						isRowEmpty = false;
				} else {
					System.out.print(" ---");
					localStack.push(null);
					localStack.push(null);
				}
				for (int j = 0; j < nBlanks * 2 - 4; j++)
					System.out.print(' ');
			} // end while globalStack not empty
			System.out.println();
			nBlanks /= 2;
			while (localStack.isEmpty() == false)
				globalStack.push(localStack.pop());
		} // end while isRowEmpty is false
		System.out
				.println("......................................................");
	} // end displayTree()
} // end class Tree

// Builds a Huffman code table and Huffman tree from an input string
class Huffman {
	private char[] data; // data items (characters)
	private int[] frequency; // frequency of each data item
	private String[] code; // code for each data item
	private int count; // number of data items
	private Tree huffmanTree;

	public Huffman(String s) { // constructor
		data = new char[s.length()];
		frequency = new int[s.length()];
		code = new String[s.length()];
		count = 0;
		getFrequencies(s);
		huffmanTree = new Tree(buildTree());
		getCodes(huffmanTree.root, "");
	} // end constructor

	// Pulls characters from input string and counts frequency of each
	private void getFrequencies(String s) {
		for (int i = 0; i < s.length(); i++) {
			char next = s.charAt(i);
			boolean found = false;
			for (int j = 0; j < count; j++) {
				if (next == data[j]) {
					frequency[j]++;
					found = true;
					break;
				} // end if
			} // end for
			if (!found) {
				data[count] = next;
				frequency[count] = 1;
				count++;
			} // end if
		} // end for
	} // end getFrequencies()

	// Builds the Huffman tree using a priority queue
	private Node buildTree() {
		PriorityQ queue = makeQueue();
		Node left = queue.remove();
		while (!queue.isEmpty()) {
			Node right = queue.remove();
			Node parent = new Node(left.key + right.key, '$');
			parent.leftChild = left;
			parent.rightChild = right;
			queue.insert(parent);
			left = queue.remove();
		} // end while
		if (!left.hasChildren()) { // tree has only one node
			Node parent = new Node(left.key, '$');
			parent.leftChild = left;
			left = parent;
		} // end if
		return left; // last node in queue
	} // end buildTree()

	// Initializes priority queue and fills with leaf nodes
	private PriorityQ makeQueue() {
		PriorityQ queue = new PriorityQ(count);
		for (int i = 0; i < count; i++) {
			Node newNode = new Node(frequency[i], data[i]);
			queue.insert(newNode);
		} // end for
		return queue;
	} // end makeQueue

	// Recursively traverses the Huffman tree, keeping track of the path taken
	private void getCodes(Node localRoot, String path) {
		if (!localRoot.hasChildren()) {
			setCode(localRoot.data, path);
			return;
		} // end if
		getCodes(localRoot.leftChild, path + "0");
		if (localRoot.rightChild != null)
			getCodes(localRoot.rightChild, path + "1");
	} // end getCodes()

	// Sets the Huffman code for the given character
	private void setCode(char value, String path) {
		for (int i = 0; i < count; i++) {
			if (value == data[i]) {
				code[i] = path;
				return;
			} // end if
		} // end for
	} // end setCode()

	// Finds the height of the Huffman tree by finding the longest Huffman code
	private int getSize() {
		int size = 0;
		for (int j = 0; j < count; j++) {
			if (size < code[j].length()) {
				size = code[j].length();
			} // end if
		} // end for
		return size;
	} // end getSize()

	// Uses the Huffman code table to encode an input string. Throws a
	// DataFormatException if the string contains characters not found in the
	// Huffman table.
	public String encode(String input) throws DataFormatException {
		String code = "";
		for (int i = 0; i < input.length(); i++) {
			char next = input.charAt(i);
			boolean found = false;
			for (int j = 0; j < count; j++) {
				if (next == data[j]) {
					code += this.code[j];
					found = true;
					break;
				} // end if
			} // end for
			if (!found)
				throw new DataFormatException(
						"String contains an invalid character: " + next);
		} // end for
		return code;
	} // end encode()

	// Uses the Huffman tree to decode an input string. Throws a
	// DataFormatException if the string is not binary.
	public String decode(String input) throws DataFormatException {
		String message = "";
		Node current = huffmanTree.root;
		for (int i = 0; i < input.length(); i++) {
			switch (input.charAt(i)) {
			case '0':
				current = current.leftChild;
				break;
			case '1':
				current = current.rightChild;
				break;
			default:
				throw new DataFormatException(
						"String must contain only 0s and 1s");
			} // end switch
			if (!current.hasChildren()) {
				message += current.data;
				current = huffmanTree.root;
			} // end if
		} // end for
		return message;
	}// end decode()

	// Displays the Huffman tree if it is small enough
	public void displayTree() {
		if (getSize() < 5) {
			System.out.println("Huffman tree:");
			huffmanTree.displayTree();
		} else
			System.out.println("The tree is too large to display.");
	} // end displayTree()

	// Displays a table of characters with their frequencies and Huffman Codes
	public void displayTable() {
		System.out.println("Huffman code table:");
		System.out.println("Char|Freq|Code");
		for (int i = 0; i < count; i++) {
			// display a character representation of spaces, carriage returns
			// and line feeds
			switch (data[i]) {
			case 10:
				System.out.printf("%4s", "lf");
				break;
			case 13:
				System.out.printf("%4s", "cr");
				break;
			case 32:
				System.out.printf("%4s", "sp");
				break;
			default:
				System.out.printf("%4c", data[i]);
				break;
			} // end switch
			System.out.printf("|%4d|%s\n", frequency[i], code[i]);
		} // end for
	}
} // end class Huffman

/*
 * Creates a Huffman code from an input string. Outputs the Huffman tree, the
 * encoded string, and the re-decoded string. Can handle any characters as
 * input, including carriage returns and line feeds.
 */
class HuffmanApp {
	public static void main(String[] args) throws IOException {
		String str;
		do {
			System.out.println("Enter lines of text to encode ('$' to end): ");
			str = getString();
		} while (str == "");
		Huffman theCode = new Huffman(str);
		theCode.displayTable();
		theCode.displayTree();
		try {
			String code = theCode.encode(str);
			System.out.println("The encoded string is: " + code);
			System.out
					.println("The decoded string is: " + theCode.decode(code));
		} catch (DataFormatException e) {
			System.out.println(e.getMessage());
		} // end try-catch
	} // end main()

	// Reads the input stream character by character until it finds a '$'
	public static String getString() throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String s = "";
		char next = (char) br.read();
		while (next != '$') {
			s += next;
			next = (char) br.read();
		} // end while
		return s;
	} // end getString()
} // end class HuffmanApp