package com.ezhealthtrack.model;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.ezhealthtrack.activity.DashboardActivity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RadiologyModel {

	@SerializedName("category_name")
	@Expose
	private String categoryName;
	@SerializedName("category_id")
	@Expose
	private String categoryId;
	@Expose
	private String type;
	@SerializedName("test_name")
	@Expose
	private String testName;
	@Expose
	private String codid;
	@SerializedName("parent_id")
	@Expose
	private String parentId;
	@Expose
	private String codcat;
	@Expose
	private String gid;
	public boolean isChecked;

	public boolean getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getCodid() {
		return codid;
	}

	public void setCodid(String codid) {
		this.codid = codid;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCodcat() {
		return codcat;
	}

	public void setCodcat(String codcat) {
		this.codcat = codcat;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

}