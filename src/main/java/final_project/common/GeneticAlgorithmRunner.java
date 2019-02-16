package final_project.common;

import final_project.genetic.algorithm.GeneticAlgorithm;

public class GeneticAlgorithmRunner implements AlgorithmRunner {
	@Override
	public int[][][] run(int[][][] constraints, int numberOfWorkersInShift) {
		GeneticAlgorithm genAlgo = new GeneticAlgorithm(constraints, numberOfWorkersInShift);

		return genAlgo.scheduleShifts();
	}
}