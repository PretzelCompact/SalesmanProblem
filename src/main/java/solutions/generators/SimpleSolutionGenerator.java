package solutions.generators;

import city.City;
import city.Resource;
import city.Salesman;
import solutions.SimpleSolution;
import solutions.distributors.OneSalesmanResourceDistributor;
import solutions.util.Selection;
import solutions.util.SolutionEstimater;

import java.util.ArrayList;
import java.util.List;

public class SimpleSolutionGenerator {
    private City city;
    private Selection selection;
    private OneSalesmanResourceDistributor oneSalesmanResourceDistributor;
    private SolutionEstimater solutionEstimater;

    public SimpleSolutionGenerator(City city, Selection selection, OneSalesmanResourceDistributor oneSalesmanResourceDistributor, SolutionEstimater solutionEstimater){
        this.city = city;
        this.selection = selection;
        this.oneSalesmanResourceDistributor = oneSalesmanResourceDistributor;
        this.solutionEstimater = solutionEstimater;
    }

    public SimpleSolution generate(List<Resource> resources, Salesman salesman){
        var basePoints = oneSalesmanResourceDistributor.generateBasePoints(resources, salesman);
        return generateFromBasePoints(basePoints, salesman, resources);
    }

    private SimpleSolution generateFromBasePoints(List<Integer> basePoints, Salesman salesman, List<Resource> resources){

        var route = new ArrayList<Integer>();
        var indicesOfBasePointsInRoute = new ArrayList<Integer>();

        indicesOfBasePointsInRoute.add(0);
        int lastIndex = 0;
        route.add(basePoints.get(0));

        for(int i = 1; i < basePoints.size(); i++){
            var vertices = city.getAveragePath(basePoints.get(i-1), basePoints.get(i)).getVertexList();

            int newIndex = lastIndex + vertices.size() - 1;
            //System.out.println(newIndex);
            indicesOfBasePointsInRoute.add(newIndex);
            lastIndex = newIndex;

            route.addAll(vertices.subList(1, vertices.size()));
        }

        var solution = new SimpleSolution(salesman, resources, route, indicesOfBasePointsInRoute);
        return solution;
    }
}
