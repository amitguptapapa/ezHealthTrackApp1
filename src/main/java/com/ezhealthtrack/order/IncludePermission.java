package com.ezhealthtrack.order;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class IncludePermission {

@Expose
private List<String> users = new ArrayList<String>();

/**
* 
* @return
* The users
*/
public List<String> getUsers() {
return users;
}

/**
* 
* @param users
* The users
*/
public void setUsers(List<String> users) {
this.users = users;
}

}