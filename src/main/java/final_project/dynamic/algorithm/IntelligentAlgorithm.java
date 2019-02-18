package final_project.dynamic.algorithm;

import final_project.common.Algorithm;
import final_project.dynamic.entities.Knapsack.KnapsackItem;

import java.util.Arrays;

public class IntelligentAlgorithm implements Algorithm {
	private final int HEAVY_CONSTRAINT = 1;
	private final int MEDIUM_CONSTRAINT = 10;
	private final int LIGHT_CONSTRAINT = 100;
	private final int NO_CONSTRAINT = 1000;
	private final int MAX_WEIGHT = 10;

	private int[][][] shiftRequests;
	private int necessaryWorkers;
	private int numberOfWorkers;
	private int numberOfDays;
	private int numberOfShifts;

	public IntelligentAlgorithm(int[][][] shiftRequests, int necessaryWorkers) {
		this.shiftRequests = shiftRequests;
		this.necessaryWorkers = necessaryWorkers;

		this.numberOfWorkers = shiftRequests.length;
		this.numberOfDays = shiftRequests[0].length;
		this.numberOfShifts = shiftRequests[0][0].length;
	}

	public int[][][] scheduleShifts() {
		KnapsackItem[][][] valueWeightShifts = this.requestsToValueWeightMapper(this.shiftRequests);

		return this.resolveManning(valueWeightShifts);
	}

	private KnapsackItem[][][] requestsToValueWeightMapper(int[][][] shiftRequests) {
		KnapsackItem[][][] valueWeightMatrix = this.getInitializedValueWeightMatrix();
		int workerShiftWeight;

		for (int i = 0; i < this.numberOfWorkers; i++) {
			for (int j = 0; j < this.numberOfDays; j++) {
				for (int k = 0; k < this.numberOfShifts; k++) {
					workerShiftWeight = this.calculateWorkerShiftsWeight(shiftRequests[i], j, k);

					valueWeightMatrix[i][j][k] = new KnapsackItem(shiftRequests[i][j][k], workerShiftWeight);
				}
			}
		}

		return valueWeightMatrix;
	}

	private int[][][] resolveManning(KnapsackItem[][][] valueWeightShifts) {
		int[][][] manning = new int[this.numberOfWorkers][this.numberOfDays][this.numberOfShifts];

		for (int j = 0; j < this.numberOfDays; j++) {
			for (int k = 0; k < this.numberOfShifts; k++) {
				KnapsackItem[] input = new KnapsackItem[numberOfWorkers];

				for (int worker = 0; worker < this.numberOfWorkers; worker++) {
					input[worker] = valueWeightShifts[worker][j][k];
				}

				int[] visited = new int[numberOfWorkers];

				Knapsack.solve(this.MAX_WEIGHT, input, visited);

				int workersInShift = Arrays.stream(visited).sum();

				while (workersInShift != this.necessaryWorkers) {
					if (workersInShift > this.necessaryWorkers) {
						this.removeUnnecessaryWorkers(input, visited, workersInShift);

						workersInShift = Arrays.stream(visited).sum();
					} else {
						visited = new int[numberOfWorkers];

						Arrays.stream(input)
								.filter(x -> x.getWeight() > 1)
								.forEach(x -> x.setWeight((x.getWeight() - 1)));
						Knapsack.solve(this.MAX_WEIGHT, input, visited);

						workersInShift = Arrays.stream(visited).sum();
					}
				}

				for (int i = 0; i < visited.length; i++) {
					manning[i][j][k] = visited[i];
				}

				this.makeNextShiftsImpossible(visited, k, j, valueWeightShifts);
			}
		}

		return manning;
	}

	private void removeUnnecessaryWorkers(KnapsackItem[] input, int[] visited, int workersInShift) {
		int workersToRemove = workersInShift - this.necessaryWorkers;

		for (int i = 0; i < workersToRemove; i++) {
			int minValue = Integer.MAX_VALUE;
			int minIndex = -1;
			for (int worker = 0; worker < numberOfWorkers; worker++) {
				if (visited[worker] == 1 && minValue > input[worker].getValue()) {
					minValue = input[worker].getValue();
					minIndex = worker;
				}
			}

			visited[minIndex] = 0;
		}
	}

	private int calculateWorkerShiftsWeight(int[][] workerPreferences, int startingFromDay, int startingFromShift) {
		int counter = 0;
		for (int k = startingFromShift; k < numberOfShifts; k++) {
			if (workerPreferences[startingFromDay][k] >= this.LIGHT_CONSTRAINT) {
				counter++;
			}
		}

		for (int j = startingFromDay + 1; j < numberOfDays; j++) {
			for (int k = 0; k < numberOfShifts; k++) {
				if (workerPreferences[j][k] >= this.LIGHT_CONSTRAINT) {
					counter++;
				}
			}
		}

		return counter;
	}

	private void makeNextShiftsImpossible(int[] visited, int currentShift, int day, KnapsackItem[][][] valueWeightShifts) {
		int nextShift = (currentShift + 1) % this.numberOfShifts;

		if (nextShift == 0 && day < this.numberOfDays - 1) {
			day++;
		}

		for (int i = 0; i < numberOfWorkers; i++) {
			if (visited[i] == 1) {
				(valueWeightShifts[i][day][nextShift]).setWeight(MAX_WEIGHT * numberOfWorkers + 1);
			}
		}
	}

	private KnapsackItem[][][] getInitializedValueWeightMatrix() {
		KnapsackItem[][][] valueWeightMatrix = new KnapsackItem[this.numberOfWorkers][this.numberOfDays][this.numberOfShifts];

		for (int i = 0; i < this.numberOfWorkers; i++) {
			for (int j = 0; j < this.numberOfDays; j++) {
				for (int k = 0; k < this.numberOfShifts; k++) {
					valueWeightMatrix[i][j][k] = new KnapsackItem(0, 0);
				}
			}
		}

		return valueWeightMatrix;
	}

	private void print3dMatrix(int[][][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				for (int k = 0; k < matrix[i][j].length; k++) {
					System.out.print(matrix[i][j][k]);
					System.out.print(' ');
				}
				System.out.print(", ");
			}
			System.out.println();
		}
	}
}
