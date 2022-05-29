package solutions.distributors;

import city.City;
import city.Resource;
import city.Salesman;
import solutions.util.Selection;
import solutions.util.SolutionEstimater;

import java.util.*;

/**
 * Класс, который распределяет ресурсы между коммивояжёрами
 */
public class ComplexResourceDistributor {


    /**
     * Класс, представляющий параметры коммивояжёра при распределении ресурсов
     */
    class SalesmanInfo{
        public double weightPicked;
        //ma be more params?
    }

    private City city;
    private Selection selection;
    private SolutionEstimater estimater;
    private OneSalesmanResourceDistributor oneSalesmanResourceDistributor;

    public ComplexResourceDistributor(City city, Selection selection, SolutionEstimater estimater, OneSalesmanResourceDistributor oneSalesmanResourceDistributor){
        this.city = city;
        this.selection = selection;
        this.estimater = estimater;
        this.oneSalesmanResourceDistributor = oneSalesmanResourceDistributor;
    }

    /**
     * Генерирует распределение ресурсов в виде пар (коммивожёр-список ресурсов)
     * @return
     * Распределение в установленном городе
     */
    public HashMap<Salesman, List<Resource>> generateNewDistribution(){

        var salesmen = city.getSalesmen();
        var resources = city.getResources();

        return generateNewDistribution(salesmen, resources);
    }

    private HashMap<Salesman, List<Resource>> generateNewDistribution(Salesman[] salesmen, Resource[] resources){

        var result = new HashMap<Salesman, List<Resource>>();
        var salesmenInfo = new HashMap<Salesman, SalesmanInfo>();

        //Генерация начальных параметров коммивояжёров
        Arrays.stream(salesmen)
                .forEach(s->{
                    var info = new SalesmanInfo();
                    info.weightPicked = 0;
                    salesmenInfo.put(s, info);
                });


        for(var resource : resources){

            //Получение значений функции распределения для каждого коммивояжёра
            var values = Arrays.stream(salesmen)
                    .mapToDouble(s->{
                        var info = salesmenInfo.get(s);
                        return distributionFunc(s, resource, info);
                    })
                    .toArray();

            int n = selection.roulette(values);
            var chosenOne = salesmen[n];

            var list = result.get(chosenOne);
            if(list == null){
                list = new ArrayList<>();
                result.put(chosenOne, list);
            }
            list.add(resource);

            var info = salesmenInfo.get(chosenOne);
            info.weightPicked += resource.getWeight();
        }

        for(var s : salesmen){
            if(!salesmenInfo.containsKey(s))
                return generateNewDistribution(salesmen, resources);
        }

        return result;
    }

    /**
     * Определяет распределение в завимиости от логических соображений.
     * Например, выдаёт большее значение, если коммивояжёр набрал мало груза
     * @param salesman
     * Коммивояжёр
     * @param resource
     * Ресурс
     * @param salesmanInfo
     * Текущие параметры указанного коммивояжёра
     * @return
     * Значение функции распределения
     */
    private double distributionFunc(Salesman salesman, Resource resource, SalesmanInfo salesmanInfo){

        double maxWorkingTime = salesman.getWorkDuration();
        double maxWeight = salesman.getMaxWeight();
        double weight = resource.getWeight();
        double maxDeliveryTime = resource.getMaxDeliveryTime();
        double weightPicked = salesmanInfo.weightPicked;

        //Единица устанавливается, чтобы избежать деления на ноль
        if(weightPicked == 0)
            weightPicked = 1;
        if(weight == 0)
            weight = 1;

        if(maxWeight < weight)
            return 0;

        double value = maxWorkingTime * maxWeight * maxDeliveryTime / weight / weightPicked;
        return value;
    }
}
