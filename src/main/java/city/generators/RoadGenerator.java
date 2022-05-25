package city.generators;

import city.Road;
import city.generators.params.RoadGeneratorParams;

import java.util.Random;

public class RoadGenerator {

    private RoadGeneratorParams params;
    private Random rnd;

    public RoadGenerator(RoadGeneratorParams params, Random rnd) {
        this.params = params;
        this.rnd = rnd;
    }

    private double generateRandomDouble(double min, double max){
        return min + (max-min) * rnd.nextDouble();
    }

    public Road generateOne(){
        var distance = generateRandomDouble(params.minDistance, params.maxDistance);
        var speeds = new double[params.numberOfSpeeds];

        for(int i = 0; i < params.numberOfSpeeds; i++)
            speeds[i] = generateRandomDouble(params.minSpeed, params.maxSpeed);

        return new Road(distance, speeds);
    }
}
