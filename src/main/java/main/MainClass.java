package main;

import city.generators.*;
import city.generators.params.CityGeneratorParams;
import city.generators.params.ResourceGeneratorParams;
import city.generators.params.RoadGeneratorParams;
import city.generators.params.SalesmanGeneratorParams;

import java.time.LocalDateTime;
import java.util.Random;

public class MainClass {
    public static void main(String[] args){

        var algorithmParams = new AlgorithmParams();
        algorithmParams.numberOfStrongMutated = 1;
        algorithmParams.numberOfMediumMutated = 5;
        algorithmParams.numberOfLowMutated = 20;
        algorithmParams.numberOfSurvivedSolutions = 10;
        algorithmParams.notDeliveredPunishment = 1;
        algorithmParams.outOfWorkingTimePunishment = 1;
        algorithmParams.outOfDeliveryTimePunishment = 1;
        algorithmParams.lowMutationProbability = .1d;
        algorithmParams.numberOfSolutionsToMutateDuringMediumMutation = 5;
        algorithmParams.numberOfSimpleSolutionsToMutateDuringLowMutation = 5;
        algorithmParams.deltaStartWorkingTime = 0.5d;

        var rnd = new Random(LocalDateTime.now().getNano());

        /*var cityParams = new CityGeneratorParams();
        cityParams.connectivityRate = .1d;
        cityParams.numberOfVertices = 1000;
        cityParams.startVertex = 0;
        cityParams.numberOfResources = 50;
        cityParams.numberOfSalesmen = 10;

        var resourceParams = new ResourceGeneratorParams();
        resourceParams.minWeight = 70;
        resourceParams.maxWeight = 100;
        resourceParams.minLowerDeliveryTime = 7;
        resourceParams.maxLowerDeliveryTime = 10;
        resourceParams.minUpperDeliveryTime = 10.5;
        resourceParams.maxUpperDeliveryTime = 16;
        resourceParams.numberOfVertices = cityParams.numberOfVertices;
        resourceParams.startVertex = cityParams.startVertex;

        var salesmanParams = new SalesmanGeneratorParams();
        salesmanParams.minWeight = 100;
        salesmanParams.maxWeight = 200;
        salesmanParams.minCostPerDistance = 10;
        salesmanParams.maxCostPerDistance = 20;
        salesmanParams.minCostPerTime = 100;
        salesmanParams.maxCostPerTime = 200;
        salesmanParams.minWorkingTime = 5;
        salesmanParams.maxWorkingTime = 16;

        var roadParams = new RoadGeneratorParams();
        roadParams.minDistance = 2;
        roadParams.maxDistance = 20;
        roadParams.minSpeed = 10;
        roadParams.maxSpeed = 60;
        roadParams.numberOfSpeeds = 48;

        var resourceGenerator = new ResourceRandomGenerator(resourceParams, rnd);
        var salesmanGenerator = new SalesmanRandomGenerator(salesmanParams, rnd);
        var roadGenerator = new RoadRandomGenerator(roadParams, rnd);
        var cityGenerator = new CityRandomGenerator(cityParams, resourceGenerator, salesmanGenerator,roadGenerator, rnd);

        var city = cityGenerator.generateCity();
         */

        var generator = new CityGeneratorCSV("graph_info.csv", "res_info.csv", "vehicles_info.csv", 295242, rnd);
        var city = generator.generate();

        var algorithm = new MainAlgorithm(algorithmParams, city, rnd);
        algorithm.computeSolution();
    }
}
