package city;

import java.util.Objects;

public class Salesman {

    /*
    Класс, который представляет коммивояжёра
     */

    private double maxWeight;
    private double costPerTime;
    private double costPerDistance;
    private double workDuration;
    private double minStartWorkingTime;
    private double maxStartWorkingTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salesman salesman = (Salesman) o;
        return Double.compare(salesman.maxWeight, maxWeight) == 0 && Double.compare(salesman.costPerTime, costPerTime) == 0 && Double.compare(salesman.costPerDistance, costPerDistance) == 0 && Double.compare(salesman.workDuration, workDuration) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxWeight, costPerTime, costPerDistance, workDuration);
    }

    public Salesman(double maxWeight, double costPerTime, double costPerDistance, double workDuration, double minStartWorkingTime, double maxStartWorkingTime){
        this.maxWeight = maxWeight;
        this.costPerTime = costPerTime;
        this.costPerDistance = costPerDistance;
        this.workDuration = workDuration;
        this.minStartWorkingTime = minStartWorkingTime;
        this.maxStartWorkingTime = maxStartWorkingTime;
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

    public double getWorkDuration() {
        return workDuration;
    }

    public double getMinStartWorkingTime() {
        return minStartWorkingTime;
    }

    public double getMaxStartWorkingTime() {
        return maxStartWorkingTime;
    }
}
