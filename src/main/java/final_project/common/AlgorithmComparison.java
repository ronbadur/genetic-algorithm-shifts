package final_project.common;

public class AlgorithmComparison {
	private AlgorithmRunner dynamicAlgorithmRunner = new DynamicAlgorithmRunner();
	private AlgorithmRunner geneticAlgorithmRunner = new GeneticAlgorithmRunner();
	private AlgorithmScorer algorithmScorer = new AlgorithmScorer();

	public ComparisonResult compare(int[][][] constraints, int numberOfWorkersInShift) {
		long startingDynamic = System.currentTimeMillis();
		int[][][] dynamicSolution = dynamicAlgorithmRunner.run(constraints, numberOfWorkersInShift);
		long endDynamic = System.currentTimeMillis();

		long startingGenetic = System.currentTimeMillis();
		int[][][] geneticSolution = geneticAlgorithmRunner.run(constraints, numberOfWorkersInShift);
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
