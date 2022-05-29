package main;

import city.City;
import solutions.ComplexSolution;

import solutions.distributors.ComplexResourceDistributor;
import solutions.distributors.OneSalesmanResourceDistributor;

import solutions.generators.ComplexSolutionGenerator;
import solutions.generators.SimpleSolutionGenerator;

import solutions.mutators.LowComplexSolutionMutator;
import solutions.mutators.MediumComplexSolutionMutator;
import solutions.mutators.SimpleSolutionMutator;

import solutions.util.Selection;
import solutions.util.SolutionEstimater;

import java.util.*;

public class MainAlgorithm {

    private AlgorithmParams params;
    private City city;

    private Random rnd;

    private Selection selection;
    private SolutionEstimater solutionEstimater;

    private ComplexSolutionGenerator complexSolutionGenerator;
    private SimpleSolutionGenerator simpleSolutionGenerator;

    private SimpleSolutionMutator simpleSolutionMutator;
    private LowComplexSolutionMutator lowComplexSolutionMutator;
    private MediumComplexSolutionMutator mediumComplexSolutionMutator;

    private ComplexResourceDistributor complexResourceDistributor;
    private OneSalesmanResourceDistributor oneSalesmanResourceDistributor;

    public MainAlgorithm(AlgorithmParams params, City city, Random rnd){
        this.params = params;
        this.city = city;

        this.rnd = rnd;
        selection = new Selection(rnd);
        solutionEstimater = new SolutionEstimater(city, params.notDeliveredPunishment, params.outOfWorkingTimePunishment, params.minStartWorkingTime, params.maxStartWorkingTime, params.deltaStartWorkingTime, params.outOfWorkingTimePunishment);

        oneSalesmanResourceDistributor = new OneSalesmanResourceDistributor(selection);
        complexResourceDistributor = new ComplexResourceDistributor(city, selection, solutionEstimater, oneSalesmanResourceDistributor);

        simpleSolutionGenerator = new SimpleSolutionGenerator(city, selection, oneSalesmanResourceDistributor, solutionEstimater);
        complexSolutionGenerator = new ComplexSolutionGenerator(simpleSolutionGenerator, complexResourceDistributor);

        simpleSolutionMutator = new SimpleSolutionMutator(rnd, city, params.lowMutationProbability);
        lowComplexSolutionMutator = new LowComplexSolutionMutator(simpleSolutionMutator, selection, solutionEstimater, params.numberOfSimpleSolutionsToMutateDuringLowMutation);
        mediumComplexSolutionMutator = new MediumComplexSolutionMutator(simpleSolutionGenerator, selection, solutionEstimater, params.numberOfSolutionsToMutateDuringMediumMutation);
    }


    public ComplexSolution computeSolution(){
        var solutions = new ArrayList<ComplexSolution>();

        for(int i = 0; i < params.numberOfSurvivedSolutions; i++){
            solutions.add(complexSolutionGenerator.generate());
        }

        int generation = 1;

        boolean check = true;
        while (check){
            var newSolutions = new ArrayList<>(solutions.stream()
                    .flatMap(s->getMutantsFrom(s).stream())
                    .toList());
            var newSolutionsCosts = newSolutions.stream()
                    .mapToDouble(s-> solutionEstimater.estimateComplexSolution(s))
                    .toArray();

            newSolutions.sort(new Comparator<ComplexSolution>() {
                @Override
                public int compare(ComplexSolution o1, ComplexSolution o2) {
                    double d1 = solutionEstimater.estimateComplexSolution(o1);
                    double d2 = solutionEstimater.estimateComplexSolution(o2);
                    return  Double.compare(d1, d2);
                }
            });
            var survivedSolutions = new ArrayList<>(newSolutions.stream().limit(params.numberOfSurvivedSolutions).toList());
            /*for(int i = 0; i < params.numberOfSurvivedSolutions; i++){
                int n = selection.roulette(newSolutions.stream()
                        .mapToDouble(s-> 1d/solutionEstimater.estimateComplexSolution(s))
                        .toArray()
                );
                survivedSolutions.add(newSolutions.get(n));
                newSolutions.remove(n);
            }*/
            solutions = survivedSolutions;

            printGenerationInfo(generation, solutions);
            generation++;
        }

        return null;
    }

    private void printGenerationInfo(int generationNumber, List<ComplexSolution> solutions){
        var sb = new StringBuilder("Generation #" + generationNumber + "\n");
        var sum = solutions.stream()
                .mapToDouble(s->{
                    var cost = solutionEstimater.estimateComplexSolution(s);
                    sb.append(cost + ", ");
                    return cost;
                }).sum();
        sb.append("\nAverageCost: " + sum/solutions.size() + "\n-------------------------------");
        System.out.println(sb.toString());
    }

    private List<ComplexSolution> getMutantsFrom(ComplexSolution solution){
        var mutants = new ArrayList<ComplexSolution>();
        mutants.add(solution);

        try{
            for(int i = 0; i < params.numberOfStrongMutated; i++){
                var newSolution = complexSolutionGenerator.generate();
                mutants.add(newSolution);
            }

            for(int i = 0; i < params.numberOfMediumMutated; i++){
                var clone = solution.clone();
                //var start = System.nanoTime();
                mediumComplexSolutionMutator.mutate(clone);
                //var finish = System.nanoTime();
                //System.out.println((finish - start) / 1000000000d);
                mutants.add(clone);
            }

            for(int i = 0; i < params.numberOfLowMutated; i++){
                var clone = solution.clone();
                //var start = System.nanoTime();
                lowComplexSolutionMutator.mutate(clone);
                mutants.add(clone);
                //var finish = System.nanoTime();
                //System.out.println((finish - start) / 1000000000d);
            }
        } catch (CloneNotSupportedException e){ System.out.println("error");}

        return mutants;
    }
}
