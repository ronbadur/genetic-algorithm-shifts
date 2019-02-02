package dynamic.algorithm;

import dynamic.entities.Knapsack.KnapsackItem;

import java.util.Arrays;
import java.util.Random;

public class IntelligentAlgorithm {
    private final int HEAVY_CONSTRAINT = 1;
    private final int MEDIUM_CONSTRAINT = 10;
    private final int LIGHT_CONSTRAINT = 100;
    private final int NO_CONSTRAINT = 1000;

    private final int MAX_WEIGHT = 10;
    private final int WORKERS_PER_SHIFT = 2;


    //	public void scheduleShifts(List<Worker> workers, List<Shift> shifts) {
    public int[][][] scheduleShifts() {
        int numNurses = 5;
        int numShifts = 3;
        int numDays = 7;
        int minimumShiftsForWorker = 1;


//        int[][][] shiftRequests = {
//                {{0, 0, 1}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 1}, {0, 1, 0}, {0, 0, 1}},
//                {{0, 0, 0}, {0, 0, 0}, {0, 1, 0}, {0, 1, 0}, {1, 0, 0}, {0, 0, 0}, {0, 0, 1}},
//                {{0, 1, 0}, {0, 1, 0}, {0, 0, 0}, {1, 0, 0}, {0, 0, 0}, {0, 1, 0}, {0, 0, 0}},
//                {{0, 0, 1}, {0, 0, 0}, {1, 0, 0}, {0, 1, 0}, {0, 0, 0}, {1, 0, 0}, {0, 0, 0}},
//                {{0, 0, 0}, {0, 0, 1}, {0, 1, 0}, {0, 0, 0}, {1, 0, 0}, {0, 1, 0}, {0, 0, 0}}
//        };

        int[][][] shiftRequests = this.generateRandomShiftRequests();
        this.print3dMatrix(shiftRequests);

        KnapsackItem[][][] valueWeightShifts = this.requestsToValueWeightMapper(shiftRequests);
        return this.resolveManning(valueWeightShifts);
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

        for (int j = 0; j < valueWeightShifts[0].length; j++) { // day
            for (int k = 0; k < valueWeightShifts[0][j].length; k++) { // shift

                KnapsackItem[] input = new KnapsackItem[valueWeightShifts.length];

                for (int worker = 0; worker < valueWeightShifts.length; worker++) {
                    input[worker] = valueWeightShifts[worker][j][k];
                }


                int[] visited = new int[input.length];

                Knapsack.solve(this.MAX_WEIGHT, input, visited);


                int workersInShift = Arrays.stream(visited).sum();

                while(workersInShift != this.WORKERS_PER_SHIFT) {
                    if (workersInShift > this.WORKERS_PER_SHIFT) {
                        int workersToRemove = workersInShift - this.WORKERS_PER_SHIFT;

                        for (int i = 0; i < workersToRemove; i++) {
                            int minValue = Integer.MAX_VALUE;
                            int minIndex = -1;
                            for (int worker = 0; worker < input.length; worker++) {
                                if(visited[worker] == 1 && minValue > input[worker].getValue()) {
                                    minValue = input[worker].getValue();
                                    minIndex = worker;
                                }
                            }

                            visited[minIndex] = 0;
                        }

                        workersInShift = Arrays.stream(visited).sum();
                    } else {
                        Arrays.stream(input).forEach(x -> x.setWeight((x.getWeight() - 1)));

                        visited = new int[input.length];

                        Knapsack.solve(this.MAX_WEIGHT, input, visited);

                        workersInShift = Arrays.stream(visited).sum();
                    }
                }

                System.out.println(Arrays.toString(visited));
                System.out.println("---");


                for (int i = 0; i < visited.length; i++) {
                    if (visited[i] == 1) {

                        int shift = (k + 1) % valueWeightShifts[0][0].length;
                        int day = j;
                        if (shift == 0 && j < valueWeightShifts[0].length - 1) {
                            day++;
                        }


                        (valueWeightShifts[i][day][shift]).setWeight(this.MAX_WEIGHT + 1);
                    }
                }

            }
        }

        return manning;
    }

    private int calculateWorkerShiftsWeight(int[][] workerPreferences, int startingFromDay, int startingFromShift) {
        int counter = 0;
        for (int k = startingFromShift; k < workerPreferences[0].length; k++) {
            if (workerPreferences[startingFromDay][k] >= this.LIGHT_CONSTRAINT) {
                counter++;
            }
        }

        for (int j = startingFromDay + 1; j < workerPreferences.length; j++) {
            for (int k = 0; k < workerPreferences[j].length; k++) {
                if (workerPreferences[j][k] >= this.LIGHT_CONSTRAINT) {
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

    private int[][][] generateRandomShiftRequests() {
        int[][][] shiftRequestsMatrix = new int[5][7][3];

        for (int i = 0; i < shiftRequestsMatrix.length; i++) {
            for (int j = 0; j < shiftRequestsMatrix[i].length; j++) {
                for (int k = 0; k < shiftRequestsMatrix[i][j].length; k++) {
                    shiftRequestsMatrix[i][j][k] = this.randomizeConstraint();
                }
            }
        }

        return shiftRequestsMatrix;
    }

    private int randomizeConstraint() {
        int n = new Random().nextInt(4);

        return (int)Math.pow(10, n);
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
