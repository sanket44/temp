package com.rentswag.app.service.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rentswag.app.dao.OrderDao;
import com.rentswag.app.model.Order;

import com.rentswag.app.service.OrderService;
@Service
public class OrderServiceImpl implements OrderService{
	 @Autowired
	 private OrderDao orderdao;
	
	@Override
	public int getMaxOrderNum() {
	
		return 0;
	}

	@Override
	public Order saveOrder(Order orderinfo) {
		return orderdao.save(orderinfo);
	}

	@Override
	public List<Order> listOrderInfo() {
		// TODO Auto-generated method stub
		return orderdao.findAll();
	}

	@Override
	public Order findOrder(String orderId) {
		
		return orderdao.findOrderByid(orderId) ;
	}

	public List<Order> saveAllOrders(Iterable<Order> orderList) {
		List<Order> response=(List<Order>)orderdao.saveAll(orderList);
		return response;
	}

}
