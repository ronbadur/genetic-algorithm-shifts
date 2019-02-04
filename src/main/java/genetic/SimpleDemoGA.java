package genetic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static genetic.GenAlgoUtilities.*;
import static genetic.GenAlgoUtilities.Constraint.Available;
import static genetic.GenAlgoUtilities.Constraint.NotAvailable;
import static genetic.Individual.printMatrix;

/**
 *
 * @author Vijini
 */


//Main class
public class SimpleDemoGA {
    private Population population = new Population();
    private Population newPopulation = new Population();
    private int generationCount = 0;

    public static void main(String[] args) {
        int populationSize = 50;
        int[][][] manualConstraints = {
                {
                        {Available.getValue(), Available.getValue(), NotAvailable.getValue()},
                        {NotAvailable.getValue(), Available.getValue(), Available.getValue()}
                },
                {
                        {Available.getValue(), NotAvailable.getValue(), NotAvailable.getValue()},
                        {Available.getValue(), Available.getValue(), NotAvailable.getValue()}
                },
                {
                        {NotAvailable.getValue(), Available.getValue(), Available.getValue()},
                        {NotAvailable.getValue(), NotAvailable.getValue(), NotAvailable.getValue()}
                },
                {
                        {NotAvailable.getValue(), NotAvailable.getValue(), Available.getValue()},
                        {Available.getValue(), NotAvailable.getValue(), Available.getValue()}
                }
        };

        Random rn = new Random();

        SimpleDemoGA demo = new SimpleDemoGA();

        Constraint constraints = new Constraint(manualConstraints);
        constraints.print();

        //Initialize population
        demo.population.initializePopulation(populationSize);

        //Calculate fitness of each individual
        demo.population.calculateFitness(constraints);
        demo.population.sort();

        System.out.println("Generation: " + demo.generationCount + " Fittest: " + demo.population.getFittest().fitness);

        //While population gets an individual with maximum fitness
        while (demo.population.fittest < (Available.getValue() * GenAlgoUtilities.maxFitnessCanBe())) {
            ++demo.generationCount;

            //Do crossover
            demo.crossover(Math.round(populationSize * 0.5f));

            //Do mutation
            demo.mutation(Math.round(populationSize * 0.4f));

            // Create new randomize
            demo.createNewRandomize(Math.round(populationSize * 0.1f));

            //Cut the half worst offsprings
            demo.newPopulation.calculateFitness(constraints);
            demo.newPopulation.sort();
            demo.newPopulation.individuals = demo.newPopulation.individuals.subList(0, Math.round(populationSize * 0.5f));

            //Cut the half worst of old population
            demo.population.calculateFitness(constraints);
            demo.population.sort();
            demo.population.individuals = demo.population.individuals.subList(0, Math.round(populationSize * 0.5f));

            // Add them together
            demo.population.add(demo.newPopulation);
            demo.newPopulation.clear();

//            demo.population.calculateFitness(constraints);
//            demo.population.printWithFitness();
            System.out.println("Generation: " + demo.generationCount + " Population total Fitness: " +
                    demo.population.populationTotalFitness + " Fittest: " + demo.population.fittest);
        }

        System.out.println("\nSolution found in generation " + demo.generationCount);
        System.out.println("Fitness: " + demo.population.getFittest().fitness);
        System.out.print("Genes: \n");
        demo.population.getFittest().print();

    }

    // Randomize
    private void createNewRandomize(int count) {
        newPopulation.initializePopulation(count);

        for (int i = 0; i < count; i++) {
            newPopulation.add(new Individual());
        }
    }

    // Crossover
    private void crossover(int count) {
        Random rn = new Random();

        Individual first, second;
        for (int i = 0; i < count - 1; i++) {
            first = population.individuals.get(i).clone();
            second = population.individuals.get(i + 1).clone();

            // -1 is for including Zero
            final int MAX_POINT = (NUM_OF_WORKERS * NUM_OF_DAYS * NUM_OF_SHIFTS) - 1;
            int crossOverStartPoint = rn.nextInt(MAX_POINT);
            int crossOverEndPoint = rn.nextInt(MAX_POINT - crossOverStartPoint) + crossOverStartPoint;

            for (int j = crossOverStartPoint; j <= crossOverEndPoint; j++) {

                int worker = j / (NUM_OF_SHIFTS * NUM_OF_DAYS);
                int day = (j % (NUM_OF_SHIFTS * NUM_OF_DAYS)) / NUM_OF_SHIFTS;
                int shift = j % NUM_OF_SHIFTS;

                int temp = first.genes[worker][day][shift];
                first.genes[worker][day][shift] =
                        second.genes[worker][day][shift];
                second.genes[worker][day][shift] = temp;
            }

            newPopulation.add(first);
            newPopulation.add(second);
        }
    }

