package com.rentswag.app.model;

public class AuthToken {

    private String token;
      private int roles;
	 private User usr;

	public AuthToken(String token, int roles, User usr) {
	this.token = token;
	this.roles = roles;
	this.usr = usr;
}

	public int getRoles() {
		return roles;
	}

	public void setRoles(int roles) {
		this.roles = roles;
	}

	public User getUsr() {
		return usr;
	}

	public void setUsr(User usr) {
		this.usr = usr;
	}


    public AuthToken(){

    }

    public AuthToken(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
