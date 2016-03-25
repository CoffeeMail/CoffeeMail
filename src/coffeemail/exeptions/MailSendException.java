package coffeemail.exeptions;

import java.io.IOException;

public class MailSendException extends IOException {

	private static final long serialVersionUID = 444632598480155905L;
	private String error;

	public MailSendException(String error) {
		this.error = error;
	}

	@Override
	public String getMessage() {
		return error;
	}
}
