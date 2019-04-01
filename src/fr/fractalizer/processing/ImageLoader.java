package fr.fractalizer.processing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import fr.fractalizer.model.EmptyRange;
import fr.fractalizer.model.InitRange;
import fr.fractalizer.model.Range;

public class ImageLoader {

	public static List<Range>[] loadImage(File file, int rangeSize) {
		Range.RANGE_SIZE = rangeSize;
		List<Range> diList = new ArrayList<Range>();
		List<Range> riList = new ArrayList<Range>();
		try {
			BufferedImage bi = ImageIO.read(file);
			for (int x = 0; x <bi.getWidth(); x+=Range.RANGE_SIZE) {
			    for (int y = 0; y < bi.getHeight()-Range.RANGE_SIZE; y+=Range.RANGE_SIZE) { //On enleve pour éviter les dépassements
			    	//INIT COLORS
			    	Color[][] colors = new Color[Range.RANGE_SIZE][Range.RANGE_SIZE];
			    	for(int xc = x; xc<Range.RANGE_SIZE+x; xc++) {
			    		for(int yc = y; yc<Range.RANGE_SIZE+y; yc++) {
					        Color c = new Color(bi.getRGB(xc, yc));
					        colors[xc-x][yc-y] = c;
			    		}
			    	}
			    	diList.add(new InitRange(x, y, colors));
			    	riList.add(new EmptyRange(x, y));
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		List<Range>[] toReturn = new List[2];
		toReturn[0] = diList;
		toReturn[1] = riList;
		return toReturn;
	}
	
}
