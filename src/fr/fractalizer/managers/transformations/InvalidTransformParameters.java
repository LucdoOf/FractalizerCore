package fr.fractalizer.managers.transformations;

@SuppressWarnings("serial")
public class InvalidTransformParameters extends RuntimeException {

	 public InvalidTransformParameters(String errorMessage, Throwable err) {
	        super(errorMessage, err);
	 }
}
