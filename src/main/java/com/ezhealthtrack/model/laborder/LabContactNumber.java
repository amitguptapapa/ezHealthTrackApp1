package com.ezhealthtrack.model.laborder;

import com.google.gson.annotations.Expose;

public class LabContactNumber {

	@Expose
	private String num;

	/**
	 * 
	 * @return The num
	 */
	public String getNum() {
		return num;
	}

	/**
	 * 
	 * @param num
	 *            The num
	 */
	public void setNum(String num) {
		this.num = num;
	}

}