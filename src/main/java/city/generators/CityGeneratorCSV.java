package city.generators;

import city.City;
import city.Resource;
import city.Road;
import city.Salesman;
import util.ReaderCSV;

import java.util.List;
import java.util.Random;

public class CityGeneratorCSV {

    private List<List<String>> graphRecords;
    private List<List<String>> resourcesRecords;
    private List<List<String>> salesmenRecords;

    private Random rnd;

    private int numberOfVertices;
    private final double checkRadius = 0.0001d;

    public CityGeneratorCSV(String graphInfoPath, String resourcesInfoPath, String salesmenInfoPath, int numberOfVertices, Random rnd){

        graphRecords = ReaderCSV.getRecords(graphInfoPath, ",");
        resourcesRecords = ReaderCSV.getRecords(resourcesInfoPath, ";");
        salesmenRecords = ReaderCSV.getRecords(salesmenInfoPath, ";");
        this.numberOfVertices = numberOfVertices;
        this.rnd = rnd;
    }

    public City generate(){

        var roads = generateRoads();

        int startVertex = getStartVertex(roads);
        var resources = generateResources(roads, startVertex);
        var salesmen = getSalesmen();


        var city = new City(roads, resources, salesmen, startVertex, numberOfVertices);
        return city;
    }

    private Road[][] generateRoads(){
        final double ROAD_NOT_EXIST_VALUE = 1000000d;
        var roads = new Road[numberOfVertices][numberOfVertices];

        for(int i = 1; i < graphRecords.size(); i++){
            var record = graphRecords.get(i);

            int from = Integer.parseInt(record.get(5));
            int to = Integer.parseInt(record.get(6));
            double distance = Double.parseDouble(record.get(7));
            double speed = Double.parseDouble(record.get(8));
            double cost = Double.parseDouble(record.get(9));
            double reverseCost = Double.parseDouble(record.get(10));

            double x1 = Double.parseDouble(record.get(11));
            double y1 = Double.parseDouble(record.get(12));
            double x2 = Double.parseDouble(record.get(13));
            double y2 = Double.parseDouble(record.get(14));

            if(cost != ROAD_NOT_EXIST_VALUE){
                roads[from][to] = generateRoad(distance, speed, cost, x1, y1, x2, y2);
            }
            if(reverseCost != ROAD_NOT_EXIST_VALUE){
                roads[to][from] = generateRoad(distance, speed, reverseCost, x1, y1, x2, y2);
            }
        }

        return roads;
    }

    private Road generateRoad(double distance, double speed, double cost, double x1, double y1, double x2, double y2){

        var speeds = generateSpeedsVector(speed, distance, cost);
        return new Road(distance, speeds, x1, y1, x2, y2);
    }

    private double[] generateSpeedsVector(double speed, double distance, double cost){

        final int speedsNumber = 48;
        final double speedDelta = 10d;

        double minSpeed = (speed - speedDelta > 0d) ? speed - speedDelta : 1d;
        double maxSpeed = speed + speedDelta;

        var speeds = new double[speedsNumber];
        for(int i = 0; i < speedsNumber; i++){
            speeds[i] = minSpeed + (maxSpeed - minSpeed) * rnd.nextDouble();
        }

        return speeds;
    }

    private Resource[] generateResources(Road[][] roads, int startVertex){
        var resources = new Resource[resourcesRecords.size() - 3];

        for(int i = 3; i < resourcesRecords.size(); i++){

            var rec = resourcesRecords.get(i);

            double weight = 1d;
            double y = Double.parseDouble(rec.get(1));
            double x = Double.parseDouble(rec.get(2));

            var times = rec.get(5).split("-");
            double minDeliveryTime = convertStringToTime(times[0]);
            double maxDeliveryTime = convertStringToTime(times[1]);

            int finishVertex = getVertexFromRoad(roads, x, y);

            resources[i-3] = new Resource(weight, startVertex, finishVertex, minDeliveryTime, maxDeliveryTime);
        }

        return resources;
    }


    private Salesman[] getSalesmen(){
        var salesmen = new Salesman[salesmenRecords.size()];
        for(int i = 0; i < salesmenRecords.size(); i++){
            var rec = salesmenRecords.get(i);

            double maxWeight = Double.parseDouble(rec.get(1));
            double costPerDistance = Double.parseDouble(rec.get(6));
            double costPerTime = Double.parseDouble(rec.get(7));

            var times = rec.get(9).split("-");
            double minWorkingTime = convertStringToTime(times[0]);
            double maxWorkingTime = convertStringToTime(times[1]);

            double duration = maxWorkingTime - minWorkingTime;
            if(duration < 0)
                duration = (24d - minWorkingTime) + maxWorkingTime;

            salesmen[i] = new Salesman(maxWeight, costPerTime, costPerDistance, duration, minWorkingTime, maxWorkingTime);
        }

        return salesmen;
    }

    private int getStartVertex(Road[][] roads){

        double x = 39.607592d;
        double y = 47.203139d;

        return getVertexFromRoad(roads, x, y);
    }

    private int getVertexFromRoad(Road[][] roads, double x, double y){
        for(int i = 0; i < roads.length; i++){
            for(int j = i + 1; j < roads[0].length; j++){

                var road = roads[i][j];
                if(road == null)
                    continue;

                if(isPointInsideCircle(x, y, road.getX1(), road.getY1(), checkRadius)){
                    return i;
                } else if(isPointInsideCircle(x, y, road.getX2(), road.getY2(),checkRadius)){
                    return j;
                }
            }
        }
        throw  new RuntimeException("Vertex (" + x + ", " + y + ") doesn't exist in graph");
    }

    private boolean isPointInsideCircle(double pX, double pY, double cX, double cY, double radius){
        double dX = pX - cX;
        double dY = pY - cY;
        double distance = Math.sqrt(dX*dX + dY*dY);
        return distance <= radius;
    }

    private double convertStringToTime(String strTime){
        var strs = strTime.split("-");

        double hours = Double.parseDouble(strs[0]);
        double minutes = Double.parseDouble(strs[1]);

        return  hours + (minutes / 60d);
    }

}
