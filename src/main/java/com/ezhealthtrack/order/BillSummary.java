package com.ezhealthtrack.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BillSummary {

@SerializedName("total_amount")
@Expose
private Double totalAmount;
@SerializedName("balance_amount")
@Expose
private Double balanceAmount;
@SerializedName("discount_amount")
@Expose
private Double discountAmount;
@SerializedName("tax_amount")
@Expose
private Double taxAmount;
@SerializedName("original_amount")
@Expose
private Double originalAmount;

/**
* 
* @return
* The totalAmount
*/
public Double getTotalAmount() {
return totalAmount;
}

/**
* 
* @param totalAmount
* The total_amount
*/
public void setTotalAmount(Double totalAmount) {
this.totalAmount = totalAmount;
}

/**
* 
* @return
* The balanceAmount
*/
public Double getBalanceAmount() {
return balanceAmount;
}

/**
* 
* @param balanceAmount
* The balance_amount
*/
public void setBalanceAmount(Double balanceAmount) {
this.balanceAmount = balanceAmount;
}

/**
* 
* @return
* The discountAmount
*/
public Double getDiscountAmount() {
return discountAmount;
}

/**
* 
* @param discountAmount
* The discount_amount
*/
public void setDiscountAmount(Double discountAmount) {
this.discountAmount = discountAmount;
}

/**
* 
* @return
* The taxAmount
*/
public Double getTaxAmount() {
return taxAmount;
}

/**
* 
* @param taxAmount
* The tax_amount
*/
public void setTaxAmount(Double taxAmount) {
this.taxAmount = taxAmount;
}

/**
* 
* @return
* The originalAmount
*/
public Double getOriginalAmount() {
return originalAmount;
}

/**
* 
* @param originalAmount
* The original_amount
*/
public void setOriginalAmount(Double originalAmount) {
this.originalAmount = originalAmount;
}

}