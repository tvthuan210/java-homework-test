package com.test.homework.processor;

import com.test.homework.model.Order;
import com.test.homework.model.OrderDTO;
import org.springframework.batch.item.ItemProcessor;

public class OrderProcessor implements ItemProcessor<OrderDTO, Order> {

  @Override
  public Order process(OrderDTO orderDTO) {
    Order order = new Order();
    order.setItem(orderDTO.getItem());
    order.setQuantity(orderDTO.getQuantity());
    order.setVendor(orderDTO.getVendor());
    order.setOrderId(orderDTO.getOrderId());
    return order;
  }
}
