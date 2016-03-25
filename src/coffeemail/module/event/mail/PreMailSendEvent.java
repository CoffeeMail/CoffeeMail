package coffeemail.module.event.mail;

import coffeemail.MailServer;
import coffeemail.mail.Address;
import coffeemail.mail.Mail;
import coffeemail.module.event.CancelableEvent;

public class PreMailSendEvent extends CancelableEvent {

	private Mail mail;
	private Address address;

	public PreMailSendEvent(Mail mail, Address address) {
		this.mail = mail;
	}

	public PreMailSendEvent setMail(Mail mail) {
		this.mail = mail;
		return this;
	}

	public Mail getMail() {
		return mail;
	}

	public PreMailSendEvent setAddress(Address address) {
		this.address = address;
		return this;
	}

	public Address getAddress() {
		return address;
	}

	public PreMailSendEvent call() {
		MailServer.getModuleManager().getEventHandler().presendMailEvent(this);
		return this;
	}
}
