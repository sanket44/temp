package com.rentswag.app.service;

import java.util.List;


import com.rentswag.app.model.Product;


public interface ProductService {
	  public Product findProduct(String code);
	   public List<Product> listOrderInfo();
	  
}
