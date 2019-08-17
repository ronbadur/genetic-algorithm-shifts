package final_project.benchmark;

import final_project.common.AlgorithmRunner;
import final_project.common.AlgorithmScorer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteForceBinaryAlgorithm implements AlgorithmRunner {

	@Override
	public int[][][] run(int[][][] constraints, int numberOfWorkersInShift) {
		int workerNumber = constraints.length;
		int dayNumber = constraints[0].length;
		int shiftNumber = constraints[0][0].length;

		AlgorithmScorer algorithmScorer = new AlgorithmScorer();

		int tryNumber = (workerNumber * dayNumber * shiftNumber);
		int numberOfPossibilities = (int) Math.pow(2, (workerNumber * dayNumber * shiftNumber));

		List<int[]> solutions = calcBruteForce(numberOfPossibilities, tryNumber);

		double currScore = 0;
		double maxScore = -99999;
		int[][][] maxSolution = null;

		for (int i = 0; i < solutions.size(); i++) {
			int[][][] solutionsIn3D = calcSolutionIn3D(solutions.get(i), workerNumber, dayNumber, shiftNumber);
			currScore = algorithmScorer.score(constraints, solutionsIn3D);

			if (currScore >= maxScore) {
				maxScore = currScore;

				maxSolution = solutionsIn3D;
			}
		}

		return maxSolution;
	}

	private int[][][] calcSolutionIn3D(int[] solution, int workerNumber, int dayNumber, int shiftNumber) {
		int counter = 0;
		int[][][] result = new int[workerNumber][dayNumber][shiftNumber];

		for (int workerIndex = 0; workerIndex < workerNumber; workerIndex++) {
			for (int dayIndex = 0; dayIndex < dayNumber; dayIndex++) {
				for (int shiftIndex = 0; shiftIndex < shiftNumber; shiftIndex++) {
					int a = solution[counter];
					result[workerIndex][dayIndex][shiftIndex] = a;
					counter++;
				}
			}
		}
		return result;
	}

	public static List<int[]> calcBruteForce(int numberOfPossibilities, int tryNumber) {
		List<int[]> solutions = new ArrayList<>();

		for (int i = 0; i < numberOfPossibilities; i++) {
			String currentSolution = String.format("%" + tryNumber + "s", Integer.toBinaryString(i)).replace(' ', '0');
			String[] currentArray = currentSolution.split("");
			solutions.add(Arrays.stream(currentArray).mapToInt(Integer::parseInt).toArray());
		}

		return solutions;
	}
}
