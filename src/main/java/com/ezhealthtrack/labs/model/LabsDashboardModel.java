package com.ezhealthtrack.labs.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabsDashboardModel {

@SerializedName("Appointments_Messages")
@Expose
private List<String> appointmentsMessages = new ArrayList<String>();
@SerializedName("Medicine_Research")
@Expose
private List<String> medicineResearch = new ArrayList<String>();
@SerializedName("Lab_Tests")
@Expose
private List<String> labTests = new ArrayList<String>();
@SerializedName("ezHealthTrack_Information")
@Expose
private List<String> ezHealthTrackInformation = new ArrayList<String>();

public List<String> getAppointmentsMessages() {
return appointmentsMessages;
}

public void setAppointmentsMessages(List<String> appointmentsMessages) {
this.appointmentsMessages = appointmentsMessages;
}

public List<String> getMedicineResearch() {
return medicineResearch;
}

public void setMedicineResearch(List<String> medicineResearch) {
this.medicineResearch = medicineResearch;
}

public List<String> getLabTests() {
return labTests;
}

public void setLabTests(List<String> labTests) {
this.labTests = labTests;
}

public List<String> getEzHealthTrackInformation() {
return ezHealthTrackInformation;
}

public void setEzHealthTrackInformation(List<String> ezHealthTrackInformation) {
this.ezHealthTrackInformation = ezHealthTrackInformation;
}

}