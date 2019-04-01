package fr.fractalizer.model;

import java.awt.Color;

public class EmptyRange extends Range{

	public EmptyRange(int x, int y) {
		super(x, y, new Color[Range.RANGE_SIZE][Range.RANGE_SIZE]);
	}
	
	public void setColors(@SuppressWarnings("exports") Color[][] colors) {
		this.colors = colors;
	}

}
