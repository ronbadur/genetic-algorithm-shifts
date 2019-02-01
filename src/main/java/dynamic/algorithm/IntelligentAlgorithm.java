package dynamic.algorithm;

import dynamic.entities.Knapsack.KnapsackItem;

public class IntelligentAlgorithm {
	private int knapsackWeight = 50;

	//	public void scheduleShifts(List<Worker> workers, List<Shift> shifts) {
	public int[][][] scheduleShifts() {
		int numNurses = 5;
		int numShifts = 3;
		int numDays = 7;
		int minimumShiftsForWorker = 1;

		int[][][] shiftRequests = {
				{{0, 0, 1}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 1}, {0, 1, 0}, {0, 0, 1}},
				{{0, 0, 0}, {0, 0, 0}, {0, 1, 0}, {0, 1, 0}, {1, 0, 0}, {0, 0, 0}, {0, 0, 1}},
				{{0, 1, 0}, {0, 1, 0}, {0, 0, 0}, {1, 0, 0}, {0, 0, 0}, {0, 1, 0}, {0, 0, 0}},
				{{0, 0, 1}, {0, 0, 0}, {1, 0, 0}, {0, 1, 0}, {0, 0, 0}, {1, 0, 0}, {0, 0, 0}},
				{{0, 0, 0}, {0, 0, 1}, {0, 1, 0}, {0, 0, 0}, {1, 0, 0}, {0, 1, 0}, {0, 0, 0}}
		};

		KnapsackItem[][][] valueWeightShifts = this.requestsToValueWeightMapper(shiftRequests);

		return this.resolveManning(valueWeightShifts);

//		int[][] possibleManning =
//				{{2, 2, 2}, {2, 2, 2}, {2, 2, 2}, {2, 2, 2}, {2, 2, 2}, {2, 2, 2}, {2, 2, 2}};

		// Each shift will spectate on the next one, and weight the current constraint accordingly.
		// In this case we get
		//
		// We might also set the shift weight according to the future possible shifts of a nurse

//		int val[] = new int[]{60, 100, 120};
//		int wt[] = new int[]{10, 20, 30};
//		int W = 50;
//		int n = val.length;
//		System.out.println(Knapsack.solve(W, wt, val, n));
	}

	private KnapsackItem[][][] requestsToValueWeightMapper(int[][][] shiftRequests) {
		KnapsackItem[][][] valueWeightMatrix = this.getInitializedValueWeightMatrix();
		int workerShiftWeight;

		for (int i = 0; i < shiftRequests.length; i++) {
			for (int j = 0; j < shiftRequests[i].length; j++) {
				for (int k = 0; k < shiftRequests[i][j].length; k++) {
					workerShiftWeight = this.calculateWorkerShiftsWeight(shiftRequests[i], j, k);

					valueWeightMatrix[i][j][k] = new KnapsackItem(shiftRequests[i][j][k], workerShiftWeight);
				}
			}
		}

		return valueWeightMatrix;
	}

	private int[][][] resolveManning(KnapsackItem[][][] valueWeightShifts) {
		int[][][] manning = new int[5][7][3];

		for (int i = 0; i < valueWeightShifts.length; i++) {
			for (int j = 0; j < valueWeightShifts[i].length; j++) {
				for (int k = 0; k < valueWeightShifts[i][j].length; k++) {
					// @TODO: Resolve
				}
			}
		}

		return manning;
	}

	private int calculateWorkerShiftsWeight(int[][] workerPreferences, int startingFromDay, int startingFromShift) {
		int counter = 0;

		for (int j = startingFromDay; j < workerPreferences.length; j++) {
			for (int k = startingFromShift; k < workerPreferences.length; k++) {
				if (workerPreferences[j][k] <= 10) {
					counter++;
				}
			}
		}

		return counter;
	}

	private KnapsackItem[][][] getInitializedValueWeightMatrix() {
		KnapsackItem[][][] valueWeightMatrix = new KnapsackItem[5][7][3];

		for (int i = 0; i < valueWeightMatrix.length; i++) {
			for (int j = 0; j < valueWeightMatrix[i].length; j++) {
				for (int k = 0; k < valueWeightMatrix[i][j].length; k++) {
					valueWeightMatrix[i][j][k] = new KnapsackItem(0, 0);
				}
			}
		}

		return valueWeightMatrix;
	}
}
