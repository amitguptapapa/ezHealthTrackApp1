package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllergiesModel {

@SerializedName("a_id")
@Expose
private String aId;
@SerializedName("bk_id")
@Expose
private String bkId;
@SerializedName("m_cat_id")
@Expose
private String mCatId;
@SerializedName("s_cat_id")
@Expose
private String sCatId;
@SerializedName("m_cat_name")
@Expose
private String mCatName;
@SerializedName("s_cat_name")
@Expose
private String sCatName;
@SerializedName("addi_info")
@Expose
private String addiInfo;

public String getAId() {
return aId;
}

public void setAId(String aId) {
this.aId = aId;
}

public String getBkId() {
return bkId;
}

public void setBkId(String bkId) {
this.bkId = bkId;
}

public String getMCatId() {
return mCatId;
}

public void setMCatId(String mCatId) {
this.mCatId = mCatId;
}

public String getSCatId() {
return sCatId;
}

public void setSCatId(String sCatId) {
this.sCatId = sCatId;
}

public String getMCatName() {
return mCatName;
}

public void setMCatName(String mCatName) {
this.mCatName = mCatName;
}

public String getSCatName() {
return sCatName;
}

public void setSCatName(String sCatName) {
this.sCatName = sCatName;
}

public String getAddiInfo() {
return addiInfo;
}

public void setAddiInfo(String addiInfo) {
this.addiInfo = addiInfo;
}

}