package fr.fractalizer.model;

import java.util.HashMap;
import java.util.List;

import fr.fractalizer.managers.transformations.Transformation;

public class ProcessResult {

	private double difference;
	private Range initRange;
	private Range comparingRange;
	private List<HashMap<Transformation, Object[]>> parametersCouples;
	
	public ProcessResult(Range initRange, Range comparingRange, List<HashMap<Transformation, Object[]>> parametersCouples, double difference) {
		this.difference = difference;
		this.comparingRange = comparingRange;
		this.parametersCouples = parametersCouples;
		this.difference = difference;
	}

	public double getDifference() {
		return difference;
	}

	public Range getInitRange() {
		return initRange;
	}

	public Range getComparingRange() {
		return comparingRange;
	}

	public List<HashMap<Transformation, Object[]>> getParametersCouples(){
		return parametersCouples;
	}

	
	
}
