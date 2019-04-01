package fr.fractalizer.managers.transformations;

import fr.fractalizer.model.Range;

public interface Transformation {

	public Range transform(Range range, Object... objects);
	
}
