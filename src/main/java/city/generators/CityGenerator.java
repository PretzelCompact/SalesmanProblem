package city.generators;

import city.City;
import city.Resource;
import city.Road;
import city.Salesman;
import city.generators.params.CityGeneratorParams;

import java.util.Random;

public class CityGenerator {

    public CityGenerator(CityGeneratorParams params, ResourceGenerator resourceGenerator, SalesmanGenerator salesmanGenerator, RoadGenerator roadGenerator, Random rnd) {
        this.params = params;
        this.resourceGenerator = resourceGenerator;
        this.salesmanGenerator = salesmanGenerator;
        this.roadGenerator = roadGenerator;
        this.rnd = rnd;
    }

    private CityGeneratorParams params;
    private ResourceGenerator resourceGenerator;
    private SalesmanGenerator salesmanGenerator;
    private RoadGenerator roadGenerator;
    private Random rnd;

    public City generateCity(){
        var resources = resourceGenerator.generateList(params.numberOfResources);
        var salesmen = salesmanGenerator.generateList(params.numberOfSalesmen);
        var roads = generateRoads();
        return new City(roads, resources.toArray(Resource[]::new), salesmen.toArray(Salesman[]::new), params.startVertex, params.numberOfVertices);
    }

    private Road[][] generateRoads(){
        var roads = new Road[params.numberOfVertices][params.numberOfVertices];

        for(int i = 0; i < params.numberOfVertices; i++){
            for(int j = i; j < params.numberOfVertices; j++){
                if(i != j && rnd.nextDouble() <= params.connectivityRate){
                    var road = roadGenerator.generateOne();
                    roads[i][j] = road;
                    roads[j][i] = road;
                }
            }
        }

        return roads;
    }
}
