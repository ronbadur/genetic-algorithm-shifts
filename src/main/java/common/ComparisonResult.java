package common;

public class ComparisonResult {
	private double dynamicScore;
	private double dynamicDuration;

	private double geneticScore;
	private double geneticDuration;

	public ComparisonResult(double dynamicScore, double dynamicDuration, double geneticScore, double geneticDuration) {
		this.dynamicScore = dynamicScore;
		this.dynamicDuration = dynamicDuration;
		this.geneticScore = geneticScore;
		this.geneticDuration = geneticDuration;
	}

	public double getDynamicScore() {
		return dynamicScore;
	}

	public void setDynamicScore(double dynamicScore) {
		this.dynamicScore = dynamicScore;
	}

	public double getDynamicDuration() {
		return dynamicDuration;
	}

	public void setDynamicDuration(double dynamicDuration) {
		this.dynamicDuration = dynamicDuration;
	}

	public double getGeneticScore() {
		return geneticScore;
	}

	public void setGeneticScore(double geneticScore) {
		this.geneticScore = geneticScore;
	}

	public double getGeneticDuration() {
		return geneticDuration;
	}

	public void setGeneticDuration(double geneticDuration) {
		this.geneticDuration = geneticDuration;
	}

	@Override
	public String toString() {
		return "ComparisonResult{" +
				"dynamicScore=" + dynamicScore +
				", dynamicDuration=" + dynamicDuration +
				", geneticScore=" + geneticScore +
				", geneticDuration=" + geneticDuration +
				'}';
	}
}
