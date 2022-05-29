package city.generators;

import city.Salesman;
import city.generators.params.SalesmanGeneratorParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SalesmanRandomGenerator {

    public SalesmanRandomGenerator(SalesmanGeneratorParams params, Random rnd) {
        this.params = params;
        this.rnd = rnd;
    }

    private SalesmanGeneratorParams params;
    private Random rnd;

    private double generateRandom(double min, double max){
        return min + (max-min) * rnd.nextDouble();
    }

    public List<Salesman> generateList(int number){
        var list = new ArrayList<Salesman>();
        while(number > 0){
            list.add(generateOne());
            number--;
        }
        return list;
    }

    public Salesman generateOne(){
        double weight = generateRandom(params.minWeight, params.maxWeight);
        double costPerTime = generateRandom(params.minCostPerTime, params.maxCostPerTime);
        double costPerDistance = generateRandom(params.minCostPerTime, params.maxCostPerDistance);
        double workingTime = generateRandom(params.minWorkingTime, params.maxWorkingTime);

        return new Salesman(weight, costPerTime, costPerDistance, workingTime, 0d, 23d);
    }
}
