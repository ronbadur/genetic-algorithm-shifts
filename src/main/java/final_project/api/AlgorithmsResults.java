package final_project.api;

public class AlgorithmsResults {
	public int[][][] geneticResult;
	public int[][][] dynamicResult;

	public AlgorithmsResults(int[][][] dynamicResult, int[][][] geneticResult) {
		this.dynamicResult = dynamicResult;
		this.geneticResult = geneticResult;
	}
}
