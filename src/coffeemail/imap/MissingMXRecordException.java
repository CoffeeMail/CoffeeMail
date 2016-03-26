package coffeemail.imap;

import java.io.IOException;

public class MissingMXRecordException extends IOException {

	private static final long serialVersionUID = -728616066744295374L;
	private String domain;

	public MissingMXRecordException(String domain) {
		this.domain = domain;
	}

	@Override
	public String getMessage() {
		return "No MX-Record found for the doamin \"" + domain + "\"!";
	}
}
