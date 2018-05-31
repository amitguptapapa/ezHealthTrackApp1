package com.ezhealthtrack.model;

import java.util.Date;

import com.google.gson.annotations.Expose;

public class AppointmentCalenderModel {

@Expose
private Date aptDate;
@Expose
private String pfId;
@Expose
private String pid;
@Expose
private String reason;
@Expose
private String type;

/**
* 
* @return
* The aptDate
*/
public Date getAptDate() {
return aptDate;
}

/**
* 
* @param aptDate
* The aptDate
*/
public void setAptDate(Date aptDate) {
this.aptDate = aptDate;
}

/**
* 
* @return
* The pfId
*/
public String getPfId() {
return pfId;
}

/**
* 
* @param pfId
* The pfId
*/
public void setPfId(String pfId) {
this.pfId = pfId;
}

/**
* 
* @return
* The pid
*/
public String getPid() {
return pid;
}

/**
* 
* @param pid
* The pid
*/
public void setPid(String pid) {
this.pid = pid;
}

/**
* 
* @return
* The reason
*/
public String getReason() {
return reason;
}

/**
* 
* @param reason
* The reason
*/
public void setReason(String reason) {
this.reason = reason;
}

/**
* 
* @return
* The type
*/
public String getType() {
return type;
}

/**
* 
* @param type
* The type
*/
public void setType(String type) {
this.type = type;
}

}