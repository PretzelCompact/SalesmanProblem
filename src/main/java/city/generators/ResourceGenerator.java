package city.generators;

import city.Resource;
import city.generators.params.ResourceGeneratorParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ResourceGenerator {

    public ResourceGenerator(ResourceGeneratorParams params, Random rnd) {
        this.params = params;
        this.rnd = rnd;
    }

    private ResourceGeneratorParams params;
    private Random rnd;

    private double generateRandomDouble(double min, double max){
        return min + (max-min) * rnd.nextDouble();
    }

    private int generateRandomInt(int min, int max, int ... exceptions){
        int value = min + Math.abs(rnd.nextInt()) % (max-min);
        boolean check = true;

        for(var e : exceptions){
            if(e == value){
                check = false;
                break;
            }
        }

        if(!check)
            return generateRandomInt(min, max, exceptions);
        return value;
    }

    public List<Resource> generateList(int number){
        var list = new ArrayList<Resource>();
        while(number > 0){
            list.add(generateOne());
            number--;
        }
        return list;
    }

    public Resource generateOne(){
        double weight = generateRandomDouble(params.minWeight, params.maxWeight);
        double deliveryTime = generateRandomDouble(params.minDeliveryTime, params.maxDeliveryTime);

        int startVertex = generateRandomInt(0, params.numberOfVertices, params.startVertex);
        int finishVertex = generateRandomInt(0, params.numberOfVertices, params.startVertex, startVertex);

        return new Resource(weight, startVertex, finishVertex, deliveryTime);
    }
}
