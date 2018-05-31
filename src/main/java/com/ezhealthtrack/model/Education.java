package com.ezhealthtrack.model;

import java.util.Date;

import com.google.gson.annotations.Expose;


public class Education {

@Expose
private String eid;
@Expose
private String degree;
@Expose
private String university;
@Expose
private String datepass;
@Expose
private String intern;
private String issueDate;
private String expiryDate;

public String getEid() {
return eid;
}

public void setEid(String eid) {
this.eid = eid;
}

public String getDegree() {
return degree;
}

public void setDegree(String degree) {
this.degree = degree;
}

public String getUniversity() {
return university;
}

public void setUniversity(String university) {
this.university = university;
}

public String getDatepass() {
return datepass;
}

public void setDatepass(String datepass) {
this.datepass = datepass;
}

public String getIntern() {
return intern;
}

public void setIntern(String intern) {
this.intern = intern;
}
public String getIssueDate() {
return issueDate;
}

public void setIssueDate(String issueDate) {
this.issueDate = issueDate;
}
public String getExpiryDate() {
return expiryDate;
}

public void setExpiryDate(String expiryDate) {
this.expiryDate = expiryDate;
}


}

