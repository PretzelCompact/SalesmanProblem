package city.generators;

import city.City;
import city.Resource;
import city.Road;
import city.Salesman;
import city.generators.params.CityGeneratorParams;
import util.IntVector2;

import java.util.HashMap;
import java.util.Random;

public class CityRandomGenerator {

    public CityRandomGenerator(CityGeneratorParams params, ResourceRandomGenerator resourceGenerator, SalesmanRandomGenerator salesmanGenerator, RoadRandomGenerator roadGenerator, Random rnd) {
        this.params = params;
        this.resourceGenerator = resourceGenerator;
        this.salesmanGenerator = salesmanGenerator;
        this.roadGenerator = roadGenerator;
        this.rnd = rnd;
    }

    private CityGeneratorParams params;
    private ResourceRandomGenerator resourceGenerator;
    private SalesmanRandomGenerator salesmanGenerator;
    private RoadRandomGenerator roadGenerator;
    private Random rnd;

    public City generateCity(){
        var resources = resourceGenerator.generateList(params.numberOfResources);
        var salesmen = salesmanGenerator.generateList(params.numberOfSalesmen);
        var roads = generateRoads();
        return new City(roads, resources.toArray(Resource[]::new), salesmen.toArray(Salesman[]::new), params.startVertex, params.numberOfVertices);
    }

    private HashMap<IntVector2, Road> generateRoads(){

        var roads = new HashMap<IntVector2, Road>();

        for(int i = 0; i < params.numberOfVertices; i++){
            for(int j = i; j < params.numberOfVertices; j++){
                if(i != j && rnd.nextDouble() <= params.connectivityRate){
                    var road = roadGenerator.generateOne();
                    var vec1 = new IntVector2(i, j);
                    var vec2 = new IntVector2(j, i);

                    roads.put(vec1, road);
                    roads.put(vec2, road);
                }
            }
        }

        return roads;
    }
}
