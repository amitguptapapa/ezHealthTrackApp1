package com.ezhealthtrack.model.laborder;

import com.google.gson.annotations.Expose;

public class LabOrderDetails {

	@Expose
	private String s;
	@Expose
	private String m;
	@Expose
	private Data data;
	@Expose
	private String printLabHeader;

	/**
	 * 
	 * @return The s
	 */
	public String getS() {
		return s;
	}

	/**
	 * 
	 * @param s
	 *            The s
	 */
	public void setS(String s) {
		this.s = s;
	}

	/**
	 * 
	 * @return The m
	 */
	public String getM() {
		return m;
	}

	/**
	 * 
	 * @param m
	 *            The m
	 */
	public void setM(String m) {
		this.m = m;
	}

	/**
	 * 
	 * @return The data
	 */
	public Data getData() {
		return data;
	}

	/**
	 * 
	 * @param data
	 *            The data
	 */
	public void setData(Data data) {
		this.data = data;
	}

	/**
	 * 
	 * @return The printLabHeader
	 */
	public String getPrintLabHeader() {
		return printLabHeader;
	}

	/**
	 * 
	 * @param printLabHeader
	 *            The printLabHeader
	 */
	public void setPrintLabHeader(String printLabHeader) {
		this.printLabHeader = printLabHeader;
	}

}