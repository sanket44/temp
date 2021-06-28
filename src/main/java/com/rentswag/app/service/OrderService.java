package com.rentswag.app.service;

import java.util.List;


import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.rentswag.app.model.Order;

@Service(value = "orderService")
public interface OrderService {
	 
	 public int getMaxOrderNum();
	 @Transactional(rollbackOn = Exception.class)
	 public Order saveOrder(Order orderinfo) ;
	 public List<Order> listOrderInfo();
	 
	  public Order findOrder(String orderId);
	
	// <S extends Order> List<Order> saveAll(Iterable<Order> entities);
}
	
