package coffeemail.mail.file;

import coffeemail.exeptions.NoValidEmailAddressException;
import coffeemail.mail.Address;
import coffeemail.mail.Mail;
import coffeemail.user.Rank;
import coffeemail.user.User;

public class DefaultFileManager extends FileManager {

	@Override
	public void saveMail(Mail m) {
	}

	@Override
	public Mail loadMailbyID(Mail m) {
		return null;
	}

	@Override
	public Mail[] loadLastMailsbyUser(User u, int count) {
		return null;
	}

	@Override
	public void loadMailreceiver(Mail m) {

	}

	@Override
	public User[] getUsers() {
		User[] users = new User[1];
		try {
			users[0] = new User("demo", "demo", Rank.USER, new Address(
					"demo@example.com"));
		} catch (NoValidEmailAddressException e) {
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public User getUserbyEmail(Address a) {
		User user = null;
		try {
			user = new User("demo", "demo", Rank.USER, new Address(
					"demo@example.com"));
		} catch (NoValidEmailAddressException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User getUserbyTimedKey(String key) {
		User user = null;
		try {
			user = new User("demo", "demo", Rank.USER, new Address(
					"demo@example.com"));
		} catch (NoValidEmailAddressException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User getUserbyName(String name) {
		return null;
	}

}
