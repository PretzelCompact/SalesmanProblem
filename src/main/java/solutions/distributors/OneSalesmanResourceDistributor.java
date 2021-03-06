package solutions.distributors;

import city.Resource;
import city.Salesman;
import solutions.util.Selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Определяет порядок точек загрузки/разгрузки ресурсов(base points) для одного коммивояжёра
 */
public class OneSalesmanResourceDistributor {

    private Selection selection;

    public OneSalesmanResourceDistributor(Selection selection){
        this.selection = selection;
    }


    /**
     * Сгенерировать опорные точки для коммивояжёра по указанным ресурсам
     * @param resources
     * Ресурсы, которые распредляются
     * @param salesman
     * Коммивояжёр, для которого происходит распределение
     * @return
     * Последовательный список опорных точек
     */
    public List<Integer> generateBasePoints(List<Resource> resources, Salesman salesman){

        int numberOfBasePoints = resources.size() * 2;

        var basePoints = new Integer[numberOfBasePoints];
        var deltaWeightsAtPoints = new Double[numberOfBasePoints];

        for(int i = 0; i < resources.size(); i++){

            int curBasePointsNumber = (i+1) * 2;
            var resource = resources.get(i);
            var weight = resource.getWeight();

            //Подсчёт веса коммивояжёра в каждой опорной точке
            var weightAtPoints = new double[curBasePointsNumber];
            for(int j = 0; j < curBasePointsNumber; j++){

                var delta = deltaWeightsAtPoints[j];
                if(delta == null || delta.equals(0))
                    continue;

                for(int k = j; k < curBasePointsNumber; k++){
                    weightAtPoints[k] += delta;
                }
            }

            //Выбрать и установить индекс загрузки текущего ресурса
            int pickIndex = findInsertionPickPoint(weightAtPoints, resource, salesman.getMaxWeight());
            insertValueIntoArray(basePoints, resource.getStartVertex(), pickIndex);
            insertValueIntoArray(deltaWeightsAtPoints, weight, pickIndex);

            //Пересчитать стоимость в соответствии с загрузкой нового груза
            for(int j = pickIndex; j < weightAtPoints.length; j++){
                weightAtPoints[j] += weight;
            }

            //Выбрать и установить индекс разгрузки текущего ресурса
            int deliveryIndex = findInsertionDeliveredPoint(weightAtPoints, pickIndex);
            insertValueIntoArray(basePoints, resource.getFinishVertex(), deliveryIndex);
            insertValueIntoArray(deltaWeightsAtPoints, -weight, deliveryIndex);

        }

        //Удалить идущие подряд дубликаты (на случай если несколько загрузок или разгрузок в одной точке)
        var list = new ArrayList<>(Arrays.stream(basePoints).toList());
        var lastPoint = list.get(0);
        for(int i = 1; i < list.size(); i++){
            var curPoint = list.get(i);
            if(curPoint.equals(lastPoint)){
                list.remove(i);
                i--;
            } else{
                lastPoint = curPoint;
            }
        }

        return list;
    }

    /**
     * Вставить элемент в массив, сместив все остальные элементы вправо
     * @param array
     * Массив
     * @param value
     * Значение для вставки
     * @param index
     * Индекс для вставки
     * @param <N>
     *     Тип массива
     */
    private <N extends Number> void insertValueIntoArray(N[] array, N value, int index){
        for(int i = array.length - 1; i > index; i--){
            array[i] = array[i-1];
        }
        array[index] = value;
    }

    /**
     * Находит идекс точки загрузки ресурса (порядок относительно других)
     * @param weightAtPoints
     * Загруженный коммивояжёром вес в зависимости от опорной точки
     * @param resource
     * Ресурс для загрузки
     * @param maxWeight
     * Грузоподъёмность коммвояжёра
     * @return
     * Индекс для вставки
     */
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

    /**
     * Находит индекс точки разгрузки ресурса (порядок относительно других)
     * @param weightAtPoints
     * Загруженный коммивояжёром вес в зависимости от опорной точки
     * @param pickedPointIndex
     * Индекс точки загрузки ресурсов
     * @return
     * Индекс для вставки
     */
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
