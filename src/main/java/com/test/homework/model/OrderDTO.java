package com.test.homework.model;

import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

public class OrderDTO implements ResourceAware {

  private long orderId;
  private String item;
  private int quantity;
  private String vendor;
  private String date;
  private String fileName;

  public OrderDTO() {

  }

  public OrderDTO(long orderId, String item, int quantity, String vendor) {
    this.orderId = orderId;
    this.item = item;
    this.quantity = quantity;
    this.vendor = vendor;
  }

  public long getOrderId() {
    return orderId;
  }

  public void setOrderId(long id) {
    this.orderId = id;
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

  public String getVendor() {
    return vendor;
  }

  public void setVendor(String vendor) {
    this.vendor = vendor;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  @Override
  public String toString() {
    return "Order{" +
        "orderId=" + orderId +
        ", item='" + item + '\'' +
        ", quantity=" + quantity +
        ", vendor='" + vendor + '\'' +
        '}';
  }

  @Override
  public void setResource(Resource resource) {
    this.fileName = resource.getFilename();
  }
}
