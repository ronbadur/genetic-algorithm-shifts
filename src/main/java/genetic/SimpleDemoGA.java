package genetic;

import genetic.entities.Constraint;
import genetic.entities.Individual;
import genetic.entities.Population;
import common.ConstraintEnum;

import java.util.Random;


/**
 *
 * @author Vijini
 */


//Main class
public class SimpleDemoGA {
//    private Population population = new Population();
//    private Population newPopulation = new Population();
//    private int generationCount = 0;

    public static void main(String[] args) {

        int populationSize = 50;
        int[][][] manualConstraints = {
                {
                        {ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.NotAvailable.getValue()},
                        {ConstraintEnum.NotAvailable.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue()}
                },
                {
                        {ConstraintEnum.Available.getValue(), ConstraintEnum.NotAvailable.getValue(), ConstraintEnum.NotAvailable.getValue()},
                        {ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.NotAvailable.getValue()}
                },
                {
                        {ConstraintEnum.NotAvailable.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue()},
                        {ConstraintEnum.NotAvailable.getValue(), ConstraintEnum.NotAvailable.getValue(), ConstraintEnum.NotAvailable.getValue()}
                },
                {
                        {ConstraintEnum.NotAvailable.getValue(), ConstraintEnum.NotAvailable.getValue(), ConstraintEnum.Available.getValue()},
                        {ConstraintEnum.Available.getValue(), ConstraintEnum.NotAvailable.getValue(), ConstraintEnum.Available.getValue()}
                }
        };

        genetic.algorithm.GeneticAlgorithm genAlgo = new genetic.algorithm.GeneticAlgorithm(manualConstraints, 2);

        Individual.printMatrix(genAlgo.scheduleShifts());
    }
}


