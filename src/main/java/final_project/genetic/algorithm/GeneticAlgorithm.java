package final_project.genetic.algorithm;

import final_project.common.Algorithm;
import final_project.common.ConstraintEnum;
import final_project.genetic.entities.Constraint;
import final_project.genetic.entities.Individual;
import final_project.genetic.entities.Population;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class GeneticAlgorithm implements Algorithm {

    // Algorithm start time
    private long startTime;
    private long stopTime;
    private long runTimeInMili = 0;

    private Population prevPopulation;
    private Population population;
    private Population newPopulation;
    private int generationCount;
    private static int populationSize = 50;

    private int necessaryWorkers;
    private int maxRunTimeInMili;
    private int numberOfWorkers;
    private int numberOfDays;
    private int numberOfShifts;
    private Constraint algorithmConstraints;
    private Random rn;

    public GeneticAlgorithm(int[][][] shiftRequests, int necessaryWorkers) {
        this(shiftRequests, necessaryWorkers, 5000);
    }

    public GeneticAlgorithm(int[][][] shiftRequests, int necessaryWorkers, int maxRunTimeInMili) {
        this.startTime = System.currentTimeMillis();
        this.necessaryWorkers = necessaryWorkers;
        this.maxRunTimeInMili = maxRunTimeInMili;
        this.numberOfWorkers = shiftRequests.length;
        this.numberOfDays = shiftRequests[0].length;
        this.numberOfShifts = shiftRequests[0][0].length;

        this.rn = new Random();
        this.algorithmConstraints = new Constraint(shiftRequests);
        this.population = new Population(numberOfWorkers, numberOfDays, numberOfShifts, necessaryWorkers);
        this.newPopulation = new Population(numberOfWorkers, numberOfDays, numberOfShifts, necessaryWorkers);
        this.generationCount = 0;
    }

    @Override
    public int[][][] scheduleShifts() {

        population.initializePopulation(populationSize);
        population.calculateFitness(algorithmConstraints);

        System.out.println("Generation: " + generationCount + " Fittest: " + population.getFittest().fitness);

        //While population gets an individual with maximum fitness
        while ((!isPopulationEqualToPrev(population)) &&
                (!isTimePassed(runTimeInMili)) &&
                (!isMaxFitted(population.fittest))) {
            this.prevPopulation = population;

            generationCount++;

            // Do crossover
            newPopulation.add(crossover(Math.round(populationSize * 0.5f)));

            // Do mutation
            newPopulation.add(mutation(Math.round(populationSize * 0.4f)));

            // Create new randomize
            newPopulation.add(createNewRandomize(Math.round(populationSize * 0.1f)));

            // Cut the half worst offsprings
            newPopulation.calculateFitness(algorithmConstraints);
            // newPopulation.sort();
            newPopulation.individuals = newPopulation.getMostFit(Math.round(populationSize * 0.5f));

            // Cut the half worst of old population
            population.calculateFitness(algorithmConstraints);

            // Add them together
            population.add(newPopulation);
            newPopulation.clear();

            population.calculateFitness(algorithmConstraints);
            population.individuals = population.getMostFit(Math.round(populationSize));

            System.out.println("Generation: " + generationCount + " Population total Fitness: " +
                population.populationTotalFitness + " Fittest: " + population.fittest);

            stopTime = System.currentTimeMillis();
            runTimeInMili = stopTime - startTime;

            if (generationCount < 10 ||
                generationCount == 1000) {
                population.printToFileWithFitness(generationCount);
            }
        }

        System.out.println("\nSolution found in generation " + generationCount);
        System.out.println("Fitness: " + population.getFittest().fitness);
        System.out.print("Genes: \n");
        return population.getFittest().genes;
    }

    // Randomize
    private ArrayList<Individual> createNewRandomize(int count) {
        ArrayList<Individual> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            result.add(new Individual(numberOfWorkers, numberOfDays, numberOfShifts, necessaryWorkers));
        }
        if (result == null) {
            System.out.println("randomize wrong");
        }
        return result;
    }

    // Crossover
    private ArrayList<Individual> crossover(int count) {
        Random rn = new Random();
        ArrayList<Individual> result = new ArrayList<>();
        Individual first, second;
        for (int i = 0; i < count - 1; i++) {
            Iterator<Individual> a = population.individuals.iterator();
            if (a.hasNext()) {
                first = a.next().clone();
            } else {
                return result;
            }
            if (a.hasNext()) {
                second = a.next().clone();
            } else {
                return result;
            }

            // -1 is for including Zero
            final int MAX_POINT = (numberOfWorkers * numberOfDays * numberOfShifts) - 1;
            int crossOverStartPoint = rn.nextInt(MAX_POINT);
            int crossOverEndPoint = rn.nextInt(MAX_POINT - crossOverStartPoint) + crossOverStartPoint;

            for (int j = crossOverStartPoint; j <= crossOverEndPoint; j++) {

                int worker = j / (numberOfShifts * numberOfDays);
                int day = (j % (numberOfShifts * numberOfDays)) / numberOfShifts;
                int shift = j % numberOfShifts;

                int temp = first.genes[worker][day][shift];
                first.genes[worker][day][shift] =
                        second.genes[worker][day][shift];
                second.genes[worker][day][shift] = temp;
            }
            result.add(first);
        }

        if (result == null) {
            System.out.println("crosover wrong");
        }
        return result;
    }

    //Mutation
    private ArrayList<Individual> mutation(int count) {
        Random rn = new Random();

        Individual first;
        ArrayList<Individual> result = new ArrayList<>();

        for (int i = 0; i < count - 1; i++) {
            Iterator<Individual> individualIterator = population.individuals.iterator();
            if (individualIterator.hasNext()) {
                first = individualIterator.next().clone();
            } else {
                return result;
            }

            // -1 is for including Zero
            final int MAX_POINT = (numberOfWorkers * numberOfDays * numberOfShifts) - 1;
            int crossOverStartPoint = rn.nextInt(MAX_POINT);
            int crossOverEndPoint = rn.nextInt(MAX_POINT - crossOverStartPoint) + crossOverStartPoint;

            for (int j = crossOverStartPoint; j <= crossOverEndPoint; j++) {

                int worker = j / (numberOfShifts * numberOfDays);
                int day = (j % (numberOfShifts * numberOfDays)) / numberOfShifts;
                int shift = j % numberOfShifts;

                first.genes[worker][day][shift] = rn.nextInt(1);
            }

            result.add(first);
        }
        if (result == null) {
            System.out.println("mutation wrong");
        }
        return result;
    }

    private final boolean isPopulationEqualToPrev(Population newPopulation){
        boolean result;
        return false;
//        if (this.prevPopulation == null) {
//            result = false;
//        } else if (population.equals(prevPopulation)) {
//            result = true;
//        } else {
//            result = false;
//        }
//
//        this.prevPopulation = (Population)newPopulation.clone();
//
//        if (result) System.out.println("The current population is equal to the prev !");
//        return result;
    }
    private final boolean isTimePassed(long currentExecutionTime) {
        boolean result = currentExecutionTime >= maxRunTimeInMili;
        if (result) System.out.println("The time reach to the limit !");

        return result;
    }
    private final boolean isMaxFitted(int maxFitnessInPopulation) {
        boolean result = maxFitnessInPopulation >= maxFitnessCanBe();
        if (result) System.out.println("The result is max Fitted !");
        return result;
    }
    private final int maxFitnessCanBe() {
        return 3 * (numberOfDays * numberOfShifts * necessaryWorkers);
    }
}
