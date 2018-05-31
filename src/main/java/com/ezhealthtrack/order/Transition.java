package com.ezhealthtrack.order;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transition {

@Expose
private String name;
@Expose
private String label;
@Expose
private List<String> permission = new ArrayList<String>();
@Expose
private IncludePermission includePermission;
@SerializedName("to_step")
@Expose
private ToStep toStep;

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
* The label
*/
public String getLabel() {
return label;
}

/**
* 
* @param label
* The label
*/
public void setLabel(String label) {
this.label = label;
}

/**
* 
* @return
* The permission
*/
public List<String> getPermission() {
return permission;
}

/**
* 
* @param permission
* The permission
*/
public void setPermission(List<String> permission) {
this.permission = permission;
}

/**
* 
* @return
* The includePermission
*/
public IncludePermission getIncludePermission() {
return includePermission;
}

/**
* 
* @param includePermission
* The includePermission
*/
public void setIncludePermission(IncludePermission includePermission) {
	this.includePermission = includePermission;
}

/**
* 
* @return
* The toStep
*/
public ToStep getToStep() {
return toStep;
}

/**
* 
* @param toStep
* The to_step
*/
public void setToStep(ToStep toStep) {
this.toStep = toStep;
}

}