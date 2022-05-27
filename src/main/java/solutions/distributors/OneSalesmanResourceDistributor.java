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

        var basePoints = new Integer[numberOfBasePoints];
        var deltaWeightsAtPoints = new Double[numberOfBasePoints];

        for(int i = 0; i < resources.size(); i++){

            int curBasePointsNumber = (i+1) * 2;
            var resource = resources.get(i);
            var weight = resource.getWeight();

            var weightAtPoints = new double[curBasePointsNumber];
            for(int j = 0; j < curBasePointsNumber; j++){

                var delta = deltaWeightsAtPoints[j];
                if(delta == null || delta.equals(0))
                    continue;

                for(int k = j; k < curBasePointsNumber; k++){
                    weightAtPoints[k] += delta;
                }
            }

            int pickIndex = findInsertionPickPoint(weightAtPoints, resource, salesman.getMaxWeight());
            insertValueIntoArray(basePoints, resource.getStartVertex(), pickIndex);
            insertValueIntoArray(deltaWeightsAtPoints, weight, pickIndex);

            for(int j = pickIndex; j < weightAtPoints.length; j++){
                weightAtPoints[j] += weight;
            }

            int deliveryIndex = findInsertionDeliveredPoint(weightAtPoints, pickIndex);
            insertValueIntoArray(basePoints, resource.getFinishVertex(), deliveryIndex);
            insertValueIntoArray(deltaWeightsAtPoints, -weight, deliveryIndex);

        }

        return Arrays.stream(basePoints).toList();
    }

    private <N extends Number> void insertValueIntoArray(N[] array, N value, int index){
        for(int i = array.length - 1; i > index; i--){
            array[i] = array[i-1];
        }
        array[index] = value;
    }

    private int findInsertionPickPoint(double[] weightAtPoints, Resource resource, double maxWeight){

        var weight = resource.getWeight();

        int n = selection.roulette(
                Arrays.stream(weightAtPoints)
                        .limit(weightAtPoints.length-1)
                        .map(weightAtPoint-> {
                            if(maxWeight < weightAtPoint + weight)
                                return 0d;
                            return weightAtPoint;
                        })
                        .toArray()
        );

        return n;
    }

    private int findInsertionDeliveredPoint(double[] weightAtPoints, int pickedPointIndex){

        var values = new double[weightAtPoints.length - pickedPointIndex - 1];

        for(int i = pickedPointIndex + 1; i < weightAtPoints.length; i++){
            var weight = weightAtPoints[i];
            values[i - pickedPointIndex - 1] = (weight == 0) ? 0 : 1d/weight;
        }

        int n = selection.roulette(values);
        return pickedPointIndex + n + 1;
    }
}
