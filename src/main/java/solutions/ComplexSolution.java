package solutions;

import java.util.List;

public class ComplexSolution implements Cloneable{

    /*
    ComplexSolution.
    Представляет совокупность SimpleSolution
     */

    private List<SimpleSolution> simpleSolutions;

    public ComplexSolution(List<SimpleSolution> simpleSolutions){
        this.setSimpleSolutions(simpleSolutions);
    }

    public List<SimpleSolution> getSimpleSolutions() {
        return simpleSolutions;
    }

    @Override
    public ComplexSolution clone() throws CloneNotSupportedException {

        List<SimpleSolution> clonedSimpleSolutions =
                getSimpleSolutions().stream()
                .map(s->{
                    try{
                        return s.clone();
                    } catch (CloneNotSupportedException exception){
                        throw new RuntimeException("Failed while clone SimpleSolution");
                    }
                })
                .toList();
        return new ComplexSolution(clonedSimpleSolutions);
    }

    public void setSimpleSolutions(List<SimpleSolution> simpleSolutions) {
        this.simpleSolutions = simpleSolutions;
    }
}
