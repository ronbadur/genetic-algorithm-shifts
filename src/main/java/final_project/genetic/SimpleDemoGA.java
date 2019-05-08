package final_project.genetic;

import final_project.genetic.algorithm.GeneticAlgorithm;
import final_project.genetic.entities.Constraint;
import final_project.genetic.entities.Individual;
import final_project.genetic.entities.Population;
import final_project.common.ConstraintEnum;

import java.util.Random;


/**
 * @author Vijini
 */


//final_project.api.Application class
public class SimpleDemoGA {
//    private Population population = new Population();
//    private Population newPopulation = new Population();
//    private int generationCount = 0;

	public static void main(String[] args) {

		int populationSize = 50;
		int[][][] manualConstraints = {
				{
						{ConstraintEnum.Available.getValue(), ConstraintEnum.NotAvailable.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue()},
						{ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue()}
				},
				{
						{ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue()},
						{ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.NotAvailable.getValue(), ConstraintEnum.Available.getValue()}
				},
				{
						{ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue()},
						{ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue()}
				},
				{
						{ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue()},
						{ConstraintEnum.Available.getValue(), ConstraintEnum.NotAvailable.getValue(), ConstraintEnum.Available.getValue(), ConstraintEnum.Available.getValue()}
				},

		};

		GeneticAlgorithm genAlgo = new GeneticAlgorithm(manualConstraints, 2);

		Individual.printMatrix(genAlgo.scheduleShifts());
	}
}


