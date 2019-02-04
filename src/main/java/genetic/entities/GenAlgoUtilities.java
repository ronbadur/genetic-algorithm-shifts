package genetic.entities;

public class GenAlgoUtilities {

   static final public int WORKERS_IN_SINGLE_SHIFT = 2;
   static final public int NUM_OF_WORKERS = 4;
   static final public int NUM_OF_DAYS = 2;
   static final public int NUM_OF_SHIFTS = 3;

   public static int maxFitnessCanBe() {
         return NUM_OF_DAYS * NUM_OF_SHIFTS * WORKERS_IN_SINGLE_SHIFT;
     }
}