package final_project.common;

import final_project.benchmark.BruteForceAlgorithm;

public class BruteForceAlgorithmRunner implements AlgorithmRunner {
        @Override
        public int[][][] run(int[][][] constraints, int numberOfWorkersInShift) {
            return new BruteForceAlgorithm(constraints, numberOfWorkersInShift).scheduleShifts();
        }
}
