package com.rentswag.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import net.bytebuddy.utility.RandomString;

import com.rentswag.app.config.TokenProvider;
import com.rentswag.app.dao.UserDao;
import com.rentswag.app.model.AuthToken;
import com.rentswag.app.model.LoginUser;
import com.rentswag.app.model.Order;
import com.rentswag.app.dao.OrderDao;
import com.rentswag.app.model.Product;
import com.rentswag.app.model.User;
import com.rentswag.app.model.UserDto;
import com.rentswag.app.service.impl.CustomerNotFoundException;
import com.rentswag.app.service.impl.OrderServiceImpl;
import com.rentswag.app.service.impl.ProductServiceImpl;
import com.rentswag.app.service.impl.UserServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

//    @Autowired
//    private UserService userService;
    @Autowired
    private UserServiceImpl userserviceimpl;
    @Autowired
    private ProductServiceImpl productimpl;
    @Autowired
    private OrderServiceImpl orderserviceImpl;
    @Autowired
    private UserDao userdao;
    @Autowired
    private OrderDao orderdao;
    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;
    @Autowired
    private JavaMailSender mailSender;
   

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody LoginUser loginUser) throws AuthenticationException {
    	User usr=userdao.findByUsername(loginUser.getUsername());
    	if(usr == null) {
    		return new ResponseEntity<>("Invalid username", HttpStatus.FORBIDDEN);
    	}

    	if(usr.isEnabled()) {
    		final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUser.getUsername(),
                            loginUser.getPassword()
                    )
            );
		
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtTokenUtil.generateToken(authentication);
		final int role=usr.getRoles().toArray().length;
            return ResponseEntity.ok(new AuthToken(token, role, usr));	
    	}
        
    	return new ResponseEntity<>("Not Verified Verify your email", HttpStatus.UNAUTHORIZED);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/adminping", method = RequestMethod.GET)
    public String adminPing(){
        return "Only Admins Can Read This";
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/userping", method = RequestMethod.GET)
    public String userPing(){
       return "Any User Can Read This";
   
    }
 
    @RequestMapping(value="/getallproduct", method = RequestMethod.GET)
    public List<Product>  findproductbtid(){
       return productimpl.listOrderInfo();
   
    }
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/getallorder", method = RequestMethod.GET)
    public List<Order> findallOrder(){
       return orderserviceImpl.listOrderInfo();
   
    }
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/addproducts", method = RequestMethod.POST)
    public List<Product> saveProducts(@RequestBody List<Product> productList ){
        return productimpl.saveAllProducts(productList);
    }
    
    @RequestMapping(value="/listOfOrder", method = RequestMethod.POST)
    public List<Order> saveListOfOrder(@RequestBody  List<Order> orderList ){
        return orderserviceImpl.saveAllOrders(orderList);
    }
    @RequestMapping(value="/addtoOrder", method = RequestMethod.POST)
    public Order saveOrder(@RequestBody Order order ){
        return orderserviceImpl.saveOrder(order);
    }
    
    @RequestMapping(value="/fetchbyusername/{username}", method = RequestMethod.GET)
    public User fetchuserbyusername(@PathVariable String username ){
		return userdao.findByUsername(username); 
    
  }
	
  @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/changestatus/{id}/{status}", method = RequestMethod.GET)
    public Order  orderstatusupdate(@PathVariable int id,@PathVariable int status ){
       Order order= orderdao.findById(id);
//    	int stat= orderdao.updatestatus(id, status);
      System.out.println(order.getCustomerPhone()+order.getId());
      order.setStatus(status);
      return orderdao.save(order);
        
    }
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/fetchallUserAndAdmin", method = RequestMethod.GET)
    public List<User> fetchalluser(){
       return userserviceimpl.findAll();
  }
    
    @PostMapping("/register")
    public  ResponseEntity<?>  processRegister(@RequestBody   UserDto user, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {
    	User tempuser=userdao.findByUsername(user.getUsername());
    	if(tempuser != null) {
    		if(user.getUsername().equals(tempuser.getUsername()) &&  user.getEmail().equals(tempuser.getEmail()))
        	{
        		return new ResponseEntity<>("username already used", HttpStatus.UNAUTHORIZED);
        	}
    	}
    		System.out.println(user.getUserFromDto());
    	userserviceimpl.register(user, getSiteURL(request));       
    	return new ResponseEntity<>("Registered Sucessfully", HttpStatus.OK);
    }
    
	    @PostMapping("/updateDetails") 
	    public User updateuserinfo(@RequestBody UserDto usr ){
	       	User nUser=userdao.findById(usr.getId());
	    	 nUser.setPhone(usr.getPhone());
	    	 nUser.setAddress(usr.getAddress());
	    	 String encodedPassword = bcryptEncoder.encode(usr.getPassword());
	    	 nUser.setPassword(encodedPassword);
	    	 nUser.setUsername(usr.getUsername());
			return userdao.save(nUser); 
	    
	  }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }  
    
    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userserviceimpl.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }
    //###########################################################################
    @PostMapping("/forgot_password/{emailfromuser}")
    public String processForgotPassword(HttpServletRequest request,@PathVariable String emailfromuser) {
    	 String email = emailfromuser;
    	    String token = RandomString.make(30);
    	    
    	    
    	    try {
    	    	//useserviceimpl.updateResetPasswordToken(token, email);
    	    	User user = userdao.findByEmail(email);
    	    	
    	        if (user != null) {
    	        	//userDao.passreset(token, email);
    	        	System.out.println("in meeeeeeeeeeeeeeeeeeeeeeejijdjfd");
    	        	user.setResetPasswordToken(token);
    	        	System.out.println("in meeeeeeeeeeeeeeeeeeeeeeejijdjfd");
    	        	userdao.save(user);
    	            System.out.println("in meeeeeeeeeeeeeeeeeeeeeeejijdjfd");
    	        } else {
    	            throw new CustomerNotFoundException("Could not find any customer with the email " + email);
    	        }
    	        String resetPasswordLink = "Token="+token; //getSiteURL(request) + "/users/reset_password?token=" + token;
    	        sendEmail(email, resetPasswordLink);
    	        return "message We have sent a reset password link to your email. Please check.";
    	         
    	    } catch (CustomerNotFoundException ex) {
    	    	return"error"+ ex.getMessage();
    	    } catch (UnsupportedEncodingException | MessagingException e) {
    	    	return "error ,Error while sending email";
    	    }
    }
     
    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();              
        MimeMessageHelper helper = new MimeMessageHelper(message);
         
        helper.setFrom("rentswagco@gmail.com", "RentSwag");
        helper.setTo(recipientEmail);
         
        String subject = "Here's the link to reset your password";
         
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + link//+ "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
         
        helper.setSubject(subject);
         
        helper.setText(content, true);
         
        mailSender.send(message);
    }
    	
 
    
    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token) {
    	System.out.println("ssssssssssjjsl;cm");
       User user = userserviceimpl.getByResetPasswordToken(token);       
         
        if (user == null) {
        	System.out.println("gdgfdg");
            return "Invalid Token";
        }
         
        return "Sucessful";
    }
     
    @PostMapping("/reset_password/{token}/{password}")
    public String processResetPassword( @PathVariable String token,@PathVariable String password ) {

         
        User user = userserviceimpl.getByResetPasswordToken(token);
         
        if (user == null) {
        	
            return "Invalid Token";
        } else {           
        	userserviceimpl.updatePassword(user, password);
             
           return "You have successfully changed your password.";
        }
    }
    
}
