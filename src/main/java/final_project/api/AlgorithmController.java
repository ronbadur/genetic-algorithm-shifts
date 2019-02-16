package final_project.api;

import final_project.common.AlgorithmComparison;
import final_project.common.ComparisonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class AlgorithmController {
	private AlgorithmComparison algorithmComparison = new AlgorithmComparison();

	@RequestMapping("/runStatistics")
	public List<ComparisonResult> runStatistics(@RequestParam("numberOfWorkers") int numberOfWorkers) {
		List<ComparisonResult> comparisonResultsList = new ArrayList<>();

		for (int i = 0; i < numberOfWorkers; i++) {
			ComparisonResult comparisonResult = algorithmComparison.compare(generateRandomShiftRequests(8, 5, 3), numberOfWorkers);
			comparisonResultsList.add(comparisonResult);
		}

		return comparisonResultsList;
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