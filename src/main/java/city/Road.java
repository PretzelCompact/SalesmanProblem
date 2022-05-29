package city;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.DoubleStream;

/**
 * Класс, представляющий дорогу межуд вершинами задачи коммивояжёра
 */
public class Road {

    private double distance;
    private double[] speeds;
    private double x1;
    private double y1;
    private double x2;
    private double y2;

    private double timeOfPeriod;

    public Road(double distance, double[] speeds){
        this.distance = distance;
        this.speeds = speeds;

        timeOfPeriod = 24d/speeds.length;
    }

    public Road(double distance, double[] speeds, double x1, double y1, double x2, double y2) {
        this(distance, speeds);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Получить информацию о перемещении по этой дороге
     * @param startTime
     * Время начала движения
     * @return
     * Экземпляр MovementInformation
     */
    public MovementInformation getMovementInfo(double startTime){
        var info = new MovementInformation();

        info.passedTime = getPassedTime(startTime);
        info.passedDistance = distance;

        return info;
    }

    /**
     * Получить продолжительность движения по дороге
     * @param startTime
     * Время начала движения
     * @return
     * Прошедшее время
     */
    public double getPassedTime(double startTime){
        if(startTime >= 24)
            return 24;

        int n = (int)(startTime/ timeOfPeriod);

        //timePassed -- прошедшее время
        //Стартовое значение: Время до окончания текущего периода
        double timePassed  = (1d-startTime % timeOfPeriod)* timeOfPeriod;
        double distancePassed = timePassed * speeds[n];

        while(distancePassed < distance){
            //Прибавить к прошедшему времени и дистанции значения за следующий период времени
            n = (n+1 >= speeds.length) ? 0 : n+1;
            distancePassed += speeds[n] * timeOfPeriod;
            timePassed += timeOfPeriod;
        }

        //Убрать излишки (движение на участке закончилось раньше, чем период)
        timePassed -= (distancePassed - distance) / speeds[n];
        return timePassed;
    }

    /**
     * Усреднить значение скоростей и получить средний вес дороги
     * @return
     * Средний вес дороги
     */
    public double getAverageWeight(){
        var averageSpeed = DoubleStream.of(speeds).average().getAsDouble();
        return distance / averageSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Road road = (Road) o;
        return Double.compare(road.distance, distance) == 0 && Double.compare(road.timeOfPeriod, timeOfPeriod) == 0 && Arrays.equals(speeds, road.speeds);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(distance, timeOfPeriod);
        result = 31 * result + Arrays.hashCode(speeds);
        return result;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }
}
