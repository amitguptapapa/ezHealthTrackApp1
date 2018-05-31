package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;

public class Frequency {

@Expose
private String frequencyName;
@Expose
private String frequencyDescription;
@Expose
private String frequencyQuantity;
@Expose
private String frequencyMultiplier;
@Expose
private String frequencyPeriod;

public String getFrequencyName() {
return frequencyName;
}

public void setFrequencyName(String frequencyName) {
this.frequencyName = frequencyName;
}

public String getFrequencyDescription() {
return frequencyDescription;
}

public void setFrequencyDescription(String frequencyDescription) {
this.frequencyDescription = frequencyDescription;
}

public String getFrequencyQuantity() {
return frequencyQuantity;
}

public void setFrequencyQuantity(String frequencyQuantity) {
this.frequencyQuantity = frequencyQuantity;
}

public String getFrequencyMultiplier() {
return frequencyMultiplier;
}

public void setFrequencyMultiplier(String frequencyMultiplier) {
this.frequencyMultiplier = frequencyMultiplier;
}

public String getFrequencyPeriod() {
return frequencyPeriod;
}

public void setFrequencyPeriod(String frequencyPeriod) {
this.frequencyPeriod = frequencyPeriod;
}

}