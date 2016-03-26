package coffeemail.exeptions;

import coffeemail.CoffeeMail;

public class NoValidEmailAddressException extends Exception {

	private static final long serialVersionUID = 3204469057464787321L;
	private String email;

	public NoValidEmailAddressException(String email) {
		this.email = email;
	}

	@Override
	public String getMessage() {
		return CoffeeMail.translate("exception.email.notvalid", email);
	}
}
