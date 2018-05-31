package com.ezhealthtrack.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderField {

@SerializedName("order_id")
@Expose
private String orderId;
@SerializedName("custom_field_id")
@Expose
private String customFieldId;
@SerializedName("custom_field_value_id")
@Expose
private String customFieldValueId;
@Expose
private String name;
@Expose
private String value;
@SerializedName("sort_order")
@Expose
private Object sortOrder;

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
* The customFieldId
*/
public String getCustomFieldId() {
return customFieldId;
}

/**
* 
* @param customFieldId
* The custom_field_id
*/
public void setCustomFieldId(String customFieldId) {
this.customFieldId = customFieldId;
}

/**
* 
* @return
* The customFieldValueId
*/
public String getCustomFieldValueId() {
return customFieldValueId;
}

/**
* 
* @param customFieldValueId
* The custom_field_value_id
*/
public void setCustomFieldValueId(String customFieldValueId) {
this.customFieldValueId = customFieldValueId;
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
* The value
*/
public String getValue() {
return value;
}

/**
* 
* @param value
* The value
*/
public void setValue(String value) {
this.value = value;
}

/**
* 
* @return
* The sortOrder
*/
public Object getSortOrder() {
return sortOrder;
}

/**
* 
* @param sortOrder
* The sort_order
*/
public void setSortOrder(Object sortOrder) {
this.sortOrder = sortOrder;
}

}