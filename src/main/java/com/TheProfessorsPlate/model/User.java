package com.TheProfessorsPlate.model;

public class User {
    private int userId;
    private String userName;
    private String userPassword;
    private String userRole;
    private String userEmail;
    private String phoneNumber;
    private String userImage;

    // Default constructor
    public User() {}
    
    public User(String userName, String userPasword) {
		this.userName = userName;
		this.userPassword = userPasword;
	}
    
    
    public User(int userId, String userName, String userPassword, String userRole, String userEmail, String phoneNumber,
    		String userImage) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userRole = userRole;
		this.userEmail = userEmail;
		this.phoneNumber = phoneNumber;
		this.userImage = userImage;
	}
    
	public User(String userName, String userPassword, String userRole, String userEmail, String phoneNumber,
			String userImage) {
		super();
		this.userName = userName;
		this.userPassword = userPassword;
		this.userRole = userRole;
		this.userEmail = userEmail;
		this.phoneNumber = phoneNumber;
		this.userImage = userImage;
	}

	// Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}