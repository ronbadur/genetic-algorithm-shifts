package final_project.genetic.algorithm;

import final_project.common.Algorithm;
import final_project.common.ConstraintEnum;
import final_project.genetic.entities.Constraint;
import final_project.genetic.entities.Individual;
import final_project.genetic.entities.Population;

import java.util.Iterator;
import java.util.Random;

public class GeneticAlgorithm implements Algorithm {

    // Algorithm start time
    private long startTime;
    private long stopTime;
    private long runTimeInMili = 0;

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

            generationCount++;

            // Do crossover
            crossover(Math.round(populationSize * 0.5f));

            // Do mutation
            mutation(Math.round(populationSize * 0.4f));

            // Create new randomize
            createNewRandomize(Math.round(populationSize * 0.1f));

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
    private void createNewRandomize(int count) {
        newPopulation.initializePopulation(count);

        for (int i = 0; i < count; i++) {
            newPopulation.add(new Individual(numberOfWorkers, numberOfDays, numberOfShifts, necessaryWorkers));
        }
    }

    // Crossover
    private void crossover(int count) {
        Random rn = new Random();

        Individual first, second;
        for (int i = 0; i < count - 1; i++) {
            Iterator<Individual> a = population.individuals.iterator();
            if (a.hasNext()) {
                first = a.next().clone();
            } else {
                continue;
            }
            if (a.hasNext()) {
                second = a.next().clone();
            } else {
                continue;
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

            newPopulation.add(first);
            newPopulation.add(second);
        }
    }

    //Mutation
    private void mutation(int count) {
        Random rn = new Random();

        Individual first, second;
        for (int i = 0; i < count - 1; i++) {
            Iterator<Individual> a = population.individuals.iterator();
            if (a.hasNext()) {
                first = a.next().clone();
            } else {
                continue;
            }
            if (a.hasNext()) {
                second = a.next().clone();
            } else {
                continue;
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
            newPopulation.add(first);
            newPopulation.add(second);
        }
    }

    private final boolean isPopulationEqualToPrev(Population newPopulation){
        // TODO: implement
        return false;
    }
    private final boolean isTimePassed(long currentExecutionTime) {
        return currentExecutionTime >= maxRunTimeInMili;
    }
    private final boolean isMaxFitted(int maxFitnessInPopulation) {
        System.out.printf("max fittness can be is: " + maxFitnessCanBe());
        return maxFitnessInPopulation >= maxFitnessCanBe();
    }
    private final int maxFitnessCanBe() {
        return 3 * (numberOfDays * numberOfShifts * necessaryWorkers);
    }
}
