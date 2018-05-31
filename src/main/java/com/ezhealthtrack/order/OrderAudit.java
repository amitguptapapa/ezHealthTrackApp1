package com.ezhealthtrack.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderAudit {

@SerializedName("order_audit_id")
@Expose
private String orderAuditId;
@SerializedName("order_id")
@Expose
private String orderId;
@SerializedName("store_id")
@Expose
private String storeId;
@SerializedName("invoice_no")
@Expose
private String invoiceNo;
@SerializedName("store_name")
@Expose
private String storeName;
@SerializedName("store_url")
@Expose
private String storeUrl;
@Expose
private String firstname;
@Expose
private String lastname;
@Expose
private String email;
@SerializedName("order_status_id")
@Expose
private String orderStatusId;
@SerializedName("bill_status_id")
@Expose
private String billStatusId;
@Expose
private String ip;
@SerializedName("user_agent")
@Expose
private String userAgent;
@SerializedName("date_added")
@Expose
private String dateAdded;
@SerializedName("date_modified")
@Expose
private String dateModified;
@Expose
private String total;
@SerializedName("customer_id")
@Expose
private String customerId;
@SerializedName("currency_id")
@Expose
private String currencyId;
@SerializedName("currency_code")
@Expose
private String currencyCode;
@SerializedName("order_display_id")
@Expose
private String orderDisplayId;
@SerializedName("balance_amount")
@Expose
private String balanceAmount;
@SerializedName("paid_amount")
@Expose
private String paidAmount;
@SerializedName("paid_on")
@Expose
private String paidOn;
@Expose
private String comment;
@SerializedName("payment_method")
@Expose
private String paymentMethod;
@SerializedName("discount_percentage")
@Expose
private String discountPercentage;
@SerializedName("discount_amount")
@Expose
private String discountAmount;
@SerializedName("tax_percentage")
@Expose
private String taxPercentage;
@SerializedName("tax_amount")
@Expose
private String taxAmount;
@SerializedName("refund_amount")
@Expose
private String refundAmount;
@SerializedName("total_amount")
@Expose
private String totalAmount;
@SerializedName("generate_bill")
@Expose
private String generateBill;

/**
* 
* @return
* The orderAuditId
*/
public String getOrderAuditId() {
return orderAuditId;
}

/**
* 
* @param orderAuditId
* The order_audit_id
*/
public void setOrderAuditId(String orderAuditId) {
this.orderAuditId = orderAuditId;
}

/**
* 
* @return
* The orderId
*/
public String getOrderId() {
return orderId;
}

/**
* 
* @param orderId
* The order_id
*/
public void setOrderId(String orderId) {
this.orderId = orderId;
}

/**
* 
* @return
* The storeId
*/
public String getStoreId() {
return storeId;
}

/**
* 
* @param storeId
* The store_id
*/
public void setStoreId(String storeId) {
this.storeId = storeId;
}

/**
* 
* @return
* The invoiceNo
*/
public String getInvoiceNo() {
return invoiceNo;
}

/**
* 
* @param invoiceNo
* The invoice_no
*/
public void setInvoiceNo(String invoiceNo) {
this.invoiceNo = invoiceNo;
}

/**
* 
* @return
* The storeName
*/
public String getStoreName() {
return storeName;
}

/**
* 
* @param storeName
* The store_name
*/
public void setStoreName(String storeName) {
this.storeName = storeName;
}

/**
* 
* @return
* The storeUrl
*/
public String getStoreUrl() {
return storeUrl;
}

/**
* 
* @param storeUrl
* The store_url
*/
public void setStoreUrl(String storeUrl) {
this.storeUrl = storeUrl;
}

/**
* 
* @return
* The firstname
*/
public String getFirstname() {
return firstname;
}

/**
* 
* @param firstname
* The firstname
*/
public void setFirstname(String firstname) {
this.firstname = firstname;
}

/**
* 
* @return
* The lastname
*/
public String getLastname() {
return lastname;
}

/**
* 
* @param lastname
* The lastname
*/
public void setLastname(String lastname) {
this.lastname = lastname;
}

/**
* 
* @return
* The email
*/
public String getEmail() {
return email;
}

/**
* 
* @param email
* The email
*/
public void setEmail(String email) {
this.email = email;
}

/**
* 
* @return
* The orderStatusId
*/
public String getOrderStatusId() {
return orderStatusId;
}

/**
* 
* @param orderStatusId
* The order_status_id
*/
public void setOrderStatusId(String orderStatusId) {
this.orderStatusId = orderStatusId;
}

/**
* 
* @return
* The billStatusId
*/
public String getBillStatusId() {
return billStatusId;
}

/**
* 
* @param billStatusId
* The bill_status_id
*/
public void setBillStatusId(String billStatusId) {
this.billStatusId = billStatusId;
}

/**
* 
* @return
* The ip
*/
public String getIp() {
return ip;
}

/**
* 
* @param ip
* The ip
*/
public void setIp(String ip) {
this.ip = ip;
}

/**
* 
* @return
* The userAgent
*/
public String getUserAgent() {
return userAgent;
}

/**
* 
* @param userAgent
* The user_agent
*/
public void setUserAgent(String userAgent) {
this.userAgent = userAgent;
}

/**
* 
* @return
* The dateAdded
*/
public String getDateAdded() {
return dateAdded;
}

/**
* 
* @param dateAdded
* The date_added
*/
public void setDateAdded(String dateAdded) {
this.dateAdded = dateAdded;
}

/**
* 
* @return
* The dateModified
*/
public String getDateModified() {
return dateModified;
}

/**
* 
* @param dateModified
* The date_modified
*/
public void setDateModified(String dateModified) {
this.dateModified = dateModified;
}

/**
* 
* @return
* The total
*/
public String getTotal() {
return total;
}

/**
* 
* @param total
* The total
*/
public void setTotal(String total) {
this.total = total;
}

/**
* 
* @return
* The customerId
*/
public String getCustomerId() {
return customerId;
}

/**
* 
* @param customerId
* The customer_id
*/
public void setCustomerId(String customerId) {
this.customerId = customerId;
}

/**
* 
* @return
* The currencyId
*/
public String getCurrencyId() {
return currencyId;
}

/**
* 
* @param currencyId
* The currency_id
*/
public void setCurrencyId(String currencyId) {
this.currencyId = currencyId;
}

/**
* 
* @return
* The currencyCode
*/
public String getCurrencyCode() {
return currencyCode;
}

/**
* 
* @param currencyCode
* The currency_code
*/
public void setCurrencyCode(String currencyCode) {
this.currencyCode = currencyCode;
}

/**
* 
* @return
* The orderDisplayId
*/
public String getOrderDisplayId() {
return orderDisplayId;
}

/**
* 
* @param orderDisplayId
* The order_display_id
*/
public void setOrderDisplayId(String orderDisplayId) {
this.orderDisplayId = orderDisplayId;
}

/**
* 
* @return
* The balanceAmount
*/
public String getBalanceAmount() {
return balanceAmount;
}

/**
* 
* @param balanceAmount
* The balance_amount
*/
public void setBalanceAmount(String balanceAmount) {
this.balanceAmount = balanceAmount;
}

/**
* 
* @return
* The paidAmount
*/
public String getPaidAmount() {
return paidAmount;
}

/**
* 
* @param paidAmount
* The paid_amount
*/
public void setPaidAmount(String paidAmount) {
this.paidAmount = paidAmount;
}

/**
* 
* @return
* The paidOn
*/
public String getPaidOn() {
return paidOn;
}

/**
* 
* @param paidOn
* The paid_on
*/
public void setPaidOn(String paidOn) {
this.paidOn = paidOn;
}

/**
* 
* @return
* The comment
*/
public String getComment() {
return comment;
}

/**
* 
* @param comment
* The comment
*/
public void setComment(String comment) {
this.comment = comment;
}

/**
* 
* @return
* The paymentMethod
*/
public String getPaymentMethod() {
return paymentMethod;
}

/**
* 
* @param paymentMethod
* The payment_method
*/
public void setPaymentMethod(String paymentMethod) {
this.paymentMethod = paymentMethod;
}

/**
* 
* @return
* The discountPercentage
*/
public String getDiscountPercentage() {
return discountPercentage;
}

/**
* 
* @param discountPercentage
* The discount_percentage
*/
public void setDiscountPercentage(String discountPercentage) {
this.discountPercentage = discountPercentage;
}

/**
* 
* @return
* The discountAmount
*/
public String getDiscountAmount() {
return discountAmount;
}

/**
* 
* @param discountAmount
* The discount_amount
*/
public void setDiscountAmount(String discountAmount) {
this.discountAmount = discountAmount;
}

/**
* 
* @return
* The taxPercentage
*/
public String getTaxPercentage() {
return taxPercentage;
}

/**
* 
* @param taxPercentage
* The tax_percentage
*/
public void setTaxPercentage(String taxPercentage) {
this.taxPercentage = taxPercentage;
}

/**
* 
* @return
* The taxAmount
*/
public String getTaxAmount() {
return taxAmount;
}

/**
* 
* @param taxAmount
* The tax_amount
*/
public void setTaxAmount(String taxAmount) {
this.taxAmount = taxAmount;
}

/**
* 
* @return
* The refundAmount
*/
public String getRefundAmount() {
return refundAmount;
}

/**
* 
* @param refundAmount
* The refund_amount
*/
public void setRefundAmount(String refundAmount) {
this.refundAmount = refundAmount;
}

/**
* 
* @return
* The totalAmount
*/
public String getTotalAmount() {
return totalAmount;
}

/**
* 
* @param totalAmount
* The total_amount
*/
public void setTotalAmount(String totalAmount) {
this.totalAmount = totalAmount;
}

/**
* 
* @return
* The generateBill
*/
public String getGenerateBill() {
return generateBill;
}

/**
* 
* @param generateBill
* The generate_bill
*/
public void setGenerateBill(String generateBill) {
this.generateBill = generateBill;
}

}