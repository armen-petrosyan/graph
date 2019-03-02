package graph;

import java.util.*;

/**
 *
 * @param <I> unique identifier
 * @param <T> type
 *
 * {@link Graph} data structure has collection of vertices
 * and relation between {@link Vertex} and adjacency list
 */
public class Graph<I, T> {

    private List<Vertex<I, T>> vertices;
    private HashMap<Vertex<I, T>, List<Edge<I, T>>> adjacencyList;

    public Graph() {
        this.vertices = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
    }

    /**
     *
     * @param id - unique vertex id
     * @param type - type of vertex
     * @return - newly created vertex
     */
    public Vertex<I, T> addVertex(I id, T type) {
        if (null == id || null == type) {
            throw new NullPointerException("Vertex type or ID is null");
        }
        Vertex<I, T> newVertex = new Vertex<>(id, type);
        vertices.add(newVertex);
        adjacencyList.put(newVertex, new ArrayList<Edge<I, T>>());
        return newVertex;
    }

    /**
     *
     * @param first - first vertex connection
     * @param second - second vertex connection
     * @return - newly created edge
     *
     * Creates new edge and fills adjacency list
     *
     */
    public Edge<I, T> addEdge(Vertex<I, T> first, Vertex<I, T> second) {
        if (null == first || null == second) {
            throw new NullPointerException("Some of the vertices is null");
        }
        Edge<I, T> edge = new Edge<>(first, second);

        List<Edge<I, T>> list = adjacencyList.get(first);
        list.add(edge);
        adjacencyList.put(first, list);
        list = adjacencyList.get(second);
        list.add(edge);
        adjacencyList.put(second, list);
        return edge;
    }

    /**
     *
     * @param root - first node for exploration
     * @param patternTypes - list of types represents search pattern
     * @return - list of vertex sequences found in accordance with search pattern
     */
    public HashSet<List<Vertex<I, T>>> search(Vertex<I, T> root, List<T> patternTypes) {
        List<T> pattern = new ArrayList<>(patternTypes);

        /* Search all vertices of type - first element from patternTypes */
        /* In order to find all first pattern element vertices need to loop over all vertices */
        /* Depending on how stored graph decide how to go over vertices */
        /* As an example used BFS and DFS algorithms */
        List<Vertex<I, T>> verticesOfFirstPatternElement = traverseGraphBFS(root, pattern.get(0)); //traverseGraphDFS(root, pattern.get(0));
        if (verticesOfFirstPatternElement == null) {
            throw new NullPointerException("Pattern first type element nodes not found!");
        }

        /* Create initial path of Vertices from first to second pattern element type */
        /* Store all found paths */
        HashSet<List<Vertex<I, T>>> paths = new HashSet<>();
        pattern.remove(0);
        for (Vertex<I, T> vertex : verticesOfFirstPatternElement) {
            List<Vertex<I, T>> path = new ArrayList<>();
            path.add(vertex);
            paths.add(path);
        }

        /* Accumulate all paths in accordance with the pattern */
        for (T patternElement : pattern) {
            HashSet<List<Vertex<I, T>>> tempPaths = new HashSet<>(paths);
            paths = getPathToRelevantNeighbor(tempPaths, patternElement);
        }

        return paths;
    }

    /**
     *
     * @param initialPaths - set of vertex lists stored in order
     * @param patternElement - pattern element
     * @return - accumulated paths
     */
    private HashSet<List<Vertex<I, T>>> getPathToRelevantNeighbor(HashSet<List<Vertex<I, T>>> initialPaths, T patternElement) {

        HashSet<List<Vertex<I, T>>> nextPaths = new HashSet<>();
        for (List<Vertex<I, T>> initialPath : initialPaths) {
            /* gets relevant neighbor for each last element in the certain path */
            /* adds found neighbors to initial path */
            List<Vertex<I, T>> neighbors = getRelevantNeighbors(initialPath.get(initialPath.size()-1), patternElement);
            for (Vertex<I, T> neighbor : neighbors) {
                List<Vertex<I, T>> newPath = new ArrayList<>(initialPath);
                newPath.add(neighbor);
                nextPaths.add(newPath);
            }
        }
        return nextPaths;
    }

