package solutions.mutators;

import solutions.ComplexSolution;
import solutions.generators.SimpleSolutionGenerator;
import solutions.util.Selection;
import solutions.util.SolutionEstimater;

import java.util.ArrayList;
import java.util.HashSet;

public class MediumComplexSolutionMutator {

    private SimpleSolutionGenerator simpleSolutionGenerator;
    private Selection selection;
    private SolutionEstimater estimater;
    private int numberOfSolutionsToMutateDuringMediumMutation;

    public MediumComplexSolutionMutator(SimpleSolutionGenerator simpleSolutionGenerator, Selection selection, SolutionEstimater estimater, int numberOfSolutionsToMutateDuringMediumMutation){
        this.simpleSolutionGenerator = simpleSolutionGenerator;
        this.selection = selection;
        this.estimater = estimater;
        this.numberOfSolutionsToMutateDuringMediumMutation = numberOfSolutionsToMutateDuringMediumMutation;
    }

    public void mutate(ComplexSolution solution){

        var simpleSolutions = new ArrayList<>(solution.getSimpleSolutions());
        var costs = simpleSolutions.stream()
                .mapToDouble(s->estimater.estimateSimpleSolution(s))
                .toArray();

        var indicesToMutate = new HashSet<Integer>();

        for(int i = 0; i < numberOfSolutionsToMutateDuringMediumMutation; i++){
            int n = selection.roulette(costs);
            indicesToMutate.add(n);
        }

        for(var n : indicesToMutate){
            var s = simpleSolutions.get(n);
            var mutatedS = simpleSolutionGenerator.generate(s.getResources(), s.getSalesman());
            simpleSolutions.set(n, mutatedS);
        }
        solution.setSimpleSolutions(simpleSolutions);
    }
}
