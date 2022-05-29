package city;

import java.util.Objects;

/**
 * Класс, представляющий ресурс задачи коммивояжёра
 */
public class Resource {
    private double weight;
    private int startVertex;
    private int finishVertex;
    private double minDeliveryTime;
    private double maxDeliveryTime;

    public Resource(double weight, int startVertex, int finishVertex, double minDeliveryTime, double maxDeliveryTime){
        this.weight = weight;
        this.startVertex = startVertex;
        this.finishVertex = finishVertex;
        this.minDeliveryTime = minDeliveryTime;
        this.maxDeliveryTime = maxDeliveryTime;
    }

    public double getWeight() {
        return weight;
    }

    public int getStartVertex() {
        return startVertex;
    }

    public int getFinishVertex() {
        return finishVertex;
    }

    public double getMinDeliveryTime() { return minDeliveryTime; }

    public double getMaxDeliveryTime() {
        return maxDeliveryTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Double.compare(resource.weight, weight) == 0 && startVertex == resource.startVertex && finishVertex == resource.finishVertex && Double.compare(resource.getMaxDeliveryTime(), getMaxDeliveryTime()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, startVertex, finishVertex, getMaxDeliveryTime());
    }

}
