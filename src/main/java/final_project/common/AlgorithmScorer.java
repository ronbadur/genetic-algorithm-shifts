package final_project.common;

public class AlgorithmScorer {
	public double score(int[][][] constraints, int[][][] solution) {
		int numberOfWorkers = solution.length;
		int numberOfDays = solution[0].length;
		int numberOfShifts = solution[0][0].length;

		double score = 0;

		if (!this.isSolutionValid(solution)) {
			return -999999999;
		}

		for (int worker = 0; worker < numberOfWorkers; worker++) {
			for (int day = 0; day < numberOfDays; day++) {
				for (int shift = 0; shift < numberOfShifts; shift++) {
					if (solution[worker][day][shift] == 1) {
						score += this.normalizeConstraint(constraints[worker][day][shift]);
					}
				}
			}
		}

		return score;
	}

	private boolean isSolutionValid(int[][][] solution) {
		int numberOfDays = solution[0].length;
		int numberOfShifts = solution[0][0].length;

		for (int[][] worker : solution) {
			for (int j = 0; j < numberOfDays; j++) {
				for (int k = 0; k < numberOfShifts; k++) {
					int nextShift = (k + 1) % numberOfShifts;
					int nextDayShift = j;

					if (nextShift == 0 && j < numberOfDays - 1) {
						nextDayShift++;
					}

					if (worker[nextDayShift][nextShift] == 1 && worker[j][k] == 1) {
						return false;
					}
				}
			}
		}

		return true;
	}

	private double normalizeConstraint(int constraint) {
		return Math.log10(constraint);
	}
}
