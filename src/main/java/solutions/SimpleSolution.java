package solutions;

import city.Resource;
import city.Salesman;

import java.util.List;
import java.util.Optional;

public class SimpleSolution implements Cloneable{

    /*
    SimpleSolution.
    Простое решение.
    Представляет из себя решение задачи для одного коммивояжёра
     */

    private List<Integer> route;
    private List<Integer> baseIndices;
    private List<Resource> resources;
    private Salesman salesman;

    private Optional<Double> estimatedValue;

    public SimpleSolution(Salesman salesman, List<Resource> resources, List<Integer> route, List<Integer> indicesOfBasePointsInRoute){
        this.salesman = salesman;
        this.setResources(resources);
        this.setRoute(route);
        this.baseIndices = indicesOfBasePointsInRoute;
        estimatedValue = Optional.empty();
    }

    public List<Integer> getRoute() {
        return route;
    }


    public Optional<Double> getEstimatedValue() {
        return estimatedValue;
    }

    public void resetEstimatedValue() {
        estimatedValue = Optional.empty();
    }

    public List<Resource> getResources() {
        return resources;
    }

    public Salesman getSalesman() {
        return salesman;
    }

    @Override
    public SimpleSolution clone() throws CloneNotSupportedException {
        var newResources = List.copyOf(resources);
        var newRoute = List.copyOf(route);
        var newBasePointsIndices = List.copyOf(baseIndices);
        return new SimpleSolution(salesman, newResources, newRoute, newBasePointsIndices);
    }

    public void setRoute(List<Integer> route) {
        this.route = route;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public List<Integer> getBaseIndices() {
        return baseIndices;
    }

    public void setBaseIndices(List<Integer> baseIndices) {
        this.baseIndices = baseIndices;
    }
}
