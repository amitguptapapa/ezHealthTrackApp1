package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;

public class PatientAutoSuggest {

	@Expose
	private String id;
	@Expose
	private String name;
	
	public PatientAutoSuggest(){
		
	}
	
	public PatientAutoSuggest(String id , String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * 
	 * @return The id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return The name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}
