package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;

public class Publication {

@Expose
private String pid;
@Expose
private String pub_name;
@Expose
private String pub_desc;
@Expose
private String pub_date;
@Expose
private String pub_published;
@Expose
private String source;

public String getPid() {
return pid;
}

public void setPid(String pid) {
this.pid = pid;
}

public String getPub_name() {
return pub_name;
}

public void setPub_name(String pub_name) {
this.pub_name = pub_name;
}

public String getPub_desc() {
return pub_desc;
}

public void setPub_desc(String pub_desc) {
this.pub_desc = pub_desc;
}

public String getPub_date() {
return pub_date;
}

public void setPub_date(String pub_date) {
this.pub_date = pub_date;
}

public String getPub_published() {
return pub_published;
}

public void setPub_published(String pub_published) {
this.pub_published = pub_published;
}

public String getSource() {
return source;
}

public void setSource(String source) {
this.source = source;
}

}