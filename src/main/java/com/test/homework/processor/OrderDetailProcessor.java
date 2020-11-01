package com.test.homework.processor;

import com.test.homework.model.Order;
import com.test.homework.model.OrderDetailDTO;
import org.springframework.batch.item.ItemProcessor;

public class OrderDetailProcessor implements ItemProcessor<Order, OrderDetailDTO> {

  @Override
  public OrderDetailDTO process(Order order) {
    OrderDetailDTO orderDetail = new OrderDetailDTO();
    orderDetail.setOrderId(order.getOrderId());
    orderDetail.setItem(order.getItem());
    orderDetail.setQuantity(order.getQuantity());
    return orderDetail;
  }
}
