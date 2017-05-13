import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * Your implementations of various graph algorithms.
 *
 * @author Karel Klein-Cardena
 * @version 1.0
 */
public class GraphAlgorithms {

    /**
     * Perform breadth first search on the given graph, starting at the start
     * Vertex.  You will return a List of the vertices in the order that
     * you visited them.  Make sure to include the starting vertex at the
     * beginning of the list.
     *
     * When exploring a Vertex, make sure you explore in the order that the
     * adjacency list returns the neighbors to you.  Failure to do so may
     * cause you to lose points.
     *
     * The graph passed in may be directed or undirected, but never both.
     *
     * You may import/use {@code java.util.Queue}, {@code java.util.Set},
     * {@code java.util.Map}, {@code java.util.List}, and any classes
     * that implement the aforementioned interfaces.
     *
     * @throws IllegalArgumentException if any input is null, or if
     *         {@code start} doesn't exist in the graph
     * @param start the Vertex you are starting at
     * @param graph the Graph we are searching
     * @param <T> the data type representing the vertices in the graph.
     * @return a List of vertices in the order that you visited them
     */
    public static <T> List<Vertex<T>> breadthFirstSearch(Vertex<T> start,
            Graph<T> graph) {
        if (start == null || graph == null
                || !graph.getAdjacencyList().containsKey(start)) {
            throw new IllegalArgumentException("Invalid input:"
                    + " start vertex or graph may be null,"
                    + " or the start vertex is not in the graph.");
        }
        List<Vertex<T>> bfsList = new ArrayList<Vertex<T>>();
        Map<Vertex<T>, List<VertexDistancePair<T>>> adjacencyList
                = graph.getAdjacencyList();
        Set<Vertex<T>> visited = new HashSet<Vertex<T>>();
        Queue<Vertex<T>> q = new LinkedList<Vertex<T>>();

        q.add(start);
        visited.add(start);
        while (!q.isEmpty()) {
            Vertex<T> current = q.remove();
            bfsList.add(current);
            for (VertexDistancePair<T> vertexPair
                    : adjacencyList.get(current)) {
                if (!visited.contains(vertexPair.getVertex())) {
                    q.add(vertexPair.getVertex());
                    visited.add(vertexPair.getVertex());
                }
            }
        }
        return bfsList;
    }

    /**
     * Perform depth first search on the given graph, starting at the start
     * Vertex.  You will return a List of the vertices in the order that
     * you visited them.  Make sure to include the starting vertex at the
     * beginning of the list.
     *
     * When exploring a Vertex, make sure you explore in the order that the
     * adjacency list returns the neighbors to you.  Failure to do so may
     * cause you to lose points.
     *
     * The graph passed in may be directed or undirected, but never both.
     *
     * You MUST implement this method recursively.
     * Do not use any data structure as a stack to avoid recursion.
     * Implementing it any other way WILL cause you to lose points!
     *
     * You may import/use {@code java.util.Set}, {@code java.util.Map},
     * {@code java.util.List}, and any classes that implement the
     * aforementioned interfaces.
     *
     * @throws IllegalArgumentException if any input is null, or if
     *         {@code start} doesn't exist in the graph
     * @param start the Vertex you are starting at
     * @param graph the Graph we are searching
     * @param <T> the data type representing the vertices in the graph.
     * @return a List of vertices in the order that you visited them
     */
    public static <T> List<Vertex<T>> depthFirstSearch(Vertex<T> start,
            Graph<T> graph) {
        if (start == null || graph == null
                || !graph.getAdjacencyList().containsKey(start)) {
            throw new IllegalArgumentException("Invalid input:"
                   + " start vertex or graph may be null,"
                   + " or the start vertex is not in the graph.");
        }
        List<Vertex<T>> dfsList = new ArrayList<Vertex<T>>();
        Set<Vertex<T>> visited = new HashSet<Vertex<T>>();
        dfsList.add(start);
        visited.add(start);
        return dfs(start, graph, dfsList, visited);
    }

    /**
     * Recursively perform a dfs through a graph from a start vertex
     * @param current the vertex from which to start the dfs
     * @param graph the graph to be searched
     * @param dfsList the list of vertices in th order they are visited
     * @param visited a set of vertices already visited
     * @param <T> the data type representing the vertices in the graph
     * @return a List of vertices in the order that they are visited
     */
    private static <T> List<Vertex<T>> dfs(Vertex<T> current,
                                           Graph<T> graph,
                                           List<Vertex<T>> dfsList,
                                           Set<Vertex<T>> visited) {

        Map<Vertex<T>, List<VertexDistancePair<T>>> adjacencyList
                = graph.getAdjacencyList();
        if (!adjacencyList.get(current).isEmpty()) {
            for (VertexDistancePair<T> vertexPair
                   : adjacencyList.get(current)) {
                if (!visited.contains(vertexPair.getVertex())) {
                    dfsList.add(vertexPair.getVertex());
                    visited.add(vertexPair.getVertex());
                    dfs(vertexPair.getVertex(), graph, dfsList, visited);
                }
            }
        }
        return dfsList;
    }

