package solutions.util;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.DoubleStream;

/**
 * Класс для выборки элементов из массива
 */
public class Selection {

    public Random rnd;
    public Selection(Random rnd){
        this.rnd = rnd;
    }

    /**
     * Реализация метода Монте-Карло. Выбирает индекс маассива. Вероятность выбора элемента тем больше, чем больше значение элемента
     * @param values
     * массив значений для выбора
     * @return
     * индекс выбранного элемента
     */
    public int roulette(double values[]){

        double sum = Arrays.stream(values).sum();
        if(sum == 0)
            return 0;
        return chooseProb(
                DoubleStream.of(values)
                        .map(v->v/sum)
                        .toArray());
    }

    /**
     * Выбрать иднекс из массива вероятностей
     * @param probs
     * массив вероятностей. Сумма всех элементов должна быть равна единице
     * @return
     * индекс выбранного элемента
     */
    private int chooseProb(double probs[]){

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
