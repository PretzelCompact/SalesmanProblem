package solutions.distributors;

import city.Resource;
import city.Salesman;
import solutions.util.Selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OneSalesmanResourceDistributor {

    /*
    OneSalesmanResourceDistributor.
    Класс, который определяет порядок точек загрузки/разгрузки ресурсов для одного коммивояжёра
     */

    private Selection selection;

    public OneSalesmanResourceDistributor(Selection selection){
        this.selection = selection;
    }

    public List<Integer> generateBasePoints(List<Resource> resources, Salesman salesman){

        int numberOfBasePoints = resources.size() * 2;

        var basePoints = new ArrayList<Integer>();
        basePoints.ensureCapacity(numberOfBasePoints);

        var weightAtPoints = new double[resources.size() * 2];

        resources.stream().forEach(r->{
            var weight = r.getWeight();

            int pickedPointIndex;
            if(basePoints.size() == 0)
                pickedPointIndex = 0;
            else
                pickedPointIndex = findInsertionPickPoint(weightAtPoints, r, salesman.getMaxWeight());

            basePoints.add(pickedPointIndex, r.getStartVertex());

            for(int i = pickedPointIndex; i < basePoints.size(); i++){
                weightAtPoints[i] += weight;
            }

            int deliveredPointIndex = findInsertionDeliveredPoint(weightAtPoints, r, pickedPointIndex);
            basePoints.add(deliveredPointIndex, r.getFinishVertex());

            for(int i = deliveredPointIndex; i < basePoints.size(); i++){
                weightAtPoints[i] -= weight;
            }
        });

        return basePoints;
    }

    private int findInsertionPickPoint(double[] weightAtPoints, Resource resource, double maxWeight){

        var weight = resource.getWeight();

        int n = selection.roulette(
                Arrays.stream(weightAtPoints)
                        .map(weightAtPoint-> {
                            if(maxWeight < weightAtPoint + weight)
                                return 0d;
                            return weightAtPoint;
                        })
                        .toArray()
        );

        return n + 1;
    }

    private int findInsertionDeliveredPoint(double[] weightAtPoints, Resource resource, int pickedPointIndex){

        var valuesArray = new double[weightAtPoints.length - pickedPointIndex];

        for(int i = 0; i < valuesArray.length; i++){
            var weightAtPoint = weightAtPoints[pickedPointIndex + i];
            if(weightAtPoint == 0){
                valuesArray[i] = 0;
            } else{
                valuesArray[i] = 1d / weightAtPoints[pickedPointIndex + i];
            }
        }

        int n = selection.roulette(valuesArray);
        return pickedPointIndex + 1 + n;
    }
}
