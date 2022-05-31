package city;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import util.IntVector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Класс, представляющий все параметры задачи коммивояжёра с ограничениями
 */
public class City {

    private HashMap<IntVector2, Road> roads;
    private Resource[] resources;
    private Salesman[] salesmen;
    private int startVertex;
    private int verticesNumber;

    private SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graphOfAverageWeights;
    private DijkstraShortestPath<Integer, DefaultWeightedEdge> dijkstraShortestPath;
    private HashMap<IntVector2, GraphPath<Integer, DefaultWeightedEdge>> averagesPaths;

    public City(HashMap<IntVector2, Road> roads, Resource[] resources, Salesman[] salesmen, int startVertex, int verticesNumber){

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
        averagesPaths = new HashMap<>();

        //Инициализация рёбер в графе средних скоростей по матрице дорог
        for(var key : roads.keySet()){
            if(key.to() == key.from())
                continue;
            var road = roads.get(key);
            var edge = graphOfAverageWeights.addEdge(key.from(), key.to());
            graphOfAverageWeights.setEdgeWeight(edge,road.getAverageWeight());
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
        return roads.get(new IntVector2(start, finish));
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

        var vec = new IntVector2(start, finish);
        var path = averagesPaths.get(vec);

        if(path == null){
            path = dijkstraShortestPath.getPath(start, finish);
            averagesPaths.put(vec, path);
        }
        return path;
    }

    public List<Integer> getNeighboursFrom(int vertex){
        var neighbours = new ArrayList<Integer>();

        neighbours.addAll(roads.keySet().stream()
                .filter(vec->vec.from() == vertex)
                .map(vec->vec.to())
                .toList());

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
