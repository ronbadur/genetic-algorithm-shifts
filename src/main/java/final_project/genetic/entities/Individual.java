package final_project.genetic.entities;

import java.util.Arrays;
import java.util.Random;

public class Individual implements Comparable {
    private int necessaryWorkers;
    private int numberOfWorkers;
    private int numberOfDays;
    private int numberOfShifts;

    public int fitness;
    public int[][][] genes;
    Random rn;

    public Individual(int[][][] initChromosome, int necessaryWorkers) {
        this.numberOfWorkers = initChromosome.length;
        this.numberOfDays = initChromosome[0].length;
        this.numberOfShifts = initChromosome[0][0].length;
        this.necessaryWorkers = necessaryWorkers;
        this.genes = new int[this.numberOfWorkers][this.numberOfDays][this.numberOfShifts];

        for (int worker = 0; worker < this.numberOfWorkers; worker++) {
            for (int day = 0; day < this.numberOfDays; day++) {
                for (int shift = 0; shift < this.numberOfShifts; shift++) {
                    this.genes[worker][day][shift] = initChromosome[worker][day][shift];
                }
            }
        }
    }

    public Individual(int numberOfWorkers, int numberOfDays, int numberOfShifts, int necessaryWorkers) {
        this.numberOfWorkers = numberOfWorkers;
        this.numberOfDays = numberOfDays;
        this.numberOfShifts = numberOfShifts;
        this.necessaryWorkers = necessaryWorkers;

        genes = new int[numberOfWorkers][numberOfDays][numberOfShifts];
        rn = new Random();

        // Set genes randomly for each individual
        for (int day = 0; day < numberOfDays; day++) {
            for (int shift = 0; shift < numberOfShifts; shift++) {
                // Who of the workers will get the shift
                int workerRandIndex;
                int[] workers = new int[numberOfWorkers];
                int counter = 0;
                // TODO: int tries =  numberOfWorkers * 2;
                while (counter < necessaryWorkers) {
                    workerRandIndex = rn.nextInt(numberOfWorkers);

                    // Just if the worker not working the shift before, assign him to the shift
                    if (isWorkingShiftBefore(workerRandIndex, day, shift)) {
                        continue;
                    } else {
                        workers[workerRandIndex] = 1;
                        counter++;
                    }
                }

                // Insert the workers
                for (int worker = 0; worker < numberOfWorkers; worker++) {
                    genes[worker][day][shift] = workers[worker];
                }
            }
        }

        fitness = 0;
    }

    private final boolean isWorkingShiftBefore(int workerIndex, int day, int shift) {
        boolean result;

        if ((day == 0) && (shift == 0)) {
            // It's the first shift
            result = false;
        } else if (shift == 0) {
            // If shift is zero go to the last shift in prev day
            result = genes[workerIndex][day - 1][numberOfShifts - 1] == 1;
        } else {
            // look at the value of prev shift
            result = genes[workerIndex][day][shift - 1] == 1;
        }

        return result;
    }

    public String getPrintableObject() {
        StringBuilder result = new StringBuilder();
        result.append("------------------\n");
        result.append(getPrintableMatrix(genes));

        return result.toString();
    }

    public void print() {
        System.out.println("------------------");
        printMatrix(genes);
    }

    public static String getPrintableMatrix(int[][][] matrix) {
        StringBuilder result = new StringBuilder();

        for (int worker = 0; worker < matrix.length; worker++) {
            result.append("man " + worker + ":");
            for (int shift = 0; shift < matrix[worker][0].length; shift++) {
                result.append("shift " + shift + "|");
            }
            result.append("\n");
            for (int day = 0; day < matrix[worker].length; day++) {
                result.append("day " + day + ";");
                for (int shift = 0; shift < matrix[worker][day].length; shift++) {
                    result.append("    " + matrix[worker][day][shift]);
                    result.append("    ");
                }
                result.append("\n");
            }
        }

        return result.toString();
    }

