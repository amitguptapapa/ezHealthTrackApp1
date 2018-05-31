package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;

public class Consultation_Charges {

@Expose
private String initial_visit="";
@Expose
private String repeat_visit="";
private String issueDate;
private String expiryDate;

public String getInitial_visit() {
return initial_visit;
}

public void setInitial_visit(String initial_visit) {
this.initial_visit = initial_visit;
}

public String getRepeat_visit() {
return repeat_visit;
}

public void setRepeat_visit(String repeat_visit) {
this.repeat_visit = repeat_visit;
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

