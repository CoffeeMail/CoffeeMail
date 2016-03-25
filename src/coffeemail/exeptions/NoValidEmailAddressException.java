package coffeemail.exeptions;

public class NoValidEmailAddressException extends Exception {

	private static final long serialVersionUID = 3204469057464787321L;
	private String email;

	public NoValidEmailAddressException(String email) {
		this.email = email;
	}

	@Override
	public String getMessage() {
		return "The EmailAddress \"" + email + "\" is not valid!";
	}
}
