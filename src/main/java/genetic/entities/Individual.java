package genetic.entities;

import genetic.*;

import java.util.Random;

import static genetic.entities.GenAlgoUtilities.*;

public class Individual implements Comparable{
    public int fitness;
    public int[][][] genes;
    Random rn;

    public Individual() {
        genes = new int[NUM_OF_WORKERS][NUM_OF_DAYS][NUM_OF_SHIFTS];
        rn = new Random();

        //Set genes randomly for each individual
        for (int day = 0; day < NUM_OF_DAYS; day++) {
            for (int shift = 0; shift < NUM_OF_SHIFTS; shift++) {
                // who of the workers will get the shift
                int worker;
                for (int counter = 0; counter < WORKERS_IN_SINGLE_SHIFT; counter++) {
                    worker = rn.nextInt(NUM_OF_WORKERS);
                    genes[worker][day][shift] = 1;
                }
            }
        }

        fitness = 0;
    }

    public void print(){
        System.out.println("------------------");
        printMatrix(genes);
    }

    public static void printMatrix(int[][][] matrix) {
        for (int worker = 0; worker < matrix.length; worker++) {
            System.out.printf("man " + worker + ":");
            for(int shift = 0; shift < matrix[worker][0].length; shift++) {
                System.out.printf("shift " + shift + "|");
            }
            System.out.println();
            for (int day = 0; day < matrix[worker].length; day++) {
                System.out.printf("day " + day +";");
                for(int shift = 0; shift < matrix[worker][day].length; shift++) {
                    System.out.printf("%4d", matrix[worker][day][shift]);
                    System.out.printf("    ");
                }
                System.out.println();
            }
        }
    }

    //Calculate fitness
    public void calcFitness(Constraint constraints) {
        fitness = 0;

        if (this.isValid()) {
            for (int worker = 0; worker < genes.length; worker++) {
                for (int day = 0; day < genes[worker].length; day++) {
                    for (int shift = 0; shift < genes[worker][day].length; shift++) {
                        fitness += constraints.constraints[worker][day][shift] * genes[worker][day][shift];
                    }
                }
            }
        }
    }

    public boolean isValid() {
        //Set genes randomly for each individual
        for (int day = 0; day < genes[0].length; day++) {
            for (int shift = 0; shift < genes[0][day].length; shift++) {
                int numOfWorkersInShift = 0;

                for (int worker = 0; worker < genes.length; worker++) {
                    if (genes[worker][day][shift] == 1) {
                        numOfWorkersInShift++;
                    }
                }

                if (numOfWorkersInShift != WORKERS_IN_SINGLE_SHIFT) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int compareTo(Object o) {
        Individual individual2 = (Individual) o;
        if (fitness > individual2.fitness) {
            return -1;
        } else if (fitness == individual2.fitness) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public Individual clone() {
        Individual a = new Individual();
        a.fitness = fitness;

        for (int worker = 0; worker < genes.length; worker++) {
            for (int day = 0; day < genes[worker].length; day++) {
                for (int shift = 0; shift < genes[worker][day].length; shift++) {
                    a.genes[worker][day][shift] = genes[worker][day][shift];
                }
            }
        }

        return a;
    }
}
