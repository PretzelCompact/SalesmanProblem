package solutions.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.DoubleStream;

public class Selection {

    /*
    Selection -- класс, который занимается выборкой элементов массива
     */

    public Random rnd;

    public Selection(Random rnd){
        this.rnd = rnd;
    }

    public int roulette(double values[]){

        /*
        Метод Монте-Карло (рулетка)
         */

        double sum = Arrays.stream(values).sum();
        if(sum == 0)
            return 0;
        return chooseProb(
                DoubleStream.of(values)
                        .map(v->v/sum)
                        .toArray());
    }

    private int chooseProb(double probs[]){

        /*
        На вход подаётся массив вероятностей. Сумма элементов массива должна быть равна единице.
        Выбирается индекс в соответствии с вероятностным распределением
         */

        double sum = 0;
        double chance;
        do { chance = rnd.nextDouble(); } while(chance == 0);

        for(int i = 0; i < probs.length; i++){
            if(probs[i] == 0)
                continue;
            sum += probs[i];
            if(chance <= sum){
                return i;
            }
        }
        Arrays.stream(probs).forEach(p->System.out.println(p));
        throw new RuntimeException("chooseProb() in selection is broken");
        //return chooseProb(probs);
    }
}
