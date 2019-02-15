package genetic.entities;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Population {

    public List<Individual> individuals;

    // Before checking this values, you need to run calculateFitness
    public int fittest = 0;
    public int populationTotalFitness = 0;

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

    public void printToFileWithFitness(int generationNum){
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("GenerationFiles/Generation-" + generationNum + ".txt"), "utf-8"))) {

            individuals.forEach(individual -> {
                try {
                    writer.write(individual.getPrintableObject() + "\n");
                    writer.write("fitness: " + individual.fitness + "\n");
                } catch (IOException e) {
                    System.out.printf(e.getMessage());
                }
            });
        } catch (IOException e) {
            System.out.printf(e.getMessage());
        }

    }
}