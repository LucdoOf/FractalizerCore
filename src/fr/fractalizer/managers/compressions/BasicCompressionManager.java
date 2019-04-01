package fr.fractalizer.managers.compressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import fr.fractalizer.managers.transformations.Transformation;
import fr.fractalizer.managers.transformations.TransformationManager;
import fr.fractalizer.model.ProcessResult;
import fr.fractalizer.model.Range;

public class BasicCompressionManager extends CompressionManager {

	public BasicCompressionManager(List<Range> diList, List<Range> riList) {
		super(diList, riList, CompressionType.BASIC);
	}

	@Override
	public LinkedHashMap<Range, List<ProcessResult>> start() {
		LinkedHashMap<Range, List<ProcessResult>> rangeCouples = new LinkedHashMap<Range, List<ProcessResult>>();
		for(int i = 0; i < diList.size(); i++) {
			Range rangeDI = diList.get(i);
			double minDifference = 255;
			ProcessResult matchingQuery = null;
			final Range initialRange = rangeDI;
			for(List<HashMap<Transformation, Object[]>> parametersCouple : getParametersCouples()) {
				Range transformedInitialRange = initialRange;
				for(HashMap<Transformation, Object[]> transformations : parametersCouple) {
					Transformation t = (Transformation) transformations.keySet().toArray()[0];
					Object[] p = transformations.get(t);
					transformedInitialRange = t.transform(rangeDI, p);
					for(Range testingRange : diList) {
						if(testingRange == rangeDI) continue;
						double difference = transformedInitialRange.compare(testingRange);
						if(difference <= minDifference) {
							minDifference = difference;
							matchingQuery = new ProcessResult(transformedInitialRange, testingRange, parametersCouple, difference);
						}
					}
				}
			}
			System.out.println("GAROFALO: Pour le range en " + rangeDI.getX() + " " + rangeDI.getY() + " on trouve " + matchingQuery.getComparingRange().getX() + " " + matchingQuery.getComparingRange().getY() + " avec une difference de " + matchingQuery.getDifference());
			rangeCouples.put(rangeDI, Arrays.asList(matchingQuery));
		}
		return rangeCouples;
	}

	@Override
	protected List<List<HashMap<Transformation, Object[]>>> findParametersCouples() {
		Transformation rotation = TransformationManager.rotation;
		Transformation isometry = TransformationManager.isometry;
		List<Object[]> rotationParameters = TransformationManager.rotationParameters;
		List<Object[]> isometryParameters = TransformationManager.isometryParameters;
		List<List<HashMap<Transformation, Object[]>>> combinations = new ArrayList<List<HashMap<Transformation,Object[]>>>();
		ArrayList<HashMap<Transformation, Object[]>> identityTransformation = new ArrayList<HashMap<Transformation,Object[]>>();
		HashMap<Transformation, Object[]> identityHashMap = new HashMap<Transformation, Object[]>();
		identityHashMap.put(TransformationManager.identity, null);
		identityTransformation.add(identityHashMap);
		combinations.add(identityTransformation);
		for(Object[] rP : rotationParameters) {
			for(Object[] iP : isometryParameters) {
				HashMap<Transformation, Object[]> rH = new HashMap<Transformation, Object[]>();
				rH.put(rotation, rP);
				HashMap<Transformation, Object[]> iH = new HashMap<Transformation, Object[]>();
				iH.put(isometry, iP);
				combinations.add(Arrays.asList(rH, iH));
			}
		}
		return combinations;
	}
	

}
