package solutions.util;

import city.City;
import city.Resource;
import solutions.ComplexSolution;
import solutions.SimpleSolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class SolutionEstimater {

    /*
    SolutionEstimater.
    Класс, который оценивает стоимость решения
    Просто последовательно идёт по точкам маршрута и проверяет точки загрузки/разгрузки
     */

    private City city;
    private double notDeliveredPunishment; //любое положительное число
    private double outOfTimePunishment;

    public SolutionEstimater(City city, double notDeliveredPunishment, double outOfTimePunishment) {
        this.city = city;
        this.notDeliveredPunishment = notDeliveredPunishment;
        this.outOfTimePunishment = outOfTimePunishment;
    }

    public double estimateSimpleSolution(SimpleSolution solution){
        //var start = System.nanoTime();
        var estimatedValue = solution.getEstimatedValue();
        if(estimatedValue.isEmpty()) {
            estimatedValue = Optional.of(calculateSimpleSolutionCost(solution));
        }
        //var finish = System.nanoTime();
        //System.out.println((finish - start) / 1000000000d);
        return estimatedValue.get();
    }

    private double calculateSimpleSolutionCost(SimpleSolution solution){

        var baseIndices = solution.getBaseIndices();
        var route = solution.getRoute();
        var salesman = solution.getSalesman();

        //Ресурс загружен или нет
        var resourceStates = new HashMap<Resource, Boolean>();

        solution.getResources().stream().forEach(r->resourceStates.put(r, false));

        double distance = 0;
        double time = 0;
        double weight = 0;
        double costModifier = 1;

        int j = 1;
        var nextBaseIndex = baseIndices.get(j);

        for(int i = 1; i < route.size(); i++){
            var curVertex = route.get(i-1);
            var nextVertex = route.get(i);

            //System.out.println("[" + curVertex + ", " + nextVertex + "]");
            var movementInfo = city.getRoad(curVertex, nextVertex).getMovementInfo(time);
            time += movementInfo.passedTime;
            distance += movementInfo.passedDistance;

            if(i != nextBaseIndex)
                continue;

            j++;

            var deliveredList = new ArrayList<Resource>();

            for(var r : resourceStates.keySet()){

                boolean resourcePicked = resourceStates.get(r);

                if(r.getStartVertex() == nextVertex && !resourcePicked){
                    resourceStates.put(r, true);
                    weight += r.getWeight();
                    if(weight > salesman.getMaxWeight()){
                        costModifier += notDeliveredPunishment;
                    }
                } else if(r.getFinishVertex() == nextVertex && resourcePicked){
                    deliveredList.add(r);
                    weight -= r.getWeight();
                }
            }

            deliveredList.stream().forEach(r->{
                resourceStates.remove(r);
            });
        }

        if(time > salesman.getMaxWorkingTime())
            costModifier += outOfTimePunishment;

        double cost = distance * salesman.getCostPerDistance() +
                time * salesman.getCostPerTime();



        if(costModifier != 1)
            return Double.MAX_VALUE / 1024;
        else
            return cost;


        //return cost * costModifier;
    }

    public double estimateComplexSolution(ComplexSolution solution){
        return solution.getSimpleSolutions()
                .stream()
                .mapToDouble(s -> estimateSimpleSolution(s))
                .sum();
    }
}
