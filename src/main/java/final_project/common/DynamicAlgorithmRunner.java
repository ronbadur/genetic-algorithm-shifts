package final_project.common;

import final_project.dynamic.algorithm.IntelligentAlgorithm;

public class DynamicAlgorithmRunner implements AlgorithmRunner {
	@Override
	public int[][][] run(int[][][] constraints, int numberOfWorkersInShift) {
		return new IntelligentAlgorithm(constraints, numberOfWorkersInShift).scheduleShifts();
	}
}
