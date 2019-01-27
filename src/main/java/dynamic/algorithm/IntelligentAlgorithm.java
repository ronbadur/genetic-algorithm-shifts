package dynamic.algorithm;

import dynamic.entities.Shift;
import dynamic.entities.Worker;

import java.util.List;

public class IntelligentAlgorithm {
	public void scheduleShifts(List<Worker> workers, List<Shift> shifts) {
		for (Worker worker : workers) {
			worker.getConstraints();
		}
	}
}
