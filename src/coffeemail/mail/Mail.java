package coffeemail.mail;

import java.util.ArrayList;
import java.util.List;

import coffeemail.MailServer;
import coffeemail.mail.content.MailContent;
import coffeemail.smtp.sender.MailSendClient;

public class Mail {

	private Address sender;
	private Address receiver;
	private String subject;
	private List<Address> ccreceivers = new ArrayList<>();
	private List<Address> bccreceivers = new ArrayList<>();
	private List<MailAttribute> attribute = new ArrayList<>();
	private List<MailContent> content = new ArrayList<>();
	private List<String> lines = new ArrayList<>();
	private MailPriority priority = MailPriority.Normal;
	public boolean isprepared = false;

	public Mail() {

	}

	public Mail(Address sender, Address receiver, String subject,
			List<String> lines) {
		this.sender = sender;
		this.receiver = receiver;
		this.subject = subject;
		this.lines = lines;
	}

	public void addAttribute(MailAttribute mailAttribute) {
		attribute.add(mailAttribute);
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public Address setSender(Address sender) {
		return this.sender = sender;
	}

	public Address getSender() {
		return sender;
	}

	public Address getReceiver() {
		return receiver;
	}

	public List<Address> getCCReceiver() {
		return ccreceivers;
	}

	public List<Address> getBCCReceiver() {
		return bccreceivers;
	}

	public Mail addCCReceiver(Address address) {
		ccreceivers.add(address);
		return this;
	}

	public Mail addBCCReceiver(Address address) {
		ccreceivers.add(address);
		return this;
	}

	public Mail setReceiver(Address address) {
		receiver = address;
		return this;
	}

	public List<MailAttribute> getAttribute() {
		return attribute;
	}

	public void send() {
		MailServer.getMailSendManager().send(this);
	}

	public void addLine(String line) {
		if (lines == null) {
			lines = new ArrayList<String>();
		}
		lines.add(line);
	}

	public MailPriority getPriority() {
		return priority;
	}

	public void setPriority(MailPriority priority) {
		this.priority = priority;
	}

	public enum MailPriority {
		Highest(1), High(2), Normal(3), Low(4), Lowest(5);

		private int priority;

		MailPriority(int priority) {
			this.priority = priority;
		}

		public int getPriority() {
			return priority;
		}
	}

	public void prepareSend() {
		addAttribute(new MailAttribute("Return-Path", sender.getAddress()));
		addAttribute(new MailAttribute("From", sender.getName() + " <"
				+ sender.getAddress() + ">"));
		addAttribute(new MailAttribute("Subject", subject));
		addAttribute(new MailAttribute("MIME-Version", "1.0"));
		addAttribute(new MailAttribute("X-Priority", ""
				+ getPriority().getPriority()));
		addAttribute(new MailAttribute("Content-Type",
				"text/plain; charset=utf-8"));
		addAttribute(new MailAttribute("Content-Transfer-Encoding", "base64"));
	}

	public List<MailContent> getContents() {
		return content;
	}

	public void writeContents(MailSendClient mailSendClient) {
		for (String message : getLines()) {
			mailSendClient.writeData(message);
		}
	}
}
