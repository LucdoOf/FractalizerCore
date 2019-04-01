package fr.fractalizer.processing;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;

import fr.fractalizer.managers.compressions.CompressionManager;
import fr.fractalizer.model.ProcessResult;
import fr.fractalizer.model.Range;

public class ImageSaver {

	public static void saveImage(LinkedHashMap<Range, List<ProcessResult>> ranges, CompressionManager compressionManager) {
		switch (compressionManager.getCompressionType()) {
		case JACQUIN:
			String saveString = "";
			for(Range range : ranges.keySet()) {
				//Sauvegarde du range de base
				for(int x = 0; x < Range.RANGE_SIZE; x++) {
					for(int y = 0; y < Range.RANGE_SIZE; y++) {
						saveString += rgbToHex(range.getColors()[x][y]);
						if(y != Range.RANGE_SIZE-1) saveString += ",";
						else saveString += ";";
					}
				}
				if(!ranges.get(range).isEmpty()) {
					//Sauvegarde des paramètres de chaque transformations
					saveString += " ";
					for(ProcessResult processResult : ranges.get(range)) {
						saveString += processResult.getComparingRange().getX() + "," + processResult.getComparingRange().getY() + "," 
								+ compressionManager.getParametersCouples().indexOf(processResult.getParametersCouples());
						if(!processResult.equals(ranges.get(range).get(ranges.get(range).size()-1))) saveString += ";";
						else saveString += "\n";
					}
				} else {
					saveString += "\n";
				}
			}
			try {
				PrintWriter writer = new PrintWriter("save.txt", "UTF-8");
				writer.print(saveString);
				writer.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	
	public final static String rgbToHex(@SuppressWarnings("exports") Color colour) throws NullPointerException {
	  String hexColour = Integer.toHexString(colour.getRGB() & 0xffffff);
	  if (hexColour.length() < 6) {
	    hexColour = "000000".substring(0, 6 - hexColour.length()) + hexColour;
	  }
	  return hexColour;
	}
	
}
