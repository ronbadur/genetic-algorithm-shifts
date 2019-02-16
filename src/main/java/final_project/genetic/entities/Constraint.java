package final_project.genetic.entities;

import java.util.Random;

import static final_project.genetic.entities.GenAlgoUtilities.*;
import static final_project.genetic.entities.Individual.printMatrix;

public class Constraint {

    int[][][] constraints;

    public Constraint(int[][][] constraints) {
        this.constraints = constraints;
    }

    public Constraint() {
        constraints = new int[NUM_OF_WORKERS][NUM_OF_DAYS][NUM_OF_SHIFTS];
        Random rn = new Random();

        //Set genes randomly for each individual
        for (int worker = 0; worker < NUM_OF_DAYS; worker++) {
            for (int day = 0; day < NUM_OF_DAYS; day++) {
                for (int shift = 0; shift < NUM_OF_SHIFTS; shift++) {
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
}
