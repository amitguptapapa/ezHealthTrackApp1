package com.ezhealthtrack.order;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SampleMetum {

	@Expose
	private String name;
	@Expose
	private String method;
	@Expose
	public String result_interpretation;
	@Expose
	public Boolean result_name_bold;
	@Expose
	public Boolean display_interpretation;
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

	/**
	 * 
	 * @return The result_name_bold
	 */
	public Boolean getresult_name_bold() {
		return result_name_bold;
	}

	/**
	 * 
	 * @param result_name_bold
	 *            The result_name_bold
	 */
	public void setresult_name_bold(Boolean result_name_bold) {
		this.result_name_bold = result_name_bold;
	}

	/**
	 * 
	 * @return The display_interpretation
	 */
	public Boolean getdisplay_interpretation() {
		return display_interpretation;
	}

	/**
	 * 
	 * @param display_interpretation
	 *            The display_interpretation
	 */
	public void setdisplay_interpretation(Boolean display_interpretation) {
		this.display_interpretation = display_interpretation;
	}

}
