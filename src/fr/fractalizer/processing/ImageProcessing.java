package fr.fractalizer.processing;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;

import javax.imageio.ImageIO;

import fr.fractalizer.managers.DecompressionManager;
import fr.fractalizer.managers.compressions.CompressionManager;
import fr.fractalizer.managers.compressions.CompressionType;
import fr.fractalizer.model.ProcessResult;
import fr.fractalizer.model.Range;
import fr.fractalizer.utils.ProcessCallback;

public class ImageProcessing {

	//METADATA COMPRESSIONARGS: RangeSize, Percentage, StepCallback, DoneCallback
	public static void compress(File file, CompressionType type, Object... compressionArgs) {
		
		int rangeSize = compressionArgs.length > 0 ? (int) compressionArgs[0] : 4;
		int percentage = compressionArgs.length > 1 ? (int) compressionArgs[1] : 100;
		int epsilon = compressionArgs.length > 2 ? (int) compressionArgs[2] : 20;
		ProcessCallback stepCallback = compressionArgs.length > 3 ? (ProcessCallback) compressionArgs[3] : null;
		ProcessCallback doneCallback = compressionArgs.length > 4 ? (ProcessCallback) compressionArgs[4] : null;
		
		long startTime = System.currentTimeMillis();
		System.out.println("Partition de l'image..");
		List<Range>[] loadedRanges = ImageLoader.loadImage(file, rangeSize);
		System.out.println("Partition de l'image finie, durée: " + ((System.currentTimeMillis()-startTime)) + "ms");
		
		System.out.println("Compression de l'image..");
		startTime = System.currentTimeMillis();
		CompressionManager compressionManager = CompressionManager.initCompressionManager(loadedRanges, type, percentage, epsilon, stepCallback);
		LinkedHashMap<Range, List<ProcessResult>> compressionResult = compressionManager.start();
		System.out.println("Compression de l'image finie, durée: " + ((System.currentTimeMillis()-startTime)/1000) + "s");
		
		System.out.println("Sauvegarde de l'image..");
		startTime = System.currentTimeMillis();
		ImageSaver.saveImage(compressionResult, compressionManager);
		System.out.println("Sauvegarde de l'image finie, durée: " + ((System.currentTimeMillis()-startTime)/1000) + "s");
		
		doneCallback.done(-1, -1);
	}
	
	public static void decompress(File initFile, CompressionType type, Object... compressionArgs) {
		List<Range>[] loadedRanges = ImageLoader.loadImage(initFile, compressionArgs.length > 0 ? (int) compressionArgs[0] : 4);
		try {
			String str = new String(Files.readAllBytes(Paths.get("save.txt")));
			DecompressionManager.decompress(str, ImageIO.read(initFile).getWidth(), ImageIO.read(initFile).getHeight(), CompressionManager.initCompressionManager(loadedRanges, type, compressionArgs.length > 1 ? (int) compressionArgs[1] : 100, compressionArgs.length > 2 ? (int) compressionArgs[2] : 20));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
