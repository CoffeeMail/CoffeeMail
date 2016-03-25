package coffeemail.user;

import coffeemail.mail.Address;
import coffeemail.util.Util;

public class User {

	private String username;
	private String hashedpassword;
	private Rank rank;
	private Address[] emails;
	
	public User() {
		
	}
	
	public User(String username, String hashedpassword, Rank rank, Address... addresses) {
		
	}

	public String getUsername() {
		return username;
	}

	public String getHashedpassword() {
		return hashedpassword;
	}

	public Rank getRank() {
		return rank;
	}

	public Address[] getEmails() {
		return emails;
	}

	public boolean validLogin(String password) {
		return Util.getHash(password).equals(getHashedpassword());
	}

	public static User getUser(String name) {
		return null;
	}
}
