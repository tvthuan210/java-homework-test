package com.test.homework.model;

import javax.persistence.*;

@Entity
@Table(name = "purchase_order")
public class Order {

  @Id
  @GeneratedValue
  @Column(columnDefinition = "BIGINT")
  private long id;

  @Column
  private long orderId;

  @Column
  private String item;

  @Column
  private String vendor;

  @Column
  private int quantity;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

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

  public String getVendor() {
    return vendor;
  }

  public void setVendor(String vendor) {
    this.vendor = vendor;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return "Order{" +
        "id=" + id +
        ", orderId=" + orderId +
        ", item='" + item + '\'' +
        ", vendor='" + vendor + '\'' +
        ", quantity=" + quantity +
        '}';
  }
}
