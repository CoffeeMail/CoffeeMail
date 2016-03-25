package coffeemail.mail.file;

import coffeemail.mail.Address;
import coffeemail.mail.Mail;
import coffeemail.user.User;

public abstract class FileManager {

	public abstract void saveMail(Mail m);

	public abstract Mail loadMailbyID(Mail m);

	public abstract Mail[] loadLastMailsbyUser(User u, int count);

	public abstract void loadMailreceiver(Mail m);

	public abstract User[] getUsers();

	public abstract User getUserbyEmail(Address a);

	public abstract User getUserbyTimedKey(String key);

	public abstract User getUserbyName(String name);
}