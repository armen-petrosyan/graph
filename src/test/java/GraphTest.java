import graph.Graph;
import graph.Vertex;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GraphTest {

    @Test
    public void graphSearchByPatternTest() {
        Graph<Integer, String> graph = new Graph<>();
        Vertex<Integer, String> vertex1 = graph.addVertex(1, "A");
        Vertex<Integer, String> vertex2 = graph.addVertex(2, "B");
        Vertex<Integer, String> vertex3 = graph.addVertex(3, "D");
        Vertex<Integer, String> vertex4 = graph.addVertex(4, "A");
        Vertex<Integer, String> vertex5 = graph.addVertex(5, "C");
        Vertex<Integer, String> vertex6 = graph.addVertex(6, "B");
        Vertex<Integer, String> vertex7 = graph.addVertex(7, "E");
        Vertex<Integer, String> vertex8 = graph.addVertex(8, "C");

        graph.addEdge(vertex1, vertex2);
        graph.addEdge(vertex2, vertex1);
        graph.addEdge(vertex2, vertex3);
        graph.addEdge(vertex2, vertex5);
        graph.addEdge(vertex3, vertex2);
        graph.addEdge(vertex4, vertex5);
        graph.addEdge(vertex4, vertex6);
        graph.addEdge(vertex4, vertex7);
        graph.addEdge(vertex5, vertex2);
        graph.addEdge(vertex5, vertex4);
        graph.addEdge(vertex5, vertex6);
        graph.addEdge(vertex6, vertex4);
        graph.addEdge(vertex6, vertex5);
        graph.addEdge(vertex6, vertex8);
        graph.addEdge(vertex7, vertex4);
        graph.addEdge(vertex8, vertex6);

        List<String> typePattern = new ArrayList<>();
        typePattern.add("A");
        typePattern.add("B");
        typePattern.add("C");

        HashSet<List<Integer>> actualResults = getActualResults(graph.search(vertex1, typePattern));
        HashSet<List<Integer>> expectedResults = buildExpectedResults();

        System.out.println(actualResults);
        System.out.println(expectedResults);
        assertResult(actualResults, expectedResults);

    }

    private void assertResult(HashSet<List<Integer>> actualResults, HashSet<List<Integer>> expectedResults) {
        for (List<Integer> actualResult : actualResults) {
            Assert.assertTrue(expectedResults.contains(actualResult));
        }
    }

    private HashSet<List<Integer>> getActualResults(HashSet<List<Vertex<Integer, String>>> result) {
        HashSet<List<Integer>> actualResults = new HashSet<>();
        for (List<Vertex<Integer, String>> vertices : result) {
            List<Integer> verticesID = new ArrayList<>();
            for (Vertex<Integer, String> vertex : vertices) {
                verticesID.add(vertex.getId());
            }
            actualResults.add(verticesID);
        }
        return actualResults;
    }

    private HashSet<List<Integer>> buildExpectedResults() {
        HashSet<List<Integer>> expectedResults = new HashSet<>();
        List<Integer> result1 = new ArrayList<>();
        result1.add(4);
        result1.add(6);
        result1.add(5);
        List<Integer> result2 = new ArrayList<>();
        result2.add(1);
        result2.add(2);
        result2.add(5);
        List<Integer> result3 = new ArrayList<>();
        result3.add(4);
        result3.add(6);
        result3.add(8);
        expectedResults.add(result1);
        expectedResults.add(result2);
        expectedResults.add(result3);

        return expectedResults;
    }

}

