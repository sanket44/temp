package com.rentswag.app.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.rentswag.app.dao.UserDao;
import com.rentswag.app.model.Role;
import com.rentswag.app.model.User;
import com.rentswag.app.model.UserDto;
import com.rentswag.app.service.RoleService;
import com.rentswag.app.service.UserService;

import net.bytebuddy.utility.RandomString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private RoleService roleService;
     
    private User user;
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;
    
    //private UserDto userdto;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findUserByUsername(username);
        System.out.println("in loadbyusername");
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        userDao.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public User findOne(String username) {
		return null;
    }

    @Override
    public User save(UserDto user) {

        User nUser = user.getUserFromDto();
        nUser.setPassword(bcryptEncoder.encode(user.getPassword()));

        Role role = roleService.findByName("USER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        if(nUser.getEmail().split("@")[1].equals("admin.edu")){
            role = roleService.findByName("ADMIN");
            roleSet.add(role);
        }

        nUser.setRoles(roleSet);
        return userDao.save(nUser);
    }

	@Override
	public boolean isEnabled() {
		return user.isEnabled() ;
	}
	
	public void register(UserDto user, String siteURL) throws UnsupportedEncodingException, MessagingException{
		 User nUser = user.getUserFromDto();  
		String encodedPassword = bcryptEncoder.encode(user.getPassword());
		    nUser.setPassword(encodedPassword);
		     
		    String randomCode = RandomString.make(64);
		    nUser.setVerificationCode(randomCode);
		    nUser.setEnabled(false);
		    Role role = roleService.findByName("USER");
	        Set<Role> roleSet = new HashSet<>();
	        roleSet.add(role);

	        if(nUser.getEmail().split("@")[1].equals("rentswag.com")){
	            role = roleService.findByName("ADMIN");
	            roleSet.add(role);
	        }

	        nUser.setRoles(roleSet);
		     
		    userDao.save(nUser);
		     
		    sendVerificationEmail(nUser, siteURL);
    }
     
    private void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException{
    	 String toAddress = user.getEmail();
    	    String fromAddress = "rentswagco@gmail.com";
    	    String senderName = "RentSwag";
    	    String subject = "Please verify your registration";
    	    String content = "Dear [[name]],<br>"
    	            + "Please click the link below to verify your registration:<br>"
    	            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
    	            + "Thank you,<br>"
    	            + "RentSwag";
    	     
    	    MimeMessage message = mailSender.createMimeMessage();
    	    MimeMessageHelper helper = new MimeMessageHelper(message);
    	     
    	    helper.setFrom(fromAddress, senderName);
    	    helper.setTo(toAddress);
    	    helper.setSubject(subject);
    	     
    	    content = content.replace("[[name]]", user.getCname());
    	    String verifyURL =siteURL +"/users"+ "/verify?code=" + user.getVerificationCode();
    	     
    	    content = content.replace("[[URL]]", verifyURL);
    	     
    	    helper.setText(content, true);
    	     
    	    mailSender.send(message);
    }

	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean verify(String verificationCode) {
	    User user = userDao.findByVerificationCode(verificationCode);
	     System.out.println(user.getCname());
	    if (user == null || user.isEnabled()) {
	        return false;
	    } else {
	        user.setVerificationCode(null);
	        user.setEnabled(true);
	        userDao.save(user);
	         
	        return true;
	    }
	     
	}
	public User getByResetPasswordToken(String token) {
	        return userDao.findByResetPasswordToken(token);
	    }
	     
	    public void updatePassword(User user, String newPassword) {
	        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        String encodedPassword = passwordEncoder.encode(newPassword);
	        user.setPassword(encodedPassword);
	         
	        user.setResetPasswordToken(null);
	        userDao.save(user);
	    }
	
}
