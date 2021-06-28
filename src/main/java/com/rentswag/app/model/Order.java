package com.rentswag.app.model;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "Orders" )//uniqueConstraints = { @UniqueConstraint(columnNames = "Order_Num") }
public class Order implements Serializable {
 
    private static final long serialVersionUID = -2576670215015463100L;
 
    @Id
    @GeneratedValue
    @Column(name = "ID", length = 50)
    private int id;
 
    @Column(name = "Order_from", nullable = false)
    private Date orderFrom;
    
    @Column(name = "Order_to", nullable = false)
    private Date orderTo;
 
    @Column(name = "Customer_Name", length = 255, nullable = false)
    private String customerName;
 
    @Column(name = "Customer_Address", length = 255, nullable = false)
    private String customerAddress;
 
    @Column(name = "Customer_Email", length = 128, nullable = false)
    private String customerEmail;
 
    @Column(name = "Customer_Phone", length = 128, nullable = false)
    private String customerPhone;
    
    @Column(name = "Quantity", nullable = false)
    private int quanity;
    
    @Column(name = "ProductCode", nullable = false)
    private int productcode;
    @Column(columnDefinition = "integer default 0 ")
    private int status; 
    
    public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

    public int getQuanity() {
		return quanity;
	}

	public void setQuanity(int quanity) {
		this.quanity = quanity;
	}

	public int getProductcode() {
		return productcode;
	}

	public void setProductcode(int productcode) {
		this.productcode = productcode;
	}

	@ManyToOne
    private User user;
    
	public void setUser(User user) {
		this.user = user;
	}

	public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public Date getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(Date orderFrom) {
		this.orderFrom = orderFrom;
	}

	public Date getOrderTo() {
		return orderTo;
	}

	public void setOrderTo(Date orderTo) {
		this.orderTo = orderTo;
	}

//	public int getOrderNum() {
//        return orderNum;
//    }
 
//    public void setOrderNum(int orderNum) {
//        this.orderNum = orderNum;
//    }
 
//    public double getAmount() {
//        return amount;
//    }
// 
//    public void setAmount(double amount) {
//        this.amount = amount;
//    }
 
    public String getCustomerName() {
        return customerName;
    }
 
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
 
    public String getCustomerAddress() {
        return customerAddress;
    }
 
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
 
    public String getCustomerEmail() {
        return customerEmail;
    }
 
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
 
    public String getCustomerPhone() {
        return customerPhone;
    }
 
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Order(int id, Date orderFrom, Date orderTo, String customerName, String customerAddress,
			String customerEmail, String customerPhone, int quanity, int productcode, int status) {
		super();
		this.id = id;
		this.orderFrom = orderFrom;
		this.orderTo = orderTo;
		this.customerName = customerName;
		this.customerAddress = customerAddress;
		this.customerEmail = customerEmail;
		this.customerPhone = customerPhone;
		this.quanity = quanity;
		this.productcode = productcode;
		this.status = status;
	}
	


    
    
}