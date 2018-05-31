package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Document {

@Expose
private String id;
@Expose
private String did;
@SerializedName("d-name")
@Expose
private String dName;
@SerializedName("d-type")
@Expose
private String dType;
@Expose
private String date;
@Expose
private String section;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getDid() {
return did;
}

public void setDid(String did) {
this.did = did;
}

public String getDName() {
return dName;
}

public void setDName(String dName) {
this.dName = dName;
}

public String getDType() {
return dType;
}

public void setDType(String dType) {
this.dType = dType;
}

public String getDate() {
return date;
}

public void setDate(String date) {
this.date = date;
}

public String getSection() {
return section;
}

public void setSection(String section) {
this.section = section;
}

}