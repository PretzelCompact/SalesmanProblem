package city;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.ArrayList;
import java.util.List;

public class City {

    private Road[][] roads;
    private Resource[] resources;
    private Salesman[] salesmen;
    private int startVertex;
    private int verticesNumber;

    private SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graphOfAverageWeights;
    //private BellmanFordShortestPath<Integer, DefaultWeightedEdge> averagesPaths;
    private DijkstraShortestPath<Integer, DefaultWeightedEdge> dijkstraShortestPath;
    private GraphPath<Integer, DefaultWeightedEdge>[][] averagesPaths;

    public City(Road[][] roads, Resource[] resources, Salesman[] salesmen, int startVertex, int verticesNumber){

        this.roads = roads;
        this.resources = resources;
        this.salesmen = salesmen;
        this.startVertex = startVertex;
        this.verticesNumber = verticesNumber;

        graphOfAverageWeights = new SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        for(int i = 0; i < verticesNumber; i++){
            graphOfAverageWeights.addVertex(i);
        }

        dijkstraShortestPath = new DijkstraShortestPath<>(graphOfAverageWeights);
        averagesPaths = new GraphPath[verticesNumber][verticesNumber];

        for(int i = 0; i < verticesNumber; i++){
            for(int j = 0; j < verticesNumber; j++){
                if(roads[i][j] != null){
                    var edge = graphOfAverageWeights.addEdge(i,j);
                    graphOfAverageWeights.setEdgeWeight(edge,roads[i][j].getAverageWeight());
                }
            }
        }

        //averagesPaths = new BellmanFordShortestPath<>(graphOfAverageWeights);
        //averagesPaths = new DijkstraShortestPath<>(graphOfAverageWeights);
    }

    public Road getRoad(int start, int finish){
        return roads[start][finish];
    }

    public GraphPath<Integer, DefaultWeightedEdge> getAveragePath(int start, int finish){
        //System.out.println("[" + start + ", " + finish + "]");

       // var startTime = System.nanoTime();
        //var value = averagesPaths.getPath(start, finish);

        //var finishTime = System.nanoTime();
        //System.out.println((finishTime - startTime) / 1000000000d);
        //return value;

        if(averagesPaths[start][finish] == null){
            averagesPaths[start][finish] = dijkstraShortestPath.getPath(start, finish);
        }
        return averagesPaths[start][finish];
    }

    public List<Integer> getNeighboursFrom(int vertex){
        var neighbours = new ArrayList<Integer>();
        for(int j = 0; j < verticesNumber; j++){
            if(roads[vertex][j] != null){
                neighbours.add(j);
            }
        }
        return neighbours;
    }

    public Resource[] getResources() {
        return resources;
    }

    public Salesman[] getSalesmen() {
        return salesmen;
    }

    public int getStartVertex() {
        return startVertex;
    }

    public int getVerticesNumber() {
        return verticesNumber;
    }
}
