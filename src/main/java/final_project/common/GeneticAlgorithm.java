package final_project.common;

import final_project.genetic.entities.Constraint;
import final_project.genetic.entities.Population;

import java.util.Random;

public class GeneticAlgorithm implements AlgorithmRunner {

    @Override
    public int[][][] run(int[][][] constraints, int numberOfWorkersInShift) {
        Random rn = new Random();
        Constraint algorithmConstraints = new Constraint(constraints);
        Population population = new Population();
        Population newPopulation = new Population();
        int generationCount = 0;

        return new int[0][][];
    }
}
