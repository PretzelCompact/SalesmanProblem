package main;

public class AlgorithmParams {

    //Вероятность смена пути в лёгкой мутации
    public double lowMutationProbability;

    //Наказание (множитель стоимости решения) за недоставленный ресурс
    public double notDeliveredPunishment;

    //Наказание (множитель стоимости решения) за "просрочку" времени работы коммивояжёра
    public double outOfWorkingTimePunishment;

    //
    public double outOfDeliveryTimePunishment;

    //Количество простых решений, которые затронет простая мутация
    public int numberOfSimpleSolutionsToMutateDuringLowMutation;

    //Количество простых решений, у которого перераспределяться опорные точки во время средней мутации
    public int numberOfSolutionsToMutateDuringMediumMutation;

    //Количество выживаемых решений на каждой итерации алгоритма
    public int numberOfSurvivedSolutions;

    //Количество сильных мутантов на каждого выжившего итерации
    public int numberOfStrongMutated;

    //Количество средних мутантов на каждого выжившего итерации
    public int numberOfMediumMutated;

    //Количество лёгких мутанов на каждого выживышего итерации
    public int numberOfLowMutated;

    public double minStartWorkingTime;
    public double maxStartWorkingTime;
    public double deltaStartWorkingTime;
}
