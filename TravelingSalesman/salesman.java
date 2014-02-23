import java.io.*;

class Graph {

	double adjacency[][];
	int vertices;
	int currentRoute[];
	String shortRoute;
	double currentWeight, shortWeight;

	// Create a graph with verts vertices and no edges
	public Graph(int verts) {
		vertices = verts;
		adjacency = new double[vertices][vertices];
		currentRoute = new int[vertices + 1];
		for (int i = 0; i < vertices; i++) { // Initialize adjacency matrix
												// with no edges
			for (int j = 0; j < vertices; j++) {
				adjacency[i][j] = Double.POSITIVE_INFINITY;
			}// for
			currentRoute[i] = i; // Initialize starting route
		}// for
		shortWeight = Double.POSITIVE_INFINITY;
	}// constructor

	// Add an undirected edge with the specified information to the adjacency
	// matrix
	public void addEdge(int start, int end, double weight) {
		adjacency[start][end] = weight; // Add edge
		adjacency[end][start] = weight; // Add edge in opposite direction
	}// addEdge()

	// Find and print shortest route in graph
	public void shortRoute() {
		doAnagram(vertices - 1); // Check all possible routes
		System.out.println("\nShortest route is"); // Print shortest route
		System.out.println(shortRoute);
		System.out.println("Distance is " + shortWeight);
	} // shortRoute()

	// Permute all possible routes
	private void doAnagram(int newSize) {
		if (newSize == 1) // if too small,
			return; // go no further
		for (int j = 0; j < newSize; j++) // for each position,
		{
			doAnagram(newSize - 1); // anagram remaining
			if (newSize == 2) // if innermost,
				checkRoute(); // check route weight
			rotate(newSize); // rotate vertices
		}// for
	}// doAnagram()

	// Check length of route and print
	private void checkRoute() {
		currentWeight = 0;
		for (int i = 0; i < vertices; i++) { // Calculate weight of current
												// route
			currentWeight += adjacency[currentRoute[i]][currentRoute[i + 1]];
		}// for
		if (currentWeight == Double.POSITIVE_INFINITY) { // Check if invalid
															// route
			System.out.println(printRoute() + ": Invalid route");
		} else {
			System.out.println(printRoute() + ": Distance = " + currentWeight);
			if (currentWeight < shortWeight) { // Check if shortest
				shortWeight = currentWeight;
				shortRoute = printRoute();
			}// if
		}// if-else
	}// checkRoute()

	// Convert route array into a String
	private String printRoute() {
		String route = "";
		for (int i = 0; i <= vertices; i++) {
			route += (char) (currentRoute[i] + 65) + " "; // Convert route index
															// to a character
		}// for
		return route;
	}// printRoute()

	// rotate left all vertices from position to end
	private void rotate(int newSize) {
		int j;
		int position = vertices - newSize;
		int temp = currentRoute[position]; // save first vertex
		for (j = position + 1; j < vertices; j++)
			// shift others left
			currentRoute[j - 1] = currentRoute[j];
		currentRoute[j - 1] = temp; // put first on right
	}// rotate()
}// Graph

class SalesmanApp {
	public static void main(String[] args) throws IOException {
		int vertices, edges, start, end;
		double weight;
		System.out.print("Enter number of vertices: ");
		vertices = getInt();
		Graph theGraph = new Graph(vertices);
		System.out.print("Enter number of edges: ");
		edges = getInt();
		for (int i = 1; i <= edges; i++) { // get edges
			System.out.print("Enter index (0 to " + (vertices - 1)
					+ ") of first vertex for edge " + i + ": ");
			start = getInt();
			System.out.print("Enter index (0 to " + (vertices - 1)
					+ ") of second vertex for edge " + i + ": ");
			end = getInt();
			System.out.print("Enter weight of edge " + i + ": ");
			weight = getDouble();
			theGraph.addEdge(start, end, weight);
		}// for
		theGraph.shortRoute();
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

	public static double getDouble() throws IOException {
		String s = getString();
		return Double.parseDouble(s);
	}// getDouble()
}// SalesmanApp