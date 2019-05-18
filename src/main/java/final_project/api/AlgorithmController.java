package final_project.api;

import final_project.common.*;
import final_project.genetic.entities.Constraint;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class AlgorithmController {
	private AlgorithmComparison algorithmComparison = new AlgorithmComparison();

	@CrossOrigin(origins = {"http://localhost:63343", "http://localhost:63342"})
	@RequestMapping("/runStatistics")
	public List<ComparisonResult> runStatistics(
			@RequestParam("workers") int workers,
			@RequestParam("days") int days,
			@RequestParam("shifts") int shifts,
			@RequestParam("necessaryWorkers") int necessaryWorkers
	) {
		List<ComparisonResult> comparisonResultsList = new ArrayList<>();

		for (int i = 1; i < necessaryWorkers && i <= workers / 2; i++) {
			ComparisonResult comparisonResult = algorithmComparison.compare(generateRandomShiftRequests(workers, days, shifts), necessaryWorkers);
			comparisonResultsList.add(comparisonResult);
		}

		return comparisonResultsList;
	}

	@CrossOrigin(origins = {"http://localhost:63343", "http://localhost:63342"})
	@RequestMapping("/realData")
	public AlgorithmsResults realData() {
		AlgorithmRunner dynamicAlgorithmRunner = new DynamicAlgorithmRunner();
		AlgorithmRunner geneticAlgorithmRunner = new GeneticAlgorithmRunner();

		int[][][] dynamicResult = dynamicAlgorithmRunner.run(RealData.getData(), 5);
		int[][][] geneticResult = geneticAlgorithmRunner.run(RealData.getData(), 5);

		AlgorithmsResults algorithmsResults = new AlgorithmsResults(dynamicResult, geneticResult);

		return algorithmsResults;
	}

	@CrossOrigin(origins = {"http://localhost:63343", "http://localhost:63342"})
	@RequestMapping("/genetic")
	public double genetic(
			@RequestParam("populationSize") int populationSize,
			@RequestParam("crossoverRate") float crossoverRate,
			@RequestParam("mutationRate") float mutationRate,
			@RequestParam("randomizeRate") float randomizeRate
	) {

		GeneticAlgorithmRunner geneticAlgorithmRunner = new GeneticAlgorithmRunner();

		int[][][] result = geneticAlgorithmRunner.run(
				RealData.getData(),
				5,
				populationSize,
				crossoverRate,
				mutationRate,
				randomizeRate);

		AlgorithmScorer algorithmScorer = new AlgorithmScorer();

		return algorithmScorer.score(RealData.getData(), result);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping("/runAlgorithm")
	@ResponseBody
	public int[][][] runAlgorithm(@RequestBody Constraint constraints) {
		DynamicAlgorithmRunner dynamicAlgorithmRunner = new DynamicAlgorithmRunner();
		int[][][] dynamicResult = dynamicAlgorithmRunner.run(constraints.getConstraints(), constraints.getNecessaryWorkers());

		return dynamicResult;
	}

	private int[][][] generateRandomShiftRequests(int workers, int days, int shifts) {
		int[][][] shiftRequestsMatrix = new int[workers][days][shifts];

		for (int i = 0; i < shiftRequestsMatrix.length; i++) {
			for (int j = 0; j < shiftRequestsMatrix[i].length; j++) {
				for (int k = 0; k < shiftRequestsMatrix[i][j].length; k++) {
					shiftRequestsMatrix[i][j][k] = randomizeConstraint();
				}
			}
		}

		return shiftRequestsMatrix;
	}

	private int randomizeConstraint() {
		int n = new Random().nextInt(4);

		return (int) Math.pow(10, n);
	}
}