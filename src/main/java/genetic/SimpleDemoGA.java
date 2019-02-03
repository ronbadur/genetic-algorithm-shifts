package genetic;

import genetic.entities.Constraint;
import genetic.entities.GenAlgoUtilities;
import genetic.entities.Individual;
import genetic.entities.Population;

import java.util.Random;

import static genetic.entities.GenAlgoUtilities.*;
import static genetic.entities.ConstraintEnum.*;


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
            demo.crossover(Math.round(populationSize * 0.64f));

            //Do mutation
            demo.mutation(Math.round(populationSize * 0.28f));

            // Create new randomize
            demo.createNewRandomize(Math.round(populationSize * 0.08f));

            //Add offsprings to population
            demo.population.add(demo.newPopulation);

            //Calculate new fitness value
            demo.population.calculateFitness(constraints);
            demo.population.sort();
            demo.population.individuals = demo.population.individuals.subList(0, populationSize);

            demo.newPopulation.clear();
            System.out.println("Generation: " + demo.generationCount + " Fittest: " + demo.population.fittest);
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

            //Select a random crossover point
            int crossOverPoint = rn.nextInt((NUM_OF_WORKERS * NUM_OF_DAYS * NUM_OF_SHIFTS) - 1);

            for (int j = crossOverPoint; j < (NUM_OF_WORKERS * NUM_OF_DAYS * NUM_OF_SHIFTS); j++) {

                int worker = j / (NUM_OF_SHIFTS * NUM_OF_DAYS);
                int day = (j % (NUM_OF_SHIFTS * NUM_OF_DAYS)) / NUM_OF_SHIFTS;
                int shift = j % NUM_OF_SHIFTS;

                int temp = first.genes[worker][day][shift];
                first.genes[worker][day][shift] =
                        second.genes[worker][day][shift];
                second.genes[worker][day][shift] = temp;
            }

            newPopulation.add(first);
        }
    }

    //Mutation
    private void mutation(int count) {
        Random rn = new Random();

        //Select a random mutation point
        int mutationPointWorker = rn.nextInt(NUM_OF_WORKERS);
        int mutationPointDay = rn.nextInt(NUM_OF_DAYS);
        int mutationPointShift = rn.nextInt(NUM_OF_SHIFTS);

        Individual first, second;
        for (int i = 0; i < count - 1; i++) {
            first = population.individuals.get(i).clone();
            second = population.individuals.get(rn.nextInt(count - i) + i).clone();

            //Flip values at the mutation point
            if (first.genes[mutationPointWorker][mutationPointDay][mutationPointShift] == 0) {
                first.genes[mutationPointWorker][mutationPointDay][mutationPointShift] = 1;
            } else {
                first.genes[mutationPointWorker][mutationPointDay][mutationPointShift] = 0;
            }

            mutationPointWorker = rn.nextInt(NUM_OF_WORKERS);
            mutationPointDay = rn.nextInt(NUM_OF_DAYS);
            mutationPointShift = rn.nextInt(NUM_OF_SHIFTS);

            // Flip again
            if (second.genes[mutationPointWorker][mutationPointDay][mutationPointShift] == 0) {
                second.genes[mutationPointWorker][mutationPointDay][mutationPointShift] = 1;
            } else {
                second.genes[mutationPointWorker][mutationPointDay][mutationPointShift] = 0;
            }

            newPopulation.add(first);
        }
    }
}