
package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;


public class Certification {

@Expose
private String cid;
@Expose
private String certification;
@Expose
private String institute;
@Expose
private String datei;
@Expose
private String datee;
@Expose
private String cme;

public String getCid() {
return cid;
}

public void setCid(String cid) {
this.cid = cid;
}

public String getCertification() {
return certification;
}

public void setCertification(String certification) {
this.certification = certification;
}

public String getInstitute() {
return institute;
}

public void setInstitute(String institute) {
this.institute = institute;
}

public String getDatei() {
return datei;
}

public void setDatei(String datei) {
this.datei = datei;
}

public String getDatee() {
return datee;
}

public void setDatee(String datee) {
this.datee = datee;
}

public String getCme() {
return cme;
}

public void setCme(String cme) {
this.cme = cme;
}

}

