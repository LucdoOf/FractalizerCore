package fr.fractalizer;

public enum CompressionState {

	LOADING_IMAGE("Chargement de l'image..");
	
	private String name;
	
	private CompressionState(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
	
}
