package com.ezhealthtrack.order;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;


public class Data {

@Expose
private List<OrderProduct> orderProducts = new ArrayList<OrderProduct>();

/**
* 
* @return
* The orderProducts
*/
public List<OrderProduct> getOrderProducts() {
return orderProducts;
}

/**
* 
* @param orderProducts
* The orderProducts
*/
public void setOrderProducts(List<OrderProduct> orderProducts) {
this.orderProducts = orderProducts;
}

}