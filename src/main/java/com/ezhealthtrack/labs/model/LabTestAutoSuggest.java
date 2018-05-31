package com.ezhealthtrack.labs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabTestAutoSuggest {

@Expose
private String name;
@Expose
private String id;
@SerializedName("price_min")
@Expose
private String priceMin;
@SerializedName("price_max")
@Expose
private String priceMax;
@Expose
private String samples;

/**
* 
* @return
* The name
*/
public String getName() {
return name;
}

/**
* 
* @param name
* The name
*/
public void setName(String name) {
this.name = name;
}

/**
* 
* @return
* The id
*/
public String getId() {
return id;
}

/**
* 
* @param id
* The id
*/
public void setId(String id) {
this.id = id;
}

/**
* 
* @return
* The priceMin
*/
public String getPriceMin() {
return priceMin;
}

/**
* 
* @param priceMin
* The price_min
*/
public void setPriceMin(String priceMin) {
this.priceMin = priceMin;
}

/**
* 
* @return
* The priceMax
*/
public String getPriceMax() {
return priceMax;
}

/**
* 
* @param priceMax
* The price_max
*/
public void setPriceMax(String priceMax) {
this.priceMax = priceMax;
}

/**
* 
* @return
* The samples
*/
public String getSamples() {
return samples;
}

/**
* 
* @param samples
* The samples
*/
public void setSamples(String samples) {
this.samples = samples;
}

@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}

}

