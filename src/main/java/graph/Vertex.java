package graph;

public class Vertex<I, T> {

    private I id;
    private T type;
    private boolean visited;

    Vertex(I id, T type) {
        this.id = id;
        this.type = type;
        visited = false;
    }

    public I getId() {
        return id;
    }

    T getType() {
        return type;
    }

    boolean isVisited() {
        return visited;
    }

    void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public String toString() {
        return id + "";
    }

}
