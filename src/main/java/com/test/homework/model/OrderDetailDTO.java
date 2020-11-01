package com.test.homework.model;

public class OrderDetailDTO {

  private long orderId;
  private String item;
  private int quantity;

  public long getOrderId() {
    return orderId;
  }

  public void setOrderId(long orderId) {
    this.orderId = orderId;
  }

  public String getItem() {
    return item;
  }

  public void setItem(String item) {
    this.item = item;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return this.orderId + "," + this.item + "," + this.quantity;
  }
}
