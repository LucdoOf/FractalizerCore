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
import fr.fractalizer.utils.ProcessCallback;

public class JacquinCompressionManager extends CompressionManager {

	private int percentageCover;
	private int epsilon;
	private ProcessCallback stepCallback;
	
	@SuppressWarnings("exports")
	public JacquinCompressionManager(List<Range> diList, List<Range> riList, int percentageCover, int epsilon, ProcessCallback stepCallback) {
		super(diList, riList, CompressionType.JACQUIN);
		this.percentageCover = percentageCover;
		this.epsilon = epsilon;
		this.stepCallback = stepCallback;
	}

	@Override
	public LinkedHashMap<Range, List<ProcessResult>> start() {
		int total = 0;
		LinkedHashMap<Range, List<ProcessResult>> rangeCouples = new LinkedHashMap<Range, List<ProcessResult>>();
		List<Range> processedRanges = new ArrayList<Range>();
		for(int i = 0; i < diList.size(); i++) {
			if(((float)total/diList.size())*100 >= percentageCover) break;
			Range rangeDI = diList.get(i);
			final Range initialRange = rangeDI;
			HashMap<Range, ProcessResult> results = new HashMap<Range, ProcessResult>();
			for(List<HashMap<Transformation, Object[]>> parametersCouple : getParametersCouples()) {
				Range transformedInitialRange = initialRange;
				for(HashMap<Transformation, Object[]> transformations : parametersCouple) {
					Transformation t = (Transformation) transformations.keySet().toArray()[0];
					Object[] p = transformations.get(t);
					transformedInitialRange = t.transform(rangeDI, p);
					for(Range testingRange : diList) {
						if(testingRange == rangeDI) continue;
						double difference = transformedInitialRange.compare(testingRange);
						if(difference <= epsilon && !processedRanges.contains(testingRange)) {
							ProcessResult result = new ProcessResult(transformedInitialRange, testingRange, parametersCouple, difference);
							if(!results.containsKey(testingRange)) {
								results.put(testingRange, result);
							} else {
								if(results.get(testingRange).getDifference() > difference) {
									results.put(testingRange, result);
								}
							}
						}
					}
				}
			}
			if(!results.isEmpty()) {
				for(Range bestRange : results.keySet()) {
					total++;
					if(!rangeCouples.containsKey(rangeDI)) rangeCouples.put(rangeDI, new ArrayList<ProcessResult>());
					rangeCouples.get(rangeDI).add(new ProcessResult(rangeDI, rangeDI, getParametersCouples().get(0), 0));
					rangeCouples.get(rangeDI).add(results.get(bestRange));
					processedRanges.add(bestRange);
					processedRanges.add(results.get(bestRange).getInitRange());
				}
			}
			boolean contained = processedRanges.contains(rangeDI);
			if(!(rangeCouples.containsKey(rangeDI) || contained)) {
				rangeCouples.put(rangeDI, new ArrayList<ProcessResult>());
				rangeCouples.get(rangeDI).add(new ProcessResult(rangeDI, rangeDI, getParametersCouples().get(0), 0));
			}
			System.out.println(total + " / " + diList.size() + " " + i);
			stepCallback.done(total, diList.size());
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
