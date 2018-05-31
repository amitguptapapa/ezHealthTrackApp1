package com.ezhealthtrack.order;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Step {

@Expose
private String name;
@Expose
private String label;
@Expose
private List<String> permission = new ArrayList<String>();
@Expose
private Boolean editable;
@Expose
private String view;
@Expose
private List<Transition> transitions = new ArrayList<Transition>();
@Expose
private String notifications;

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
* The editable
*/
public Boolean getEditable() {
return editable;
}

/**
* 
* @param editable
* The editable
*/
public void setEditable(Boolean editable) {
this.editable = editable;
}

/**
* 
* @return
* The view
*/
public String getView() {
return view;
}

/**
* 
* @param view
* The view
*/
public void setView(String view) {
this.view = view;
}

/**
* 
* @return
* The transitions
*/
public List<Transition> getTransitions() {
return transitions;
}

/**
* 
* @param transitions
* The transitions
*/
public void setTransitions(List<Transition> transitions) {
this.transitions = transitions;
}

/**
* 
* @return
* The notifications
*/
public String getNotifications() {
return notifications;
}

/**
* 
* @param notifications
* The notifications
*/
public void setNotifications(String notifications) {
this.notifications = notifications;
}

}