    /**
     *
     * Accumulates all vertexes by type given in pattern element
     */
    private List<Vertex<I, T>> getRelevantNeighbors(Vertex<I, T> vertex, T patternElement) {
        List<Vertex<I, T>> relevantNeighbors = new ArrayList<>();
        List<Vertex<I, T>> neighbors = getNeighbors(vertex);
        for (Vertex<I, T> neighbor : neighbors) {
            if (neighbor.getType().equals(patternElement)) {
                relevantNeighbors.add(neighbor);
            }
        }
        return relevantNeighbors;
    }

    /**
     *
     * Gets all neighbors of certain vertex
     */
    private List<Vertex<I,T>> getNeighbors(Vertex<I,T> vertex) {
        List<Vertex<I, T>> neighbors = new ArrayList<>();
        List<Edge<I, T>> edges = adjacencyList.get(vertex);
        for (Edge<I, T> edge : edges) {
            neighbors.add(edge.getSecond());
        }
        return neighbors;
    }

    /**
     *
     * @param rootVertex - first vertex for exploration
     * @param type - vertex type for search
     * @return - list of vertices in accordance with given type
     *
     * Used depth first search algorithm
     */
    private List<Vertex<I, T>> traverseGraphDFS(Vertex<I, T> rootVertex, T type) {

        List<Vertex<I, T>> firstVertices = new ArrayList<>();

        if (rootVertex.getType() == type) {
            firstVertices.add(rootVertex);
        }

        /* Create a Stack. Set rootVertex as visited. Add the rootVertex to stack */
        Stack<Vertex<I, T>> stack = new Stack<>();
        rootVertex.setVisited(true);
        stack.add(rootVertex);

        /* Check if stack is not empty */
        while (!stack.empty()) {
            /* Remove the first element from the stack */
            Vertex<I, T> currentNode = stack.pop();
            /* Check for all of the neighbors */
            for (Vertex<I, T> vertex : getNeighbors(currentNode)) {
                /* If the neighbor is unvisited, mark it as visited and add to the stack */
                if (!vertex.isVisited()) {
                    vertex.setVisited(true);
                    if (vertex.getType() == type) {
                        firstVertices.add(vertex);
                    }
                    stack.push(vertex);
                }
            }
        }
        return firstVertices;
    }

    /**
     *
     * @param rootVertex - first vertex for exploration
     * @param type - vertex type for search
     * @return - list of vertices in accordance with given type
     *
     * Used breadth first search algorithm
     *
     */
    private List<Vertex<I, T>> traverseGraphBFS(Vertex<I, T> rootVertex, T type) {
        List<Vertex<I, T>> result = new ArrayList<>();
        if (null == rootVertex || null == type) {
            throw new NullPointerException("Either root vertex or search type is null !!");
        }
        if (!adjacencyList.containsKey(rootVertex)) {
            throw new IllegalArgumentException("Search Failed : Invalid root vertex !!");
        }

        for (Vertex<I, T> vertex : vertices) {
            vertex.setVisited(false);
        }
        /* Create queue and put vertex to explore */
        Queue<Vertex<I, T>> queue = new ArrayDeque<>();
        queue.add(rootVertex);
        rootVertex.setVisited(true);

        while (!queue.isEmpty()) {
            Vertex<I, T> vertexForExploration = queue.remove();

            /* Start vertex exploration */
            if (vertexForExploration.getType().equals(type)) {
                result.add(vertexForExploration);
            }
            List<Edge<I, T>> edgesConnectedToVertex = adjacencyList.get(vertexForExploration);
            if (null == edgesConnectedToVertex) {
                continue;
            }

            /* Get next unvisited 'child' vertex put to queue */
            for (Edge<I, T> edge : edgesConnectedToVertex) {
                Vertex<I, T> adjacentVertex = edge.getAdjacentVertex(vertexForExploration);
                if (!adjacentVertex.isVisited()) {
                    adjacentVertex.setVisited(true);
                    queue.add(adjacentVertex);
                }
            }
        }
        return result;
    }


}
