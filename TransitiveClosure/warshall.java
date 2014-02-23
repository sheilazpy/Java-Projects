import java.io.*;

class Graph {

	int adjacency[][];
	int vertices;

	public Graph(int verts) {
		vertices = verts;
		adjacency = new int[vertices][vertices];
	}// constructor

	public void addEdge(int start, int end) {
		adjacency[start][end] = 1;
	}// addEdge()

	public void adjMatDisplay() {
		System.out.print("    ");
		for (int i = 0; i < vertices; i++) {
			System.out.print((char) (65 + i) + "   ");
		}// for
		System.out.println("\n");
		for (int i = 0; i < vertices; i++) {
			System.out.print((char) (65 + i) + "   ");
			for (int j = 0; j < vertices; j++) {
				System.out.print(adjacency[i][j] + "   ");
			}// for
			System.out.println("\n");
		}// for
	}// adjMatDisplay()

	public void warshall() {
		for (int i = 0; i < vertices; i++) {
			for (int j = 0; j < vertices; j++) { //search rows
				if (adjacency[i][j] == 1) {
					for (int k = 0; k < vertices; k++) { //search columns
						if (adjacency[k][i] == 1) {
							adjacency[k][j] = 1;
						}// if
					}// for
				}// if
			}// for
		}// for
	}// warshall()
}// Graph

class WarshallApp {
	public static void main(String[] args) throws IOException {
		int vertices, edges, start, end;
		System.out.print("Enter number of vertices: ");
		vertices = getInt();
		Graph theGraph = new Graph(vertices);
		System.out.print("Enter number of edges: ");
		edges = getInt();
		for (int i = 1; i <= edges; i++) { //get edges
			System.out.print("Enter index (0 to " + (vertices - 1)
					+ ") of start vertex for edge " + i + ": ");
			start = getInt();
			System.out.print("Enter index (0 to " + (vertices - 1)
					+ ") of end vertex for edge " + i + ": ");
			end = getInt();
			theGraph.addEdge(start, end);
		}// for
		System.out.println("Original adjacency matrix");
		theGraph.adjMatDisplay(); // display adj matrix
		theGraph.warshall(); // do the algorithm
		System.out.println("Transitive closure matrix");
		theGraph.adjMatDisplay();
	}// main()

	public static String getString() throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String s = br.readLine();
		return s;
	}// getString()

	public static int getInt() throws IOException {
		String s = getString();
		return Integer.parseInt(s);
	}// getInt()
}// WarshallApp