package graph;

public class Edge<I, T> {

    private Vertex<I, T> first;
    private Vertex<I, T> second;

    Edge(Vertex<I, T> first, Vertex<I, T> second) {
        if (null == first || null == second) {
            throw new NullPointerException("Can't create Edge, one of the Vertex is null");
        }
        this.first = first;
        this.second = second;
    }

    Vertex<I, T> getSecond() {
        return second;
    }

    Vertex<I, T> getAdjacentVertex(Vertex<I, T> vertex) {
        if (vertex == first) {
            return second;
        } else if (vertex == second) {
            return first;
        } else {
            return null;
        }
    }

    public String toString() {
        return first.toString() + "," + second.toString();
    }
}
