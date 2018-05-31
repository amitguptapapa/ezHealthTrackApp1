package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;

public class Insurance {

@Expose
private String iid;
@Expose
private String provider;
@Expose
private String desc;
@Expose
private String dateis;
@Expose
private String dateex;
@Expose
private String premium;
@Expose
private String ifile;

public String getIid() {
return iid;
}

public void setIid(String iid) {
this.iid = iid;
}

public String getProvider() {
return provider;
}

public void setProvider(String provider) {
this.provider = provider;
}

public String getDesc() {
return desc;
}

public void setDesc(String desc) {
this.desc = desc;
}

public String getDateis() {
return dateis;
}

public void setDateis(String dateis) {
this.dateis = dateis;
}

public String getDateex() {
return dateex;
}

public void setDateex(String dateex) {
this.dateex = dateex;
}

public String getPremium() {
return premium;
}

public void setPremium(String premium) {
this.premium = premium;
}

public String getIfile() {
return ifile;
}

public void setIfile(String ifile) {
this.ifile = ifile;
}

}

