package coffeemail.exeptions;

import java.io.IOException;

public class MailReceiveException extends IOException {

	private static final long serialVersionUID = 4219072025712282463L;
	private String error;

	public MailReceiveException(String error) {
		this.error = error;
	}

	@Override
	public String getMessage() {
		return error;
	}
}
