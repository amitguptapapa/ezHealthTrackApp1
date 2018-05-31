package com.ezhealthtrack.model.laborder;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class SampleMetum {

	@Expose
	private String name;
	@Expose
	private String method;
	@Expose
	private List<Result> results = new ArrayList<Result>();
	public List<SampleMetum> sample_meta = new ArrayList<SampleMetum>();

	/**
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 
	 * @param method
	 *            The method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * 
	 * @return The results
	 */
	public List<Result> getResults() {
		return results;
	}

	/**
	 * 
	 * @param results
	 *            The results
	 */
	public void setResults(List<Result> results) {
		this.results = results;
	}

}