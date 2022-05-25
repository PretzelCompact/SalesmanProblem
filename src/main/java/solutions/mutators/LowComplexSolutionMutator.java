package solutions.mutators;

import solutions.ComplexSolution;
import solutions.util.Selection;
import solutions.util.SolutionEstimater;

public class LowComplexSolutionMutator {

    private SimpleSolutionMutator simpleSolutionMutator;
    private SolutionEstimater estimater;
    private Selection selection;
    private int numberOfSimpleSolutionsToMutate;

    public LowComplexSolutionMutator(SimpleSolutionMutator simpleSolutionMutator, Selection selection, SolutionEstimater estimater, int numberOfSimpleSolutionsToMutate){
        this.simpleSolutionMutator = simpleSolutionMutator;
        this.selection = selection;
        this.estimater = estimater;
        this.numberOfSimpleSolutionsToMutate = numberOfSimpleSolutionsToMutate;
    }

    public void mutate(ComplexSolution solution){
        var simpleSolutions = solution.getSimpleSolutions();
        var costs = simpleSolutions.stream().mapToDouble(s->estimater.estimateSimpleSolution(s)).toArray();

        for(int i = 0; i < numberOfSimpleSolutionsToMutate; i++){
            int n = selection.roulette(costs);
            simpleSolutionMutator.mutate(simpleSolutions.get(n));
            simpleSolutions.get(n).resetEstimatedValue();
        }
    }
}
