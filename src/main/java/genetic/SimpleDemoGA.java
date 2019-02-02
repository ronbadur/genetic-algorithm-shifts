package genetic;

import java.util.Arrays;
import java.util.Random;

import static genetic.GenAlgoUtilities.*;
import static genetic.Individual.printMatrix;

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
        int[][][] manualConstraints = {
                {
                        {1, 0},
                        {0, 1}
                },
                {
                        {0, 1},
                        {1, 0}
                }
        };

        Random rn = new Random();

        SimpleDemoGA demo = new SimpleDemoGA();
        //Constraint constraints = new Constraint( NUM_OF_WORKERS, NUM_OF_DAYS, NUM_OF_SHIFTS);

        Constraint constraints = new Constraint(manualConstraints);
        constraints.print();

        //Initialize population
        demo.population.initializePopulation(10);

        //Calculate fitness of each individual
        demo.population.calculateFitness(constraints);
        demo.population.printWithFitness();

        System.out.println("Generation: " + demo.generationCount + " Fittest: " + demo.population.getFittest().fitness);

        //While population gets an individual with maximum fitness
        while (demo.population.fittest < 5) {
            ++demo.generationCount;

            //Do selection
            demo.selection();

            demo.population.printWithFitness();
            //Do crossover
            demo.crossover();
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
            System.out.println("Generation: " + demo.generationCount + " Fittest: " + demo.population.fittest);
        }

        System.out.println("\nSolution found in generation " + demo.generationCount);
        System.out.println("Fitness: " + demo.population.getFittest().fitness);
        System.out.print("Genes: \n");
        demo.population.getFittest().print();

    }

    //Selection
    private void selection() {

        //Select the most fittest individual
        fittest = population.getFittest().clone();

        //Select the second most fittest individual
        secondFittest = population.getSecondFittest().clone();
    }

    //Crossover
    private void crossover() {
        Random rn = new Random();

        //Select a random crossover point
        int crossOverPointWorker = rn.nextInt(fittest.genes.length);
        int crossOverPointDay = rn.nextInt(fittest.genes[0].length);
        int crossOverPointShift = rn.nextInt(fittest.genes[0][0].length);

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
        //fittest.calcFitness();
        //secondFittest.calcFitness();

        //Get index of least fit individual
        int leastFittestIndex = population.getLeastFittestIndex();

        //Replace least fittest individual from most fittest offspring
        population.individuals[leastFittestIndex] = getFittestOffspring();
    }

}

class Constraint {
    int[][][] constraints;

    public Constraint(int[][][] constraints) {
        this.constraints = constraints;
    }

    public Constraint() {
        constraints = new int[NUM_OF_WORKERS][NUM_OF_DAYS][NUM_OF_SHIFTS];
        Random rn = new Random();

        //Set genes randomly for each individual
        for (int worker = 0; worker < NUM_OF_DAYS; worker++) {
            for (int day = 0; day < NUM_OF_DAYS; day++) {
                for (int shift = 0; shift < NUM_OF_SHIFTS; shift++) {
                    // who of the workers will get the shift
                    int isAvailable = rn.nextInt(1);
                    constraints[worker][day][shift] = isAvailable;
                }
            }
        }
    }

    public void print() {
        System.out.println("---------constraints---------");
        printMatrix(constraints);
        System.out.println("Max Fitness Can Be: " + maxFitnessCanBe());
    }
}

//Individual class
class Individual {

    int fitness;
    int[][][] genes;
    Random rn;

    public Individual() {
        genes = new int[NUM_OF_WORKERS][NUM_OF_DAYS][NUM_OF_SHIFTS];
        rn = new Random();

        //Set genes randomly for each individual
        for (int day = 0; day < NUM_OF_DAYS; day++) {
            for (int shift = 0; shift < NUM_OF_SHIFTS; shift++) {
                // who of the workers will get the shift
                // TODO: for more workers, add a loop here.
                int worker = rn.nextInt(NUM_OF_WORKERS);
                genes[worker][day][shift] = 1;
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

                if (numOfWorkersInShift > 1) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected Individual clone() {
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

//Population class
class Population {

    Individual[] individuals;
    int fittest = 0;

    //Initialize population
    public void initializePopulation(int size) {
        individuals = new Individual[size];

        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = new Individual();
        }
    }

    //Get the fittest individual
    public Individual getFittest() {
        int maxFit = Integer.MIN_VALUE;
        Individual maxFitIndividual = individuals[0];

        for (Individual individual: individuals) {
            if (maxFit <= individual.fitness) {
                maxFit = individual.fitness;
                maxFitIndividual = individual;
            }
        }

        return maxFitIndividual;
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
    public void calculateFitness(Constraint constraints) {
        for (Individual individual : individuals) {
            individual.calcFitness(constraints);
        }

        fittest = getFittest().fitness;
    }

    public void print(){
        Arrays.asList(individuals).forEach(Individual::print);
    }

    public void printWithFitness(){
        Arrays.asList(individuals).forEach(individual -> {
            individual.print();
            System.out.println("fitness: " + individual.fitness);
        });
    }
}

class GenAlgoUtilities {
    static final public int NUM_OF_WORKERS = 2;
    static final public int NUM_OF_DAYS = 2;
    static final public int NUM_OF_SHIFTS = 2;

    public static int maxFitnessCanBe() {
        return NUM_OF_DAYS * NUM_OF_SHIFTS;
    }
}