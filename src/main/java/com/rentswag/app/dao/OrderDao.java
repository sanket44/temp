package com.rentswag.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentswag.app.model.Order;
@Repository
public interface OrderDao extends JpaRepository<Order,Integer>{
	//@Query("Select e from Order e Where e.id=123")	
	Order findOrderByid(String id);
	@Override
	List<Order> findAll();
	Order findById(int id);
//	@Override
//	default <S extends Order> List<S> saveAll(Iterable<S> entities) {
//		
//		return null;
//	}
	
	
	
	
//	@Query("insert into user (first_name, age) values (?, ?)";)
//	public void add(List<Order> orderList) {
//		  String sql = "insert into user (first_name, age) values (?, ?)";
//		  
//		  List<Object[]> userRows = new ArrayList<Object[]>();
//		  for (User user : userList) {
//		        userRows.add(new Object[] {user.getFirstName(), user.getAge()});
//		  }
//		  
//		   jdbcTemplate.batchUpdate(sql, userRows);
//		}
	
}
