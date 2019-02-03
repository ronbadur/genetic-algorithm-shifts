package genetic.entities;


import java.util.ArrayList;
import java.util.List;

public class Population {
    public List<Individual> individuals;
    public int fittest = 0;

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
        for (Individual individual : individuals) {
            individual.calcFitness(constraints);
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
