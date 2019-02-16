package common;

public class AlgorithmComparison {
	private AlgorithmRunner dynamicAlgorithm = new DynamicAlgorithmRunner();
	private AlgorithmRunner geneticAlgorithm = new GeneticAlgorithmRunner();
	private AlgorithmScorer algorithmScorer = new AlgorithmScorer();

	public ComparisonResult compare(int[][][] constraints, int numberOfWorkersInShift) {
		long startingDynamic = System.currentTimeMillis();
		int[][][] dynamicSolution = dynamicAlgorithm.run(constraints, numberOfWorkersInShift);
		long endDynamic = System.currentTimeMillis();

		long startingGenetic = System.currentTimeMillis();
		int[][][] geneticSolution = geneticAlgorithm.run(constraints, numberOfWorkersInShift);
		long endGenetic = System.currentTimeMillis();

		double dynamicScore = algorithmScorer.score(constraints, dynamicSolution);
		double geneticScore = algorithmScorer.score(constraints, geneticSolution);

		return new ComparisonResult(
				dynamicScore,
				endDynamic - startingDynamic,
				geneticScore,
				endGenetic - startingGenetic);
	}
}
