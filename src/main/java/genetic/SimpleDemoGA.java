package genetic;

import java.util.Random;

/**
 *
 * @author Vijini
 */

//Main class
public class SimpleDemoGA {

    private Population population = new Population();
    private Individual fittest;
    private Individual secondFittest;
    private int generationCount = 0;

    public static void main(String[] args) {

        Random rn = new Random();

        SimpleDemoGA demo = new SimpleDemoGA();

        //Initialize population
        demo.population.initializePopulation(10);

        //Calculate fitness of each individual
//        demo.population.calculateFitness();
//
//        System.out.println("Generation: " + demo.generationCount + " Fittest: " + demo.population.fittest);
//
//        //While population gets an individual with maximum fitness
//        while (demo.population.fittest < 5) {
//            ++demo.generationCount;
//
//            //Do selection
//            demo.selection();
//
//            //Do crossover
//            demo.crossover();
//
//            //Do mutation under a random probability
//            if (rn.nextInt()%7 < 5) {
//                demo.mutation();
//            }
//
//            //Add fittest offspring to population
//            demo.addFittestOffspring();
//
//            //Calculate new fitness value
//            demo.population.calculateFitness();
//
//            System.out.println("Generation: " + demo.generationCount + " Fittest: " + demo.population.fittest);
//        }
//
//        System.out.println("\nSolution found in generation " + demo.generationCount);
//        System.out.println("Fitness: "+demo.population.getFittest().fitness);
//        System.out.print("Genes: \n");
//        for (int i = 0; i < 5; i++) {
//            System.out.print(demo.population.getFittest().genes[i]);
//        }

    }

    //Selection
    private void selection() {

        //Select the most fittest individual
        fittest = population.getFittest();

        //Select the second most fittest individual
        secondFittest = population.getSecondFittest();
    }

    //Crossover
    private void crossover() {
//        Random rn = new Random();
//
//        //Select a random crossover point
//        int crossOverPoint = rn.nextInt(population.individuals[0].geneLength);
//
//        //Swap values among parents
//        for (int i = 0; i < crossOverPoint; i++) {
//            int temp = fittest.genes[i];
//            fittest.genes[i] = secondFittest.genes[i];
//            secondFittest.genes[i] = temp;
//
//        }

    }

    //Mutation
    private void mutation() {
//        Random rn = new Random();
//
//        //Select a random mutation point
//        int mutationPoint = rn.nextInt(population.individuals[0].geneLength);
//
//        //Flip values at the mutation point
//        if (fittest.genes[mutationPoint] == 0) {
//            fittest.genes[mutationPoint] = 1;
//        } else {
//            fittest.genes[mutationPoint] = 0;
//        }
//
//        mutationPoint = rn.nextInt(population.individuals[0].geneLength);
//
//        if (secondFittest.genes[mutationPoint] == 0) {
//            secondFittest.genes[mutationPoint] = 1;
//        } else {
//            secondFittest.genes[mutationPoint] = 0;
//        }
    }

    //Get fittest offspring
    private Individual getFittestOffspring() {
        if (fittest.fitness > secondFittest.fitness) {
            return fittest;
        }
        return secondFittest;
    }


    //Replace least fittest individual from most fittest offspring
    private void addFittestOffspring() {

        //Update fitness values of offspring
        fittest.calcFitness();
        secondFittest.calcFitness();

        //Get index of least fit individual
        int leastFittestIndex = population.getLeastFittestIndex();

        //Replace least fittest individual from most fittest offspring
        population.individuals[leastFittestIndex] = getFittestOffspring();
    }

}


//Individual class
class Individual {

    static final private int NUM_OF_WORKERS = 3;
    static final private int NUM_OF_DAYS = 2;
    static final private int NUM_OF_SHIFTS = 4;

    int fitness;
    int[][][] genes = new int[NUM_OF_WORKERS][NUM_OF_DAYS][NUM_OF_SHIFTS];
    int geneLength = 5;

    public Individual() {
        Random rn = new Random();

        //Set genes randomly for each individual
        for (int day = 0; day < NUM_OF_DAYS; day++) {
            for (int shift = 0; shift < NUM_OF_SHIFTS; shift++) {
                // who of the workers will get the shift
                int worker = Math.abs(rn.nextInt() % NUM_OF_WORKERS);
                genes[worker][day][shift] = 1;
            }
        }
        printMatrix(genes);

        fitness = 0;
    }
    private void printMatrix(int[][][] matrix) {
        System.out.println("------------------");
        for (int worker = 0; worker < matrix.length; worker++) {
            printf("man " + worker + ":");
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
                System.out.println("");
            }
        }
    }
    //Calculate fitness
    public void calcFitness() {
//
//        fitness = 0;
//        for (int i = 0; i < 5; i++) {
//            if (genes[i] == 1) {
//                ++fitness;
//            }
//        }
    }

}

//Population class
class Population {

    int popSize = 10;
    Individual[] individuals = new Individual[10];
    int fittest = 0;

    //Initialize population
    public void initializePopulation(int size) {
        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = new Individual();
        }
    }

    //Get the fittest individual
    public Individual getFittest() {
        int maxFit = Integer.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (maxFit <= individuals[i].fitness) {
                maxFit = individuals[i].fitness;
                maxFitIndex = i;
            }
        }
        fittest = individuals[maxFitIndex].fitness;
        return individuals[maxFitIndex];
    }

    //Get the second most fittest individual
    public Individual getSecondFittest() {
        int maxFit1 = 0;
        int maxFit2 = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (individuals[i].fitness > individuals[maxFit1].fitness) {
                maxFit2 = maxFit1;
                maxFit1 = i;
            } else if (individuals[i].fitness > individuals[maxFit2].fitness) {
                maxFit2 = i;
            }
        }
        return individuals[maxFit2];
    }

    //Get index of least fittest individual
    public int getLeastFittestIndex() {
        int minFitVal = Integer.MAX_VALUE;
        int minFitIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (minFitVal >= individuals[i].fitness) {
                minFitVal = individuals[i].fitness;
                minFitIndex = i;
            }
        }
        return minFitIndex;
    }

    //Calculate fitness of each individual
    public void calculateFitness() {

        for (int i = 0; i < individuals.length; i++) {
            individuals[i].calcFitness();
        }
        getFittest();
    }

}