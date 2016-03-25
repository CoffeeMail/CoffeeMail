package coffeemail.module.event.mail;

import coffeemail.MailServer;
import coffeemail.mail.Mail;
import coffeemail.module.event.Event;

public class MailReceiveEvent extends Event {

	private Mail mail;

	public MailReceiveEvent(Mail mail) {
		this.mail = mail;
	}

	public Mail getMail() {
		return mail;
	}

	public MailReceiveEvent call() {
		MailServer.getModuleManager().getEventHandler().receiveMailEvent(this);
		return this;
	}
}
