package genetic;

import genetic.entities.Constraint;
import genetic.entities.GenAlgoUtilities;
import genetic.entities.Individual;
import genetic.entities.Population;

import java.util.Random;

import static genetic.entities.GenAlgoUtilities.*;
import static common.ConstraintEnum.*;


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

        demo.population.printToFileWithFitness(0);
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
            }
            newPopulation.add(first);
            newPopulation.add(second);
        }
    }
}


