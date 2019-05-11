package final_project.benchmark;

import final_project.common.BruteForceAlgorithmRunner;
import final_project.genetic.entities.Constraint;

import java.util.Random;

/**
 *
 */
class Benchmark {


    public static void main(String[] args) {
        int[] CONSTRAINTS_OPTIONS = {1, 10, 100, 1000};
        int NUM_OF_WORKERS = 3;
        int NUM_OF_DAYS = 3;
        int NUM_OF_SHIFTS = 3;
        int NUM_OF_WORKERS_IN_SHIFT = 1;

        Random rn = new Random();

        int[][][] constraints = getRandomConstraints(NUM_OF_WORKERS, NUM_OF_DAYS, NUM_OF_SHIFTS, CONSTRAINTS_OPTIONS, rn);

        Constraint constraintsObj = new Constraint(constraints);
        constraintsObj.print();

        int[][][] bestResult = new BruteForceAlgorithmRunner().run(constraints, NUM_OF_WORKERS_IN_SHIFT);

    //    Constraint bestResultsObj = new Constraint(bestResult);
      //  bestResultsObj.print();
    }

    private static int[][][] getRandomConstraints(int NUM_OF_WORKERS, int NUM_OF_DAYS, int NUM_OF_SHIFTS,int[] CONSTRAINTS_OPTIONS, Random rn) {
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