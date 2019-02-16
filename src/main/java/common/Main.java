package common;

import java.util.Random;

public class Main {
	private static AlgorithmComparison algorithmComparison = new AlgorithmComparison();

	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			ComparisonResult comparisonResult = algorithmComparison.compare(generateRandomShiftRequests(8, 5, 3), 3);

			System.out.println("Result of round: " + (i + 1));
			System.out.println(comparisonResult.toString());
		}
	}

	private static int[][][] generateRandomShiftRequests(int workers, int days, int shifts) {
		int[][][] shiftRequestsMatrix = new int[workers][days][shifts];

		for (int i = 0; i < shiftRequestsMatrix.length; i++) {
			for (int j = 0; j < shiftRequestsMatrix[i].length; j++) {
				for (int k = 0; k < shiftRequestsMatrix[i][j].length; k++) {
					shiftRequestsMatrix[i][j][k] = randomizeConstraint();
				}
			}
		}

		return shiftRequestsMatrix;
	}

	private static int randomizeConstraint() {
		int n = new Random().nextInt(4);

		return (int) Math.pow(10, n);
	}
}
