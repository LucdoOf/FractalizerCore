package fr.fractalizer.managers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import fr.fractalizer.managers.compressions.CompressionManager;
import fr.fractalizer.managers.transformations.Transformation;
import fr.fractalizer.model.InitRange;
import fr.fractalizer.model.Range;

public class DecompressionManager {
	
	public static void decompress(String str, int width, int height, CompressionManager compressionManager) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
		for(String line : str.split("\n")) {
			String rangeString = line.split(" ")[0];
			String[] rangeHexa = rangeString.split(";");
			Color[][] initRangeColor = new Color[Range.RANGE_SIZE][Range.RANGE_SIZE];
			for(int x = 0; x < rangeHexa.length; x++) {
				String[] rangeHexaLine = rangeHexa[x].split(",");
				for(int y = 0; y < rangeHexaLine.length; y++) {
					initRangeColor[x][y] = new Color(hex2Rgb(rangeHexaLine[y]));	
				}
			}
			
			
			String transformationsString = line.split(" ")[1];
			String[] transformations = transformationsString.split(";");
			for(String transformationString : transformations) {
				List<HashMap<Transformation, Object[]>> transformationCouple = compressionManager.getParametersCouples().get(Integer.parseInt(transformationString.split(",")[2]));
				Range initRange = new InitRange(0, 0, initRangeColor);
				for(HashMap<Transformation, Object[]> transformation : transformationCouple) {
					((Transformation) transformation.keySet().toArray()[0]).transform(initRange, transformation.get(transformation.keySet().toArray()[0]));
				}
				int x = Integer.parseInt(transformationString.split(",")[0]);
				int y = Integer.parseInt(transformationString.split(",")[1]);
				for(int x1 = 0; x1 < Range.RANGE_SIZE; x1++) {
					for(int y1 = 0; y1 < Range.RANGE_SIZE; y1++) {
						image.setRGB(x1+x, y1+y, initRange.getColors()[x1][y1].getRGB());
					}
				}
			}
		}
		File outputfile = new File("decompressed.png");
		try {
			ImageIO.write(image, "jpg", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static int hex2Rgb(String colorStr) {
	    return new Color(
	            Integer.valueOf( colorStr.substring( 0, 2 ), 16 ),
	            Integer.valueOf( colorStr.substring( 2, 4 ), 16 ),
	            Integer.valueOf( colorStr.substring( 4, 6 ), 16 ) ).getRGB();
	}

}
