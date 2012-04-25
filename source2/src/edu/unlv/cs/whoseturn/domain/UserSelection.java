package edu.unlv.cs.whoseturn.domain;

/***
 * This class is for modeling users and an ephemeral selection state for use with the mobile web application
 */
public class UserSelection {
	public UserSelection(edu.unlv.cs.whoseturn.domain.User user,
			boolean selected) {
		this.user = user;
		this.selected = selected;
	}
	edu.unlv.cs.whoseturn.domain.User user;
	boolean selected;
	
	public String getKeyString() {
		return user.getKeyString();
	}
	
	public String getUsername() {
		return user.getUsername();
	}
	
	public boolean getSelected() {
		return selected;
	}
}
