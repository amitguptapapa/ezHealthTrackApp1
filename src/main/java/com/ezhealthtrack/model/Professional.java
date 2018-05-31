package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;

public class Professional {

	@Expose
	private String specal = "";
	@Expose
	private String expertise1 = "";
	@Expose
	private String expertise2 = "";
	@Expose
	private String doc_exp = "";
	@Expose
	private String exp_desc = "";
	@Expose
	private String awards = "";
	@Expose
	private String template = "";

	public String getSpecal() {
		return specal;
	}

	public void setSpecal(String specal) {
		this.specal = specal;
	}

	public String getExpertise1() {
		return expertise1;
	}

	public void setExpertise1(String expertise1) {
		this.expertise1 = expertise1;
	}

	public String getExpertise2() {
		return expertise2;
	}

	public void setExpertise2(String expertise2) {
		this.expertise2 = expertise2;
	}

	public String getDoc_exp() {
		return doc_exp;
	}

	public void setDoc_exp(String doc_exp) {
		this.doc_exp = doc_exp;
	}

	public String getExp_desc() {
		return exp_desc;
	}

	public void setExp_desc(String exp_desc) {
		this.exp_desc = exp_desc;
	}

	public String getAwards() {
		return awards;
	}

	public void setAwards(String awards) {
		this.awards = awards;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}
