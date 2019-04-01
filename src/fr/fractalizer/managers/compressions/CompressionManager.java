package fr.fractalizer.managers.compressions;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import fr.fractalizer.CompressionState;
import fr.fractalizer.managers.transformations.Transformation;
import fr.fractalizer.managers.transformations.TransformationManager;
import fr.fractalizer.model.ProcessResult;
import fr.fractalizer.model.Range;
import fr.fractalizer.utils.ProcessCallback;

public abstract class CompressionManager {

	protected List<Range> diList;
	protected List<Range> riList;
	protected CompressionState compressionState;
	private List<List<HashMap<Transformation, Object[]>>> parametersCouples;
	private CompressionType compressionType;
	
	public CompressionManager(List<Range> diList, List<Range> riList, CompressionType compressionType) {
		@SuppressWarnings("unused")
		TransformationManager transformationManager = new TransformationManager();
		this.diList = diList;
		this.riList = riList;
		this.compressionState = CompressionState.LOADING_IMAGE;
		this.compressionType = compressionType;
		this.parametersCouples = findParametersCouples();
	}
	
	public abstract LinkedHashMap<Range, List<ProcessResult>> start();
	protected abstract List<List<HashMap<Transformation, Object[]>>> findParametersCouples();
	
	public static CompressionManager initCompressionManager(List<Range>[] ranges, CompressionType type, Object... compressionParams) {
		List<Range> diList = ranges[0];
		List<Range> riList = ranges[1];
		switch (type) {
		case BASIC:
			return new BasicCompressionManager(diList, riList); 
		case JACQUIN:
			return new JacquinCompressionManager(diList, riList, (int) compressionParams[0], (int) compressionParams[1], (ProcessCallback) compressionParams[2]);
		default:
			return new BasicCompressionManager(diList, riList);
		}
	}

	public CompressionType getCompressionType() { return compressionType; }
	public void setDiList(List<Range> diList) { this.diList = diList; }
	public void setRiList(List<Range> riList) { this.riList = riList; }
	public void setCompressionState(CompressionState compressionState) { this.compressionState = compressionState; }
	public List<Range> getDiList() { return diList; }
	public List<Range> getRiList() { return riList; }
	public CompressionState getCompressionState() { return compressionState; }
	public List<List<HashMap<Transformation, Object[]>>> getParametersCouples() { return parametersCouples; }

	
}