    public static void printMatrix(int[][][] matrix) {
        for (int worker = 0; worker < matrix.length; worker++) {
            System.out.printf("man " + worker + ":");
            for (int shift = 0; shift < matrix[worker][0].length; shift++) {
                System.out.printf("shift " + shift + "|");
            }
            System.out.println();
            for (int day = 0; day < matrix[worker].length; day++) {
                System.out.printf("day " + day + ";");
                for (int shift = 0; shift < matrix[worker][day].length; shift++) {
                    System.out.printf("%4d", matrix[worker][day][shift]);
                    System.out.printf("    ");
                }
                System.out.println();
            }
        }
    }

    //Calculate fitness
    public void calcFitness(Constraint constraints) {
        fitness = 0;

        int numOfDoubleShifts = numberOfDoubleShifts();
        int numOfUnoccupiedShifts = numOfUnoccupiedShifts();

        fitness = fitness - (numOfDoubleShifts * 20);
        fitness = fitness - (numOfUnoccupiedShifts * 30);

        for (int worker = 0; worker < genes.length; worker++) {
            for (int day = 0; day < genes[worker].length; day++) {
                for (int shift = 0; shift < genes[worker][day].length; shift++) {
                    int a = constraints.getConstraints()[worker][day][shift];
                    fitness += Math.log10(a) *
                            genes[worker][day][shift];
                }
            }
        }
    }

    /**
     * We don't want a shift without the minimum necessary workers
     *
     * @return the number of unoccupied shifts in the Individual.
     */
    private final int numOfUnoccupiedShifts() {
        int numOfShiftsThatNotOccupied = 0;

        for (int day = 0; day < genes[0].length; day++) {
            for (int shift = 0; shift < genes[0][day].length; shift++) {
                int workersInShift = 0;
                for (int worker = 0; worker < genes.length; worker++) {
                    workersInShift += genes[worker][day][shift];
                }
                if (workersInShift != this.necessaryWorkers) {
                    numOfShiftsThatNotOccupied++;
                }
            }

        }

        return numOfShiftsThatNotOccupied;
    }

    /**
     * We don't want the worker to work 2 shifts one after the other
     *
     * @return the number of double shifting in this Individual.
     */
    private final int numberOfDoubleShifts() {
        // (Used in the loop) Represent if the worker did work it the shift before
        int numOfDoubleShifts = 0;
        boolean isWorkingThePrevShift;

        for (int worker = 0; worker < genes.length; worker++) {
            // initialize for the new worker
            isWorkingThePrevShift = false;
            for (int day = 0; day < genes[0].length; day++) {
                for (int shift = 0; shift < genes[0][day].length; shift++) {
                    if (genes[worker][day][shift] == 1) {
                        // We don't want the worker to work 2 shifts one after the other
                        if (isWorkingThePrevShift) {
                            numOfDoubleShifts++;
                        } else {
                            isWorkingThePrevShift = true;
                        }
                    } else {
                        isWorkingThePrevShift = false;
                    }
                }
            }
        }
        return numOfDoubleShifts;
    }

    @Override
    public int compareTo(Object o) {
        Individual individual2 = (Individual) o;
        if (fitness > individual2.fitness) {
            return -1;
        } else if (fitness == individual2.fitness) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Individual that = (Individual) o;
        return Arrays.equals(genes, that.genes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(genes);
    }

    @Override
    public Individual clone() {
        Individual a = new Individual(numberOfWorkers, numberOfDays, numberOfShifts, necessaryWorkers);
        a.fitness = fitness;

        for (int worker = 0; worker < genes.length; worker++) {
            for (int day = 0; day < genes[worker].length; day++) {
                for (int shift = 0; shift < genes[worker][day].length; shift++) {
                    a.genes[worker][day][shift] = genes[worker][day][shift];
                }
            }
        }

        return a;
    }
}