package city;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий все параметры задачи коммивояжёра с ограничениями
 */
public class City {

    private Road[][] roads;
    private Resource[] resources;
    private Salesman[] salesmen;
    private int startVertex;
    private int verticesNumber;

    private SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graphOfAverageWeights;
    private DijkstraShortestPath<Integer, DefaultWeightedEdge> dijkstraShortestPath;
    private GraphPath<Integer, DefaultWeightedEdge>[][] averagesPaths;

    public City(Road[][] roads, Resource[] resources, Salesman[] salesmen, int startVertex, int verticesNumber){

        this.roads = roads;
        this.resources = resources;
        this.salesmen = salesmen;
        this.startVertex = startVertex;
        this.verticesNumber = verticesNumber;


        //Добавить вершины в граф
        graphOfAverageWeights = new SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        for(int i = 0; i < verticesNumber; i++){
            graphOfAverageWeights.addVertex(i);
        }

        //Инициализация посика пути в графе средних скоростей
        dijkstraShortestPath = new DijkstraShortestPath<>(graphOfAverageWeights);
        averagesPaths = new GraphPath[verticesNumber][verticesNumber];

        //Инициализация рёбер в графе средних скоростей по матрице дорог
        for(int i = 0; i < verticesNumber; i++){
            for(int j = 0; j < verticesNumber; j++){
                if(roads[i][j] != null){
                    var edge = graphOfAverageWeights.addEdge(i,j);
                    graphOfAverageWeights.setEdgeWeight(edge,roads[i][j].getAverageWeight());
                }
            }
        }
    }

    /**
     * Получить дорогу из матрицы дорог
     * @param start
     * Индекс начальной вершины дороги
     * @param finish
     * Индекс конечной вершины дороги
     * @return
     * Искомая дорога
     */
    public Road getRoad(int start, int finish){
        return roads[start][finish];
    }

    /**
     * Получить путь из графа средних скоростей
     * @param start
     * Индекс начальной вершины
     * @param finish
     * Индекс конечной вершины
     * @return
     * Путь в графе
     */
    public GraphPath<Integer, DefaultWeightedEdge> getAveragePath(int start, int finish){

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
