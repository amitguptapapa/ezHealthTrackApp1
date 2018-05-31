package com.ezhealthtrack.order;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class WorkFlow {

@Expose
private String tenant;
@Expose
private List<Step> steps = new ArrayList<Step>();

/**
* 
* @return
* The tenant
*/
public String getTenant() {
return tenant;
}

/**
* 
* @param tenant
* The tenant
*/
public void setTenant(String tenant) {
this.tenant = tenant;
}

/**
* 
* @return
* The steps
*/
public List<Step> getSteps() {
return steps;
}

/**
* 
* @param steps
* The steps
*/
public void setSteps(List<Step> steps) {
this.steps = steps;
}

public Step getStepByName(String name){
	for(Step step:steps){
		if(step.getLabel().equalsIgnoreCase(name))
			return step;
	}
	return null;
}

}