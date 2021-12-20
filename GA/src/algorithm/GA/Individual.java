package algorithm.GA;

import algorithm.Algorithm;
import data.Data;

import model.Taxi;
import util.RandomUtil;

import java.util.ArrayList;
import java.util.LinkedList;


public class Individual {

    /**
     * Algorithm
     */
    protected Algorithm algorithm;

    /**
     * Size of individual gene
     */
    public int size;

    /**
     * Gene hoan vi
     */
    public int[] genes;

    /**
     * Current fitness
     */
    public int fitness;

    /**
     * k-barrier obtained
     */
    public Taxi[] taxis;

    protected Individual(Algorithm alg) {
        this.algorithm = alg;
        this.size = alg.data.N+alg.data.M+alg.data.K;
        this.fitness = -1;
    }

    public Individual(Algorithm alg, int[] genes) {
        this(alg);
        this.genes = genes;
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append(this.fitness+" genes=[");
        for (int i = 0; i < this.genes.length-1; i++)
            buff.append(this.genes[i]+", ");
        buff.append(this.genes[this.genes.length-1]+"]");
        return buff.toString();
    }

    public static void random(Algorithm alg, int[] genes) {
        Data data = alg.data;
        int size = data.N+data.M+ data.K;
        boolean[] selected = new boolean[size];
        int i = 0;
        while (i < genes.length) {
            int next = RandomUtil.nextInt(size)+1;
            if (!selected[next-1]) {
                selected[next-1] = true;
                genes[i] = next;
                i++;
                }
            }
    }

    public static Individual random(Algorithm alg) {
        Data data = alg.data;
        int size = data.N+data.M+ data.K;
        int[] genes = new int[size];

        random(alg, genes);

        return new Individual(alg, genes);
    }

    protected void calculateFitness() {
        Data data = algorithm.data;
        int size = data.N+data.M+data.K;
        taxis= new Taxi[data.K];
        for(int j = 0;j<data.K;j++){
            this.taxis[j] = new Taxi();
            this.taxis[j].list = new ArrayList<>();
            this.taxis[j].visitedList = new ArrayList<>();
            this.taxis[j].maxVolume =  data.taxis[j].maxVolume;
        }
        this.fitness = 0;
        int i = size-1;
        while (genes[i]< data.N+data.M+1)
        {
            i--;
        }
        int point = i+1;
        int taxiIndex = genes[i]-data.N-data.M-1;
        while (i>=0){
            if (genes[i]> data.N+data.M){
                taxiIndex = genes[i]-data.N-data.M-1;
            }
            else
            {
                this.taxis[taxiIndex].list.add(genes[i]);
            }
            i--;
        }

        for (int j = point;j<genes.length;j++){
            this.taxis[taxiIndex].list.add(genes[j]);
        }
        for (int j = 0;j<this.taxis.length;j++)
        {
            //System.out.println(taxis[j].toString());
            if(taxis[j].list.size()>=1)
            {
                taxis[j].calculateDistance(data);
            }
            else
            {
                taxis[j].journey = 0;
            }
            if(taxis[j].journey >fitness){
                fitness = taxis[j].journey;
            }
        }
    }
    public Individual callFitness() {
        if (fitness < 0)
            calculateFitness();
        return this;
    }

}
