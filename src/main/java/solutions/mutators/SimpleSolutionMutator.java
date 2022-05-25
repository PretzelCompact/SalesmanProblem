package solutions.mutators;

import city.City;
import solutions.SimpleSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleSolutionMutator {

    private Random rnd;
    private City city;
    private double mutationProbability;

    public SimpleSolutionMutator(Random rnd, City city, double mutationProbability){
        this.rnd = rnd;
        this.city = city;
        this.mutationProbability = mutationProbability;
    }

    public void mutate(SimpleSolution solution){

        /*
        Пройтись по каждому промежду между двумя опорными точками:
        1. Идти последовательно по точкам старого маршрута
        2. Если происходит мутация, то выбрать случайного соседа этой точки
           Построить маршрут по среднему графу между этим соседом и следующей опорной точкой
         */

        var newRoute = new ArrayList<Integer>();
        var newBaseIndices = new ArrayList<Integer>();

        var indicesOfBasePointsInRoute = solution.getBaseIndices();
        var route = solution.getRoute();

        newBaseIndices.add(indicesOfBasePointsInRoute.get(0));
        newRoute.add(route.get(indicesOfBasePointsInRoute.get(0)));

        for(int i = 1; i < indicesOfBasePointsInRoute.size(); i++){
            var lastBaseIndex = indicesOfBasePointsInRoute.get(i-1);
            var nextBaseIndex = indicesOfBasePointsInRoute.get(i);

            var subRoute = route.subList(lastBaseIndex + 1, nextBaseIndex + 1);

            for(int j = 0; j < subRoute.size(); j++){
                var curVertex = subRoute.get(j);
                newRoute.add(curVertex);

                boolean mutate = j < subRoute.size() - 1 && rnd.nextDouble() <= mutationProbability;
                if(mutate){
                    var neighbours = city.getNeighboursFrom(curVertex);
                    var nextVertex = pickMutationNeighbour(neighbours, curVertex);
                    subRoute = generateRouteBetween(nextVertex, route.get(nextBaseIndex));
                    j = -1;
                }
            }
            newBaseIndices.add(newRoute.size() - 1);
        }

        //System.out.println(indicesOfBasePointsInRoute);
        //System.out.println(newBaseIndices);

        solution.setRoute(newRoute);
        solution.setBaseIndices(newBaseIndices);
    }
    private List<Integer> generateRouteBetween(int startVertex, int finishVertex){
        return city.getAveragePath(startVertex, finishVertex).getVertexList();
    }

    private int pickMutationNeighbour(List<Integer> neighbours, int ... exceptions){

        int n = Math.abs(rnd.nextInt()) % neighbours.size();
        int pickedNeighbour = neighbours.get(n);

        boolean failed = false;

        for(int i = 0; i < exceptions.length; i++){
            if(pickedNeighbour == exceptions[i]){
                failed = true;
                break;
            }
        }

        if(failed)
            return pickMutationNeighbour(neighbours, exceptions);

        return pickedNeighbour;
    }
}
