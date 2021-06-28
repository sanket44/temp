package com.rentswag.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rentswag.app.model.User;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
	  User findUserByUsername(String username);
	  User findByUsername(String username);
//	  @Query("select u FROM User u WHERE u.username =:username")

	  //	  List<User> fetchuser(String username);
	//  User save(User user);
	@Query("SELECT u FROM User u WHERE u.email = ?1")
	    public User findByEmail(String email); 
	   
	    public User findByResetPasswordToken(String token);
	  List<User> findAll();
	  User findById(long id);
	  @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
	    public User findByVerificationCode(String code);
	
}
