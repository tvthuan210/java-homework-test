package com.test.homework.repository;

import com.test.homework.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  @Query(value = "select distinct(vendor) from Order")
  List<String> findVendors();
//
//  Page<Order> findOrdersByVendor(String name, Pageable pageable);
}
