package final_project.benchmark;

import final_project.common.AlgorithmScorer;
import final_project.common.DynamicAlgorithmRunner;

import java.util.Random;

/**
 *
 */
class Benchmark {
	public static void main(String[] args) {
		int[] CONSTRAINTS_OPTIONS = {1, 10, 100, 1000};
		int NUM_OF_WORKERS = 4;
		int NUM_OF_DAYS = 2;
		int NUM_OF_SHIFTS = 3;
		int NUM_OF_WORKERS_IN_SHIFT = 2;

		Random rn = new Random();

		int[][][] constraints = getRandomConstraints(NUM_OF_WORKERS, NUM_OF_DAYS, NUM_OF_SHIFTS, CONSTRAINTS_OPTIONS, rn);

		System.out.println("Ratio: " + getScoreRatioToOptimal(constraints, NUM_OF_WORKERS_IN_SHIFT));
	}

	static double getScoreRatioToOptimal(int[][][] constraints, int numberOfNecessaryWorkers) {
		int[][][] dynamicResult = new DynamicAlgorithmRunner().run(constraints, numberOfNecessaryWorkers);
		int[][][] bruteForceResult = new BruteForceBinaryAlgorithm().run(constraints, numberOfNecessaryWorkers);

		AlgorithmScorer algorithmScorer = new AlgorithmScorer();

		double dynamicSolutionScore = algorithmScorer.score(constraints, dynamicResult);
		System.out.println("Dynamic Score: " + dynamicSolutionScore);

		double optimalSolutionScore = algorithmScorer.score(constraints, bruteForceResult);
		System.out.println("Optimal Score: " + optimalSolutionScore);

		return dynamicSolutionScore / optimalSolutionScore;
	}

	private static int[][][] getRandomConstraints(int NUM_OF_WORKERS, int NUM_OF_DAYS, int NUM_OF_SHIFTS, int[] CONSTRAINTS_OPTIONS, Random rn) {
		int[][][] constraints = new int[NUM_OF_WORKERS][NUM_OF_DAYS][NUM_OF_SHIFTS];

		// Initialize all to Available
		for (int dayIndex = 0; dayIndex < NUM_OF_DAYS; dayIndex++) {
			for (int shiftIndex = 0; shiftIndex < NUM_OF_SHIFTS; shiftIndex++) {
				for (int workerIndex = 0; workerIndex < NUM_OF_WORKERS; workerIndex++) {
					constraints[workerIndex][dayIndex][shiftIndex] = CONSTRAINTS_OPTIONS[3];
				}
			}
		}

		// Insert random constraints
		for (int dayIndex = 0; dayIndex < NUM_OF_DAYS; dayIndex++) {
			for (int shiftIndex = 0; shiftIndex < NUM_OF_SHIFTS; shiftIndex++) {
				for (int workerIndex = 0; workerIndex < NUM_OF_WORKERS; workerIndex++) {

					if (canWorkerHaveConstraintThatShift(workerIndex, shiftIndex, dayIndex, NUM_OF_SHIFTS)) {
						constraints[workerIndex][dayIndex][shiftIndex] = CONSTRAINTS_OPTIONS[rn.nextInt(4)];
					}
				}
			}
		}

		return constraints;
	}

	static boolean canWorkerHaveConstraintThatShift(int workerIndex, int shift, int day, int numberOfShifts) {
		boolean result;

		result = ((numberOfShifts * day) + shift + workerIndex) % 2 == 0;

		return result;
	}
}