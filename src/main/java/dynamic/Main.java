package dynamic;

import com.sun.javaws.exceptions.InvalidArgumentException;
import dynamic.algorithm.IntelligentAlgorithm;

import java.util.Random;

public class Main {
	public static void main(String[] args) {
		int numOfWorkersPerShift = 2;
		int numOfWorkers = 4;

		if (numOfWorkers / 2 < numOfWorkersPerShift) {
			throw new RuntimeException("It won't work");
		}

		IntelligentAlgorithm ia = new IntelligentAlgorithm(generateRandomShiftRequests(numOfWorkers, 7, 4), numOfWorkersPerShift);

		ia.scheduleShifts();
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
