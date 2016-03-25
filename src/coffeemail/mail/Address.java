package coffeemail.mail;

import coffeemail.exeptions.NoValidEmailAddressException;

public class Address {

	private String name;
	private String user;
	private String domain;

	public Address(String user, String domain) {
		this.user = user.trim();
		this.domain = domain.trim();
		this.name = getAddress();
	}

	public Address(String user, String domain, String name) {
		this.user = user.trim();
		this.domain = domain.trim();
		this.name = name.trim();
	}

	public Address(String email) throws NoValidEmailAddressException {
		if (email.indexOf('@') == -1) {
			throw new NoValidEmailAddressException(email);
		}
		this.user = email.substring(0, email.indexOf('@')).trim();
		this.domain = email.substring(email.indexOf('@') + 1).trim();
		this.name = getAddress();
	}

	public String getName() {
		return name;
	}

	public Address setName(String name) {
		this.name = name;
		return this;
	}

	public String getAddress() {
		return getUser() + "@" + getDomain();
	}

	public String getUser() {
		return user;
	}

	public String getDomain() {
		return domain;
	}

	@Override
	public String toString() {
		return getAddress();
	}
}
