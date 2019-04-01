package fr.fractalizer.managers.transformations;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import fr.fractalizer.model.EmptyRange;
import fr.fractalizer.model.Range;

public class TransformationManager {

	public static Transformation rotation; 
	public static List<Object[]> rotationParameters;
	public static Transformation isometry;
	public static List<Object[]> isometryParameters;
	public static Transformation identity;
	public static final int ISOMETRY_VERTICAL = 1;
	public static final int ISOMETRY_HORIZONTAL = 0;
	
	public TransformationManager() {
		identity = new Transformation() {
			@Override
			public Range transform(Range range, Object... objects) {
				return range;
			}
			@Override
			public String toString() {return "Identité";}
		};
		rotation = new Transformation() {
			@Override
			public Range transform(Range range, Object... objects) {
				EmptyRange toReturn = new EmptyRange(range.getX(), range.getY());
				//System.out.println("Compression rotation à " + objects[0] + " sur " + toReturn.getX() + " " + toReturn.getY());
				int angle;
				try {
					angle = (int) objects[0];
					double xc = Range.RANGE_SIZE/2-0.5;
					double yc = Range.RANGE_SIZE/2-0.5;
					Color[][] newColors = new Color[Range.RANGE_SIZE][Range.RANGE_SIZE];
					for(int x = 0; x < Range.RANGE_SIZE; x++) {
						for(int y = 0; y < Range.RANGE_SIZE; y++) {
							double X = x - xc;
							double Y = y - yc;
							int newX = Math.round((float)(Math.cos(angle*(Math.PI/180))*(X) - Math.sin(angle*(Math.PI/180))*(Y) + xc)); 
							int newY = Math.round((float)(Math.sin(angle*(Math.PI/180))*(X) + Math.cos(angle*(Math.PI/180))*(Y) + yc)); 
							//System.out.println(newX + " " + newY + " " + x + " " + y + " " +toReturn.getX() + " " + toReturn.getY());
							newColors[newX][newY] = range.getColors()[x][y];
						}
					}
					toReturn.setColors(newColors);
					return toReturn;
				} catch (Exception e) {
					throw new InvalidTransformParameters("Error loading rotation parameters, excepting first parameter to be an integer " + objects[0], e);
				}
			}
			@Override
			public String toString() {return "Rotation";}
		};
		Object rotationP[][] = { {90},{180},{270} };
		rotationParameters = Arrays.asList(rotationP);
		isometry = new Transformation() {
			@Override
			public Range transform(Range range, Object... objects) {
				EmptyRange toReturn = new EmptyRange(range.getX(), range.getY());
				//System.out.println("Compression isométrique à " + objects[0] + " sur " + toReturn.getX() + " " + toReturn.getY());
				int isometryOrientation;
				try {
					isometryOrientation = (int) objects[0];
					Color[][] newColors = new Color[Range.RANGE_SIZE][Range.RANGE_SIZE];
					for(int x = 0; x < Range.RANGE_SIZE; x++) {
						for(int y= 0; y < Range.RANGE_SIZE; y++) {
							if(isometryOrientation == TransformationManager.ISOMETRY_VERTICAL) {
								int newX = (Range.RANGE_SIZE-x)-1; //-1 car les x vont de 0 a 3 et le 0 va au 3 
								newColors[newX][y] = range.getColors()[x][y];  //TODO Etrange que ça soit qune condition, a verifier
							} else if(isometryOrientation == TransformationManager.ISOMETRY_HORIZONTAL) {
								int newY = (Range.RANGE_SIZE-y)-1;
								newColors[x][newY] = range.getColors()[x][y];
							} else {
								throw new InvalidTransformParameters("Error loading isometry parameters, unknow isometry type", null);
							}
						}
					}
					toReturn.setColors(newColors);
					return toReturn;
				} catch (Exception e) {
					throw new InvalidTransformParameters("Error loading isometry parameters, excepting first parameter to be an integer " + objects[0], e);
				}
			}
			@Override
			public String toString() {return "Isométrie";}
		};
		Object isometryP[][] = {{ISOMETRY_HORIZONTAL},{ISOMETRY_VERTICAL}};
		isometryParameters = Arrays.asList(isometryP);
	}
	
	
}
