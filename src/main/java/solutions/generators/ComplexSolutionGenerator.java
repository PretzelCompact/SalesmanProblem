package solutions.generators;

import solutions.ComplexSolution;
import solutions.SimpleSolution;
import solutions.distributors.ComplexResourceDistributor;

import java.util.ArrayList;

/**
 * Генерирует полное решение задачи (для всех коммивояжёров)
 */
public class ComplexSolutionGenerator {

    private SimpleSolutionGenerator simpleSolutionGenerator;
    private ComplexResourceDistributor complexResourceDistributor;

    public ComplexSolutionGenerator(SimpleSolutionGenerator simpleSolutionGenerator, ComplexResourceDistributor complexResourceDistributor){
        this.simpleSolutionGenerator = simpleSolutionGenerator;
        this.complexResourceDistributor = complexResourceDistributor;
    }

    public ComplexSolution generate(){
        var distribution = complexResourceDistributor.generateNewDistribution();

        var simpleSolutions = new ArrayList<SimpleSolution>();
        distribution.keySet().stream()
                .forEach(salesman -> {
                    var resources = distribution.get(salesman);
                    var solution = simpleSolutionGenerator.generate(resources, salesman);
                    simpleSolutions.add(solution);
                });

        var complexSolution = new ComplexSolution(simpleSolutions);
        return complexSolution;
    }
}
