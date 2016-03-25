package coffeemail.module.event.mail;

import coffeemail.MailServer;
import coffeemail.mail.Address;
import coffeemail.mail.Mail;
import coffeemail.module.event.Event;

public class MailSendEvent extends Event {

	private Mail mail;
	private Address address;

	public MailSendEvent(Mail mail, Address address) {
		this.mail = mail;
		this.address = address;
	}

	public Mail getMail() {
		return mail;
	}

	public Address getAddress() {
		return address;
	}

	public MailSendEvent call() {
		MailServer.getModuleManager().getEventHandler().sendMailEvent(this);
		return this;
	}
}
