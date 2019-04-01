package fr.fractalizer.model;

import java.awt.Color;

public abstract class Range {
	
	public static int RANGE_SIZE = 4;
	
	protected int x;
	protected int y;
	protected Color[][] colors;
	
	public Range(int x, int y, @SuppressWarnings("exports") Color[][] colors) {
		this.x = x;
		this.y = y;
		this.colors = colors;
	}
	
	public double compare(Range testingRange) {
		double diff = 0;
		
		for(int x = 0; x < Range.RANGE_SIZE; x++) {
			for(int y = 0; y < Range.RANGE_SIZE; y++) {
				double diffR = Math.abs(testingRange.getColors()[x][y].getRed()-this.getColors()[x][y].getRed());
				double diffG = Math.abs(testingRange.getColors()[x][y].getGreen()-this.getColors()[x][y].getGreen());
				double diffB = Math.abs(testingRange.getColors()[x][y].getBlue()-this.getColors()[x][y].getBlue());
				diff = diff + (diffR + diffG + diffB)/3;
			}
		}
		
		diff = diff/(Range.RANGE_SIZE*Range.RANGE_SIZE);
		return diff;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@SuppressWarnings("exports")
	public Color[][] getColors() {
		return colors;
	}
	
}