    //Mutation
    private void mutation(int count) {
        Random rn = new Random();

        Individual first, second;
        for (int i = 0; i < count - 1; i++) {
            first = population.individuals.get(i).clone();
            second = population.individuals.get(i + 1).clone();

            // -1 is for including Zero
            final int MAX_POINT = (NUM_OF_WORKERS * NUM_OF_DAYS * NUM_OF_SHIFTS) - 1;
            int crossOverStartPoint = rn.nextInt(MAX_POINT);
            int crossOverEndPoint = rn.nextInt(MAX_POINT - crossOverStartPoint) + crossOverStartPoint;

            for (int j = crossOverStartPoint; j <= crossOverEndPoint; j++) {

                int worker = j / (NUM_OF_SHIFTS * NUM_OF_DAYS);
                int day = (j % (NUM_OF_SHIFTS * NUM_OF_DAYS)) / NUM_OF_SHIFTS;
                int shift = j % NUM_OF_SHIFTS;


                first.genes[worker][day][shift] = rn.nextInt(1);
//                //Flip values at the mutation point
//                if (first.genes[worker][day][shift] == 0) {
//                    first.genes[worker][day][shift] = 1;
//                } else {
//                    first.genes[worker][day][shift] = 0;
//                }

            }
            newPopulation.add(first);
            newPopulation.add(second);
        }
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
class Individual implements Comparable {

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
        Individual individual2 = (Individual)o;
        if (fitness > individual2.fitness) {
            return -1;
        } else if (fitness == individual2.fitness) {
            return 0;
        } else {
            return 1;
        }
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

    List<Individual> individuals;

    // Before checking this values, you need to run calculateFitness
    int fittest = 0;
    int populationTotalFitness = 0;

    public Population() {
        individuals = new ArrayList<>();
    }

    //Initialize population
    public void initializePopulation(int size) {
        for (int i = 0; i < size; i++) {
            individuals.add(new Individual());
        }
    }

    public void add(Population population) {
        individuals.addAll(population.individuals);
    }

    public void add(Individual individual) {
        individuals.add(individual);
    }

    //Get the fittest individual
    public Individual getFittest() {
        int maxFit = Integer.MIN_VALUE;
        Individual maxFitIndividual = individuals.get(0);

        for (Individual individual: individuals) {
            if (maxFit <= individual.fitness) {
                maxFit = individual.fitness;
                maxFitIndividual = individual;
            }
        }

        return maxFitIndividual;
    }

    //Calculate fitness of each individual
    public void calculateFitness(Constraint constraints) {
        populationTotalFitness = 0;

        for (Individual individual : individuals) {
            individual.calcFitness(constraints);
            populationTotalFitness += individual.fitness;
        }

        fittest = getFittest().fitness;
    }

    public void clear() {
        individuals.clear();
        fittest = 0;
    }

    public void print(){
        individuals.forEach(Individual::print);
    }

    public void sort() {
        individuals.sort(Individual::compareTo);
    }

    public void printWithFitness(){
        individuals.forEach(individual -> {
            individual.print();
            System.out.println("fitness: " + individual.fitness);
        });
    }
}

class GenAlgoUtilities {
    public enum Constraint {
        Available(1000),
        NotInterested(100),
        NotAvailable(10),
        NotAvailableAtAll(1);

        private int value;
        Constraint(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    static final public int WORKERS_IN_SINGLE_SHIFT = 2;
    static final public int NUM_OF_WORKERS = 4;
    static final public int NUM_OF_DAYS = 2;
    static final public int NUM_OF_SHIFTS = 3;


    public static int maxFitnessCanBe() {
        return NUM_OF_DAYS * NUM_OF_SHIFTS * WORKERS_IN_SINGLE_SHIFT;
    }
}