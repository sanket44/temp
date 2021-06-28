package com.rentswag.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rentswag.app.model.Product;

@Repository
public interface ProductDao extends JpaRepository<Product,Integer>{
			//Product findRoleByid(int id);
			@Query("Select e from Product e Where e.code=123")
			public Product findProduct( String code);
			
			//  public ProductInfo findProductInfo(String code) ;
			  //@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
//			   public void save(ProductForm productForm);
//			   public List<Product> listOrderInfo();
}
