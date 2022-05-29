package solutions.util;

import city.City;
import city.Resource;
import solutions.ComplexSolution;
import solutions.SimpleSolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * Оценивает стоимость полного и частного решения задачи коммивояжёра
 */
public class SolutionEstimater {

    private City city;
    private double notDeliveredPunishment; //любое положительное число
    private double outOfWorkingTimePunishment;
    private double deltaStartWorkingTime;
    private double outOfDeliveryTimePunishment;

    public SolutionEstimater(City city, double notDeliveredPunishment, double outOfWorkingTimePunishment, double deltaStartWorkingTime, double outOfDeliveryTimePunishment) {
        this.city = city;
        this.notDeliveredPunishment = notDeliveredPunishment;
        this.outOfWorkingTimePunishment = outOfWorkingTimePunishment;
        this.deltaStartWorkingTime = deltaStartWorkingTime;
        this.outOfDeliveryTimePunishment = outOfDeliveryTimePunishment;
    }

    /**
     * Оценивает стоимость частичного решения задачи
     * @param solution
     * частичное решение для оценки
     * @return
     * стоимость решения
     */
    public double estimateSimpleSolution(SimpleSolution solution){

        //Проверяет, было ли уже подсчитано решение. Если да, то просто возвращает значение

        //Вызывает оценку решения в зависимости от времени начала движения коммивояжёра
        //Выбирает среди них наилучшее (с наименьшей стоимостью)

        var estimatedValue = solution.getEstimatedValue();
        if(estimatedValue.isEmpty()) {

            double minCost = Double.MAX_VALUE;
            double startWorkingTime = solution.getSalesman().getMinStartWorkingTime();
            while(startWorkingTime <= solution.getSalesman().getMaxStartWorkingTime()){
                double cost = calculateSimpleSolutionCost(solution, startWorkingTime);
                if(cost < minCost)
                    minCost = cost;
                startWorkingTime += deltaStartWorkingTime;
            }

            estimatedValue = Optional.of(minCost);
        }
        return estimatedValue.get();
    }

    /**
     * Оценивает частичное решение в зависимости от времени начала движения коммивояжёра
     * @param solution
     * частичное решение для оценки
     * @param startTime
     * время начала движения
     * @return
     * стоимость решения
     */
    private double calculateSimpleSolutionCost(SimpleSolution solution, double startTime){

        //Последовательно проходит по каждой точке маршрута решения
        //Если точка является опорной, то производит разгрузку или загрузку ресурсов

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

            double curTime = time + startTime;

            var movementInfo = city.getRoad(curVertex, nextVertex).getMovementInfo(curTime);
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

                    if(r.getMinDeliveryTime() > curTime || r.getMaxDeliveryTime() < curTime){
                        costModifier += outOfDeliveryTimePunishment;
                    }
                }
            }

            deliveredList.stream().forEach(r->{
                resourceStates.remove(r);
            });
        }

        if(time > salesman.getWorkDuration())
            costModifier += outOfWorkingTimePunishment;

        double cost = distance * salesman.getCostPerDistance() +
                time * salesman.getCostPerTime();

        return cost * costModifier;
    }

    /**
     * Оценивает полное решение задачи коммивояжёра
     * @param solution
     * полное решение задачи
     * @return
     * стоимость решения
     */
    public double estimateComplexSolution(ComplexSolution solution){
        return solution.getSimpleSolutions()
                .stream()
                .mapToDouble(s -> estimateSimpleSolution(s))
                .sum();
    }
}