    /**
     * Find the shortest distance between the start vertex and all other
     * vertices given a weighted graph where the edges only have positive
     * weights.
     *
     * Return a map of the shortest distances such that the key of each entry is
     * a node in the graph and the value for the key is the shortest distance
     * to that node from start, or Integer.MAX_VALUE (representing infinity)
     * if no path exists. You may assume that going from a vertex to itself
     * has a distance of 0.
     *
     * There are guaranteed to be no negative edge weights in the graph.
     * The graph passed in may be directed or undirected, but never both.
     *
     * You may import/use {@code java.util.PriorityQueue},
     * {@code java.util.Map}, and any class that implements the aforementioned
     * interface.
     *
     * @throws IllegalArgumentException if any input is null, or if
     *         {@code start} doesn't exist in the graph
     * @param start the Vertex you are starting at
     * @param graph the Graph we are searching
     * @param <T> the data type representing the vertices in the graph.
     * @return a map of the shortest distances from start to every other node
     *         in the graph.
     */
    public static <T> Map<Vertex<T>, Integer> dijkstras(Vertex<T> start,
            Graph<T> graph) {
        if (start == null || graph == null
                || !graph.getAdjacencyList().containsKey(start)) {
            throw new IllegalArgumentException("Invalid input:"
                    + " start vertex or graph may be null,"
                    + " or the start vertex is not in the graph.");
        }
        Map<Vertex<T>, List<VertexDistancePair<T>>> adjacencyList
                = graph.getAdjacencyList();
        List<Vertex<T>> vertexList = new
                ArrayList<Vertex<T>>(adjacencyList.keySet());
        Map<Vertex<T>, Integer> dist = new HashMap<Vertex<T>, Integer>();

        int infinity = Integer.MAX_VALUE;
        for (Vertex<T> vertex : vertexList) {
            dist.put(vertex, infinity);
        }
        dist.put(start, 0);

        PriorityQueue<VertexDistancePair<T>> h = new
                PriorityQueue<VertexDistancePair<T>>();
        for (List<VertexDistancePair<T>> pairList : adjacencyList.values()) {
            for (VertexDistancePair<T> pair : pairList) {
                h.add(pair);
            }
        }
        h.add(new VertexDistancePair<T>(start, 0));

        while (!h.isEmpty()) {
            Vertex<T> u = h.remove().getVertex();
            if (!adjacencyList.get(u).isEmpty()) {
                for (VertexDistancePair<T> vertexPair : adjacencyList.get(u)) {
                    Vertex<T> v = vertexPair.getVertex();
                    if (v == start) {
                        dist.put(v, 0);
                    } else {
                        int l = vertexPair.getDistance();

                        if (dist.get(v) > dist.get(u) + l) {
                            dist.put(v, dist.get(u) + l);
                            h.add(new
                                    VertexDistancePair<T>(v, dist.get(u) + l));
                        }
                    }
                }
            }
        }
        return dist;
    }

    /**
     * Run Kruskal's algorithm on the given graph and return the minimum
     * spanning tree in the form of a set of Edges. If the graph is
     * disconnected, and therefore there is no valid MST, return null.
     *
     * You may assume that there will only be one valid MST that can be formed.
     * In addition, only an undirected graph will be passed in.
     *
     * You may import/use {@code java.util.PriorityQueue},
     * {@code java.util.Set}, {@code java.util.Map} and any class from java.util
     * that implements the aforementioned interfaces.
     *
     * @throws IllegalArgumentException if graph is null
     * @param graph the Graph we are searching
     * @param <T> the data type representing the vertices in the graph.
     * @return the MST of the graph; null if no valid MST exists.
     */
    public static <T> Set<Edge<T>> kruskals(Graph<T> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Cannot accept null graph.");
        }

        Map<Vertex<T>, List<VertexDistancePair<T>>> adjacencyList = graph.getAdjacencyList();
        List<Vertex<T>> vertexList = new ArrayList<Vertex<T>>(adjacencyList.keySet());
        PriorityQueue<Edge<T>> x = new
                PriorityQueue<Edge<T>>(graph.getEdgeList());
        while (!x.isEmpty()) {

        }
    }

    private void find(Vertex<T> x) {

    }
}
