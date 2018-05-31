package com.ezhealthtrack.order;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LabTest {

@Expose
private String id;
@Expose
private String name;
@Expose
private String aka;
@SerializedName("primary_category")
@Expose
private String primaryCategory;
@SerializedName("other_category")
@Expose
private String otherCategory;
@SerializedName("price_min")
@Expose
private String priceMin;
@SerializedName("price_max")
@Expose
private String priceMax;
@SerializedName("completion_time_min")
@Expose
private String completionTimeMin;
@SerializedName("completion_time_min_type")
@Expose
private String completionTimeMinType;
@SerializedName("completion_time_max")
@Expose
private String completionTimeMax;
@SerializedName("completion_time_max_type")
@Expose
private String completionTimeMaxType;
@SerializedName("technical_info")
@Expose
private String technicalInfo;
@SerializedName("patient_info")
@Expose
private String patientInfo;
@SerializedName("lonic_code")
@Expose
private String lonicCode;
@SerializedName("lonic_num")
@Expose
private String lonicNum;
@Expose
private String tenant;
@SerializedName("sample_meta")
@Expose
private List<SampleMetum> sampleMeta = new ArrayList<SampleMetum>();
@SerializedName("created_on")
@Expose
private String createdOn;
@SerializedName("created_by")
@Expose
private String createdBy;
@SerializedName("updated_on")
@Expose
private String updatedOn;
@SerializedName("updated_by")
@Expose
private String updatedBy;
@Expose
private String status;

/**
* 
* @return
* The id
*/
public String getId() {
return id;
}

/**
* 
* @param id
* The id
*/
public void setId(String id) {
this.id = id;
}

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
* The aka
*/
public String getAka() {
return aka;
}

/**
* 
* @param aka
* The aka
*/
public void setAka(String aka) {
this.aka = aka;
}

/**
* 
* @return
* The primaryCategory
*/
public String getPrimaryCategory() {
return primaryCategory;
}

/**
* 
* @param primaryCategory
* The primary_category
*/
public void setPrimaryCategory(String primaryCategory) {
this.primaryCategory = primaryCategory;
}

/**
* 
* @return
* The otherCategory
*/
public String getOtherCategory() {
return otherCategory;
}

/**
* 
* @param otherCategory
* The other_category
*/
public void setOtherCategory(String otherCategory) {
this.otherCategory = otherCategory;
}

/**
* 
* @return
* The priceMin
*/
public String getPriceMin() {
return priceMin;
}

/**
* 
* @param priceMin
* The price_min
*/
public void setPriceMin(String priceMin) {
this.priceMin = priceMin;
}

/**
* 
* @return
* The priceMax
*/
public String getPriceMax() {
return priceMax;
}

/**
* 
* @param priceMax
* The price_max
*/
public void setPriceMax(String priceMax) {
this.priceMax = priceMax;
}

/**
* 
* @return
* The completionTimeMin
*/
public String getCompletionTimeMin() {
return completionTimeMin;
}

/**
* 
* @param completionTimeMin
* The completion_time_min
*/
public void setCompletionTimeMin(String completionTimeMin) {
this.completionTimeMin = completionTimeMin;
}

/**
* 
* @return
* The completionTimeMinType
*/
public String getCompletionTimeMinType() {
return completionTimeMinType;
}

/**
* 
* @param completionTimeMinType
* The completion_time_min_type
*/
public void setCompletionTimeMinType(String completionTimeMinType) {
this.completionTimeMinType = completionTimeMinType;
}

/**
* 
* @return
* The completionTimeMax
*/
public String getCompletionTimeMax() {
return completionTimeMax;
}

/**
* 
* @param completionTimeMax
* The completion_time_max
*/
public void setCompletionTimeMax(String completionTimeMax) {
this.completionTimeMax = completionTimeMax;
}

/**
* 
* @return
* The completionTimeMaxType
*/
public String getCompletionTimeMaxType() {
return completionTimeMaxType;
}

/**
* 
* @param completionTimeMaxType
* The completion_time_max_type
*/
public void setCompletionTimeMaxType(String completionTimeMaxType) {
this.completionTimeMaxType = completionTimeMaxType;
}

/**
* 
* @return
* The technicalInfo
*/
public String getTechnicalInfo() {
return technicalInfo;
}

/**
* 
* @param technicalInfo
* The technical_info
*/
public void setTechnicalInfo(String technicalInfo) {
this.technicalInfo = technicalInfo;
}

/**
* 
* @return
* The patientInfo
*/
public String getPatientInfo() {
return patientInfo;
}

/**
* 
* @param patientInfo
* The patient_info
*/
public void setPatientInfo(String patientInfo) {
this.patientInfo = patientInfo;
}

/**
* 
* @return
* The lonicCode
*/
public String getLonicCode() {
return lonicCode;
}

/**
* 
* @param lonicCode
* The lonic_code
*/
public void setLonicCode(String lonicCode) {
this.lonicCode = lonicCode;
}

/**
* 
* @return
* The lonicNum
*/
public String getLonicNum() {
return lonicNum;
}

/**
* 
* @param lonicNum
* The lonic_num
*/
public void setLonicNum(String lonicNum) {
this.lonicNum = lonicNum;
}

/**
* 
* @return
* The tenant
*/
public String getTenant() {
return tenant;
}

/**
* 
* @param tenant
* The tenant
*/
public void setTenant(String tenant) {
this.tenant = tenant;
}

/**
* 
* @return
* The sampleMeta
*/
public List<SampleMetum> getSampleMeta() {
return sampleMeta;
}

/**
* 
* @param sampleMeta
* The sample_meta
*/
public void setSampleMeta(List<SampleMetum> sampleMeta) {
this.sampleMeta = sampleMeta;
}

/**
* 
* @return
* The createdOn
*/
public String getCreatedOn() {
return createdOn;
}

/**
* 
* @param createdOn
* The created_on
*/
public void setCreatedOn(String createdOn) {
this.createdOn = createdOn;
}

/**
* 
* @return
* The createdBy
*/
public String getCreatedBy() {
return createdBy;
}

/**
* 
* @param createdBy
* The created_by
*/
public void setCreatedBy(String createdBy) {
this.createdBy = createdBy;
}

/**
* 
* @return
* The updatedOn
*/
public String getUpdatedOn() {
return updatedOn;
}

/**
* 
* @param updatedOn
* The updated_on
*/
public void setUpdatedOn(String updatedOn) {
this.updatedOn = updatedOn;
}

/**
* 
* @return
* The updatedBy
*/
public String getUpdatedBy() {
return updatedBy;
}

/**
* 
* @param updatedBy
* The updated_by
*/
public void setUpdatedBy(String updatedBy) {
this.updatedBy = updatedBy;
}

/**
* 
* @return
* The status
*/
public String getStatus() {
return status;
}

/**
* 
* @param status
* The status
*/
public void setStatus(String status) {
this.status = status;
}

}

