package city;

import java.util.Objects;

public class Salesman {

    /*
    Класс, который представляет коммивояжёра
     */

    private double maxWeight;
    private double costPerTime;
    private double costPerDistance;
    private double maxWorkingTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salesman salesman = (Salesman) o;
        return Double.compare(salesman.maxWeight, maxWeight) == 0 && Double.compare(salesman.costPerTime, costPerTime) == 0 && Double.compare(salesman.costPerDistance, costPerDistance) == 0 && Double.compare(salesman.maxWorkingTime, maxWorkingTime) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxWeight, costPerTime, costPerDistance, maxWorkingTime);
    }

    public Salesman(double maxWeight, double costPerTime, double costPerDistance, double maxWorkingTime){
        this.maxWeight = maxWeight;
        this.costPerTime = costPerTime;
        this.costPerDistance = costPerDistance;
        this.maxWorkingTime = maxWorkingTime;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public double getCostPerTime() {
        return costPerTime;
    }

    public double getCostPerDistance() {
        return costPerDistance;
    }

    public double getMaxWorkingTime() {
        return maxWorkingTime;
    }
}
