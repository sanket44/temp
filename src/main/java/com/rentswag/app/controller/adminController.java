package com.rentswag.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rentswag.app.model.Order;
import com.rentswag.app.service.impl.OrderServiceImpl;


@Controller
@RequestMapping("/admin")
public class adminController {
	
	   @Autowired
	    private OrderServiceImpl orderserviceImpl;
	@RequestMapping(value="/allListOfOrder", method = RequestMethod.POST)
    public List<Order> saveListOfOrder(@RequestBody List<Order> orderList ){
    	System.out.println("ddfndsfkdsf");
        return orderserviceImpl.saveAllOrders(orderList);
    }
}
