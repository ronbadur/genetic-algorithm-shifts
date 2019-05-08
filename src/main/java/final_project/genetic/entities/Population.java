package final_project.genetic.entities;

import org.springframework.beans.BeanUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Population {

    private int numberOfWorkers;
    private int numberOfDays;
    private int numberOfShifts;
    private int necessaryWorkers;
    private static Random rn;
    public TreeSet<Individual> individuals;

    // Before checking this values, you need to run calculateFitness
    public int fittest = 0;
    public int populationTotalFitness = 0;


    public Population(int numberOfWorkers, int numberOfDays, int numberOfShifts, int necessaryWorkers) {
        this.numberOfWorkers = numberOfWorkers;
        this.numberOfDays = numberOfDays;
        this.numberOfShifts = numberOfShifts;
        this.necessaryWorkers = necessaryWorkers;
        this.rn = new Random();

        individuals = new TreeSet<>((Individual individual1, Individual individual2) -> {
                if (individual1.fitness < individual2.fitness) {
                    return 1;
                } else if (Arrays.deepEquals(individual1.genes, individual2.genes)) {
                    return 0;
                } else {
                    return -1;
                }
        });
    }

    //Initialize population
    public void add(Population population) {
        individuals.addAll(population.individuals);
    }

    public void initializePopulation(int[][][] initialChromosome, int size) throws Exception {
        if (initialChromosome == null) {throw new Exception("initialChromosome can't be null");}
        if (size < 1) { throw new Exception("size of population can't be less than 1");}

        Individual initialIndividual = new Individual(initialChromosome, necessaryWorkers);

        // Add the first chromosome
        individuals.add(initialIndividual);

        // Mash i
        for (int i = 1; i < size; i++) {
            int[][][] newGenes = switchGenes(initialIndividual.genes);
            Individual newIndividual = new Individual(newGenes, necessaryWorkers);
            individuals.add(newIndividual);
        }
    }

    public void initializePopulation(int size) {
        for (int i = 0; i < size; i++) {
            individuals.add(new Individual(numberOfWorkers, numberOfDays, numberOfShifts, necessaryWorkers));
        }
    }

    public void add(Individual individual) {
        individuals.add(individual);
    }

    public void add(ArrayList<Individual> gotIndividuals) {
        for (Individual currIndi: gotIndividuals) {
            individuals.add(currIndi);
        }
    }
    //Get the fittest individual
    public Individual getFittest() {
        int maxFit = Integer.MIN_VALUE;
        Individual maxFitIndividual = individuals.iterator().next();

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

    public TreeSet<Individual> getMostFit(int numOfElementsToPull) {
        int counter = 0;
        TreeSet result = new TreeSet<>((Individual individual1, Individual individual2) -> {
            if (individual1.fitness < individual2.fitness) {
                return 1;
            } else if (Arrays.deepEquals(individual1.genes, individual2.genes)) {
                return 0;
            } else {
                return -1;
            }
        });

        for (Iterator<Individual> iter = individuals.iterator(); iter.hasNext() && counter <= numOfElementsToPull; ) {
            Individual element = iter.next();
            counter++;

            result.add(element);
        }

        return result;
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

            AtomicInteger index = new AtomicInteger();
            individuals.forEach(individual -> {
                try {
                  index.incrementAndGet();
                    writer.write(individual.getPrintableObject() + "\n");
                    writer.write("index: " + (index.get()) + " fitness: " + individual.fitness + "\n\n");
                } catch (IOException e) {
                    System.out.printf(e.getMessage());
                }
            });
        } catch (IOException e) {
            System.out.printf(e.getMessage());
        }

    }

    private int[][][] switchGenes(int[][][] genes) {
        int[][][] newGenes = new int[this.numberOfWorkers][this.numberOfDays][this.numberOfShifts];

        // Clone
        for (int worker = 0; worker < genes.length; worker++) {
            for (int day = 0; day < genes[worker].length; day++) {
                for (int shift = 0; shift < genes[worker][day].length; shift++) {
                    newGenes[worker][day][shift] = genes[worker][day][shift];
                }
            }
        }

        // Add -1 is for including Zero
        final int MAX_POINT = (numberOfWorkers * numberOfDays * numberOfShifts) - 1;
        int switchBitsFirstPoint = this.rn.nextInt(MAX_POINT);
        int switchBitsSecondPoint = this.rn.nextInt(MAX_POINT - switchBitsFirstPoint) + switchBitsFirstPoint;

        // Calc first point
        int workerPoint1 = switchBitsFirstPoint / (numberOfShifts * numberOfDays);
        int dayPoint1 = (switchBitsFirstPoint % (numberOfShifts * numberOfDays)) / numberOfShifts;
        int shiftPoint1 = switchBitsFirstPoint % numberOfShifts;

        // Calc second point
        int workerPoint2 = switchBitsSecondPoint / (numberOfShifts * numberOfDays);
        int dayPoint2 = (switchBitsSecondPoint % (numberOfShifts * numberOfDays)) / numberOfShifts;
        int shiftPoint2 = switchBitsSecondPoint % numberOfShifts;

        int temp = newGenes[workerPoint1][dayPoint1][shiftPoint1];
        newGenes[workerPoint1][dayPoint1][shiftPoint1] = newGenes[workerPoint2][dayPoint2][shiftPoint2];
        newGenes[workerPoint2][dayPoint2][shiftPoint2] = temp;

        return newGenes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Population that = (Population) o;
        boolean result = individuals.containsAll(that.individuals) && that.individuals.containsAll(individuals);

        return result;
    }

    @Override
    public Object clone() {
        Population clonedPopulation = new Population(numberOfWorkers, numberOfDays, numberOfShifts, necessaryWorkers);
        for (Individual item : individuals) {
            clonedPopulation.add(item.clone());
        }
        return clonedPopulation;
    }

    @Override
    public int hashCode() {

        return Objects.hash(individuals);
    }
}