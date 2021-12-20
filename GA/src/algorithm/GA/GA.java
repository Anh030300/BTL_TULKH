package algorithm.GA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static algorithm.GA.Config.*;
import algorithm.Algorithm;
import model.Taxi;
import util.RandomUtil;


public class GA extends Algorithm {

    public GA() {
        super("GA");
    }

    protected List<Individual> init() {
        int i = 0;

        List<Individual> population = new ArrayList<Individual>();
        for (; i < POP_NUM; i++)
            population.add(Individual.random(this));
        return population;
    }

    protected Individual oxCrossover(Individual ind, Individual ind2) {
        // Lai ghep
        int[] genes = new int[ind.size];

        int p1 = RandomUtil.nextInt(genes.length-1), p2 = p1+1+ RandomUtil.nextInt(genes.length-1-p1);

        boolean[] selected = new boolean[ind.size];

        for (int i = p1;i<p2;i++){
            genes[i] = ind2.genes[i];
            selected[ind2.genes[i]-1] = true;
        }
        int k = p2;
        for (int i = p2;i<ind.size;i++){
            if (!selected[ind.genes[i]-1]){
                genes[k]=ind.genes[i];
                k=(++k)%ind.size;
                selected[ind.genes[i]-1] = true;
            }
        }
        for (int i = 0;i<=p2;i++){
            if (!selected[ind.genes[i]-1]){
                genes[k]=ind.genes[i];
                k=(++k)%ind.size;
            }
        }

        return new Individual(this, genes);
    }

    protected Individual rsmMutation(Individual ind) {
        int[] genes = new int[ind.size];

        for (int i = 0; i < ind.size; i++)
            genes[i] = ind.genes[i];

        // Diem dot bien
        int k = RandomUtil.nextInt(ind.size-1), l = k+1+RandomUtil.nextInt(ind.size-k-1);

        for (int i = k; i <= l; i++)
            genes[i] = ind.genes[l+k-i];

        return new Individual(this, genes);
    }


    // Comparator
    private Comparator<Individual> compareFitness = (ind1, ind2) -> ind1.fitness-ind2.fitness;

    protected List<Individual> selection(List<Individual> oldPopulation) {
        List<Individual> list = oldPopulation.stream()
                .sorted(compareFitness)
                .limit(POP_NUM/2)
                .collect(Collectors.toList());
        while (list.size() < POP_NUM)
            list.add(Individual.random(this));
        return list;
    }


    @Override
    protected void doAlgorithm() {
        // Encoding
        List<Individual> population = init();

        // Print
        System.out.println("Inited");

        int nloop = 0;
        int currFitness = -1;

        // Main loop
        for (int i = 0; i < ITER_MAX && nloop < L_MAX && currFitness != 0; i++) {
            List<Individual> ppopulation = population;

            System.out.println("Crossover");

            // Crossover
            List<Individual> childs = new ArrayList<Individual>();
            population.forEach(ind -> {
                // Check lai ghep
                if (RandomUtil.nextDouble() < CROSS_RATE) {
                    // Chon me
                    Individual ind2 = null;
                    do {
                        ind2 = ppopulation.get(RandomUtil.nextInt(ppopulation.size()));
                    } while (ind2 == ind);

                    childs.add(oxCrossover(ind, ind2));
                    childs.add(oxCrossover(ind2, ind));
                }
            });

            System.out.println("Mutation");

            // Mutation
            List<Individual> mutas = new ArrayList<Individual>();
            childs.forEach(ind -> {
                // Check dot bien
                if (RandomUtil.nextDouble() < MUTATE_RATE)
                    mutas.add(rsmMutation(ind));
//                if (RandomUtil.nextDouble() < MUTATE_RATE)
//                    mutas.add(mutation(ind));
            });

            System.out.println("Selection");

            // Selection
            List<Individual> list = Stream.of(population, childs, mutas).flatMap(Collection::stream)
                    .map(ind -> ind.callFitness())
                    .collect(Collectors.toList());

            population = selection(list);

            // Fitness
            if (currFitness < 0 || population.get(0).fitness < currFitness) {
                currFitness = population.get(0).fitness;
                nloop = 0;
            } else
                nloop++;

            // Print
            System.out.println("Iter "+i+": best="+currFitness);
            metaPs.println("Iter "+i+": best="+currFitness);
        }

        Individual best = population.get(0);
        this.MaxDistance = best.fitness;
        this.taxis = best.taxis;

        System.out.println("Solution  " + best);
        for (int i = 0; i < data.K; i++) {
            System.out.println(best.taxis[i].toString());
        }
    }

    public static void main(String args[]) {
        run(new GA(), 5);
    }

}

