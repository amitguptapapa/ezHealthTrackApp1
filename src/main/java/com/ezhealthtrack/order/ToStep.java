package com.ezhealthtrack.order;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class ToStep {

@Expose
private String name;
@Expose
private List<Object> data = new ArrayList<Object>();

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
* The data
*/
public List<Object> getData() {
return data;
}

/**
* 
* @param data
* The data
*/
public void setData(List<Object> data) {
this.data = data;
}

}