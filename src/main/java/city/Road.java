package city;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.DoubleStream;

public class Road {

    /*
    Класс, который представляет одно ребро графа
    Имеет расстояние и вектор скоростей
     */

    private double distance;
    private double[] speeds;

    private double timeOfPeriod;

    public Road(double distance, double[] speeds){
        this.distance = distance;
        this.speeds = speeds;

        timeOfPeriod = 24d/speeds.length;
    }

    public MovementInformation getMovementInfo(double startTime){
        var info = new MovementInformation();

        info.passedTime = getPassedTime(startTime);
        info.passedDistance = distance;

        return info;
    }

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
}
