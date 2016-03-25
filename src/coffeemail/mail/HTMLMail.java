package coffeemail.mail;

import java.util.List;

public class HTMLMail extends Mail {

	public HTMLMail(Address sender, Address receiver, String subject,
			List<String> lines) {
		super(sender, receiver, subject, lines);
	}

}
