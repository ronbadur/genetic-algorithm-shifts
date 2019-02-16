package common;



public class GeneticAlgorithmRunner implements AlgorithmRunner {

    @Override
    public int[][][] run(int[][][] constraints, int numberOfWorkersInShift) {
        genetic.algorithm.GeneticAlgorithm genAlgo = new genetic.algorithm.GeneticAlgorithm(constraints, numberOfWorkersInShift);

        return genAlgo.scheduleShifts();
    }
}
