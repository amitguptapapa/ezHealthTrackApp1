package com.ezhealthtrack.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileModel {

public boolean BRANCH_ADDR_AS_PATIENT_ADDR = false;
@SerializedName("Professional")
@Expose
private Professional professional = new Professional();
@SerializedName("Education")
@Expose
private List<Education> education = new ArrayList<Education>();
@SerializedName("Certifications")
@Expose
private List<Certification> certifications = new ArrayList<Certification>();
@SerializedName("Insurance")
@Expose
private List<Insurance> insurance = new ArrayList<Insurance>();
@SerializedName("Consultation_Charges")
@Expose
private Consultation_Charges consultation_Charges = new Consultation_Charges();
@SerializedName("Languages_Known")
@Expose
private List<String> languages_Known = new ArrayList<String>();
@SerializedName("Publications")
@Expose
private List<Publication> publications = new ArrayList<Publication>();

public Professional getProfessional() {
return professional;
}

public void setProfessional(Professional professional) {
this.professional = professional;
}

public List<Education> getEducation() {
return education;
}

public void setEducation(List<Education> education) {
this.education = education;
}

public List<Certification> getCertifications() {
return certifications;
}

public void setCertifications(List<Certification> certifications) {
this.certifications = certifications;
}

public List<Insurance> getInsurance() {
return insurance;
}

public void setInsurance(List<Insurance> insurance) {
this.insurance = insurance;
}

public Consultation_Charges getConsultation_Charges() {
return consultation_Charges;
}

public void setConsultation_Charges(Consultation_Charges consultation_Charges) {
this.consultation_Charges = consultation_Charges;
}

public List<String> getLanguages_Known() {
return languages_Known;
}

public void setLanguages_Known(List<String> languages_Known) {
this.languages_Known = languages_Known;
}

public List<Publication> getPublications() {
return publications;
}

public void setPublications(List<Publication> publications) {
this.publications = publications;
}

}

