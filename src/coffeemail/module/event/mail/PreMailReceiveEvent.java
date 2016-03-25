package coffeemail.module.event.mail;

import coffeemail.MailServer;
import coffeemail.mail.Address;
import coffeemail.module.event.Event;

public class PreMailReceiveEvent extends Event {

	private String senderip;
	private Address sendermail;
	private Address receivermail;

	public PreMailReceiveEvent(String senderip, Address sendermail,
			Address receivermail) {
		this.senderip = senderip;
		this.sendermail = sendermail;
		this.receivermail = receivermail;
	}

	public Address getSendermail() {
		return sendermail;
	}

	public void setSendermail(Address sendermail) {
		this.sendermail = sendermail;
	}

	public Address getReceivermail() {
		return receivermail;
	}

	public void setReceivermail(Address receivermail) {
		this.receivermail = receivermail;
	}

	public String getSenderip() {
		return senderip;
	}

	public PreMailReceiveEvent call() {
		MailServer.getModuleManager().getEventHandler()
				.prereceiveMailEvent(this);
		return this;
	}
}
