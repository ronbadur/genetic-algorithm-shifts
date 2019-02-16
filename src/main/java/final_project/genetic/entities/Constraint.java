package final_project.genetic.entities;

import java.util.Random;

import static final_project.genetic.entities.Individual.printMatrix;

public class Constraint {

    private int[][][] constraints;
    private int necessaryWorkers;
    private int numberOfDays;
    private int numberOfShifts;

    public Constraint(int[][][] constraints) {
        this.constraints = constraints;
    }

    public Constraint(int numberOfWorkers, int numberOfDays, int numberOfShifts, int necessaryWorkers) {
        this.numberOfDays = numberOfDays;
        this.numberOfShifts = numberOfShifts;
        this.necessaryWorkers = necessaryWorkers;
        constraints = new int[numberOfWorkers][numberOfDays][numberOfShifts];
        Random rn = new Random();

        //Set genes randomly for each individual
        for (int worker = 0; worker < numberOfWorkers; worker++) {
            for (int day = 0; day < numberOfDays; day++) {
                for (int shift = 0; shift < numberOfShifts; shift++) {
                    // who of the workers will get the shift
                    int isAvailable = rn.nextInt(1);
                    constraints[worker][day][shift] = isAvailable;
                }
            }
        }
    }

    public void print() {
        System.out.println("---------constraints---------");
        printMatrix(constraints);
        System.out.println("Max Fitness Can Be: " + maxFitnessCanBe());
    }

    public int[][][] getConstraints() {
        return constraints;
    }

    private int maxFitnessCanBe() {
        return numberOfDays * numberOfShifts * necessaryWorkers;
    }
}
