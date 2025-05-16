package com.cmr.streetfixer;

public class Users {
	String nameTextView, emailTextView, phoneTextView, addressTextView;

	public Users() {
	}
	public Users(String nameTextView, String emailTextView, String phoneTextView, String addressTextView) {
		this.nameTextView = nameTextView;
		this.emailTextView = emailTextView;
		this.phoneTextView = phoneTextView;
		this.addressTextView = addressTextView;
	}

	public String getNameTextView() {
		return nameTextView;
	}

	public void setNameTextView(String nameTextView) {
		this.nameTextView = nameTextView;
	}

	public String getEmailTextView() {
		return emailTextView;
	}

	public void setEmailTextView(String emailTextView) {
		this.emailTextView = emailTextView;
	}

	public String getPhoneTextView() {
		return phoneTextView;
	}

	public void setPhoneTextView(String phoneTextView) {
		this.phoneTextView = phoneTextView;
	}

	public String getAddressTextView() {
		return addressTextView;
	}

	public void setAddressTextView(String addressTextView) {
		this.addressTextView = addressTextView;
	}
}
