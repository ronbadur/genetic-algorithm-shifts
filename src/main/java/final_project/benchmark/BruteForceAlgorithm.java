package final_project.benchmark;

import final_project.common.Algorithm;
import final_project.common.AlgorithmScorer;
import final_project.common.ConstraintEnum;
import final_project.genetic.entities.Individual;

import java.util.ArrayList;
import java.util.Iterator;

public class BruteForceAlgorithm implements Algorithm {

    private final int[][][] shiftRequests;
    private final int necessaryWorkers;
    private final int numberOfWorkers;
    private final int numberOfDays;
    private final int numberOfShifts;

    public BruteForceAlgorithm(int[][][] shiftRequests, int necessaryWorkers) {
        this.shiftRequests = shiftRequests;
        this.necessaryWorkers = necessaryWorkers;

        this.numberOfWorkers = shiftRequests.length;
        this.numberOfDays = shiftRequests[0].length;
        this.numberOfShifts = shiftRequests[0][0].length;
    }

    @Override
    public int[][][] scheduleShifts() {
        Individual.printMatrix(this.shiftRequests);
        FilePrinter.getInstance().print(this.shiftRequests, "Constraint");

        int[][][] result = new int[this.numberOfWorkers][this.numberOfDays][this.numberOfShifts];
        ArrayList onesArray = new ArrayList();

        for (int dayIndex = 0; dayIndex < this.numberOfDays; dayIndex++) {
            for (int shiftIndex = 0; shiftIndex < this.numberOfShifts; shiftIndex++) {
                for (int workerIndex = 0; workerIndex < this.numberOfWorkers; workerIndex++) {
                    if (this.shiftRequests[workerIndex][dayIndex][shiftIndex] == ConstraintEnum.Available.getValue()) {
                        result[workerIndex][dayIndex][shiftIndex] = 1;
                        onesArray.add(new DigitSpot(workerIndex, dayIndex, shiftIndex));
                    }
                }
            }
        }

        System.out.println("---------num of 1's---------");
        System.out.println("number: " + onesArray.size());

        // combinations of 1's that can be in result !
        int numOfShifts = this.numberOfDays * this.numberOfShifts;
        ArrayList<ArrayList<DigitSpot>> combinations = getCombinations(onesArray, 0, 0, numOfShifts, new ArrayList<ArrayList<DigitSpot>>(), new ArrayList<>());
        ArrayList<int[][][]> bruteforceOptionalResults = new ArrayList<>();

        for (ArrayList<DigitSpot> currCombi: combinations) {
            int[][][] currOptionalResult = new int[this.numberOfWorkers][this.numberOfDays][this.numberOfShifts];

            for (DigitSpot currSpot: currCombi) {
                currOptionalResult[currSpot.workerIndex][currSpot.dayIndex][currSpot.shiftIndex] = 1;
            }
            bruteforceOptionalResults.add(currOptionalResult);
        }

        FilePrinter.getInstance().print(bruteforceOptionalResults);

        int[][][] bestSolution = getBestSolution(bruteforceOptionalResults, this.shiftRequests);

     //   FilePrinter.getInstance().print(bestSolution, "Solution");

        // TODO: run on all the results, and see who is the best !

        return null;
    }

    public int[][][] getBestSolution(ArrayList<int[][][]> optionalSolutions, int[][][] constraints) {
        AlgorithmScorer algorithmScorer =  new AlgorithmScorer();
        int[][][] bestSolution = null;
        double bestScore = Double.MIN_VALUE;
        double currScore;

        for (int[][][] currOptionalSolution : optionalSolutions) {

            currScore = algorithmScorer.score(constraints, currOptionalSolution);
            System.out.println("currScore: " + currScore);
            if (bestScore <= currScore) {
                bestScore = currScore;
                bestSolution = currOptionalSolution;
            }
        }

        return bestSolution;
    }

    public ArrayList<ArrayList<DigitSpot>> getCombinations(ArrayList<DigitSpot> digitSpots,
                                         int i, int x, int numOfShifts,
                                         ArrayList<ArrayList<DigitSpot>> combinations, ArrayList<DigitSpot> newCombination) {

        if (newCombination.size() < numOfShifts) {
            for (int j = x; j < digitSpots.size(); j++) {
                //System.out.println("i: " +  i+ ", j: " + j + ", x: " + x);
                //System.out.println(digitSpots.get(j).toString());

                newCombination.add(digitSpots.get(j));
                getCombinations(digitSpots, i + 1, j + 1, numOfShifts, combinations, cloneArrayList(newCombination));
                newCombination.clear();
            }


        } else {   // print combination
            combinations.add(newCombination);

        }

        return combinations;
    }

    public ArrayList<DigitSpot> cloneArrayList(ArrayList<DigitSpot> list){
        ArrayList<DigitSpot> clonedList = new ArrayList<>();

        Iterator<DigitSpot> iterator = list.iterator();

        while(iterator.hasNext())
        {
            //Add the object clones
            clonedList.add((DigitSpot) iterator.next().clone());
        }

        return clonedList;
    }

    class DigitSpot {
        private final int workerIndex;
        private final int dayIndex;
        private final int shiftIndex;

        DigitSpot(int workerIndex, int dayIndex, int shiftIndex){
            this.workerIndex = workerIndex;
            this.dayIndex = dayIndex;
            this.shiftIndex = shiftIndex;
        }

        public int getDayIndex() {
            return dayIndex;
        }

        public int getShiftIndex() {
            return shiftIndex;
        }

        public int getWorkerIndex() {
            return workerIndex;
        }

        @Override
        public String toString() {
            return "workerIndex: " + this.workerIndex + ", " +
                    "dayIndex: " + this.dayIndex + ", " +
                    "shiftIndex: " + this.shiftIndex;
        }

        @Override
        protected Object clone() {
            return new DigitSpot(this.workerIndex, this.dayIndex, this.shiftIndex);
        }
    }
}
