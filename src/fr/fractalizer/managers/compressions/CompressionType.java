package fr.fractalizer.managers.compressions;

public enum CompressionType {

	BASIC("Basique"),
	JACQUIN("Jacquin");
	
	private String name;
	
	private CompressionType(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
	
}
