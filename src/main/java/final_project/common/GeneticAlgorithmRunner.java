import final_project.common.AlgorithmRunner;

public class GeneticAlgorithm implements AlgorithmRunner {

	@Override
	public int[][][] run(int[][][] constraints, int numberOfWorkersInShift) {
		Random rn = new Random();
		Constraint algorithmConstraints = new Constraint(constraints);
		Population population = new Population();
		Population newPopulation = new Population();
		int generationCount = 0;

		return new int[0][][];
	}
}