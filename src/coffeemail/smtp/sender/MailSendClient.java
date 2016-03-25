package coffeemail.smtp.sender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.naming.NamingException;

import coffeemail.CoffeeMail;
import coffeemail.dns.Lookup;
import coffeemail.dns.MXRecord;
import coffeemail.exeptions.MailSendException;
import coffeemail.exeptions.MissingMXRecordException;
import coffeemail.mail.Address;
import coffeemail.mail.Mail;
import coffeemail.mail.MailAttribute;
import coffeemail.module.event.mail.MailSendEvent;
import coffeemail.module.event.mail.PreMailSendEvent;

public class MailSendClient implements Runnable {

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private Mail mail;
	private Address address;
	private boolean connected = true;

	public MailSendClient(Mail mail, Address address) {

		if (!new PreMailSendEvent(mail, address).call().isCancelled()) {
			try {
				this.socket = connect(address);
				socket.setSoTimeout(10 * 1000);
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				mail.prepareSend();
				this.mail = mail;
				this.address = address;
			} catch (IOException e) {
				CoffeeMail.error(e);
			}
		}
	}

	private Socket connect(Address address) throws MissingMXRecordException,
			MailSendException {

		Socket socket = null;

		String domain = address.getDomain();
		MXRecord[] records;
		try {

			records = Lookup.getMX(domain);
			if (records == null) {
				records = new MXRecord[0];
				throw new MissingMXRecordException(domain);
			}
		} catch (NamingException e) {
			throw new MailSendException(
					"TextParseException while looking up domian MX Entry: "
							+ e.getMessage());
		}

		for (MXRecord mxr : records) {
			try {
				socket = new Socket(mxr.getDomain(), mxr.getPort());
				return socket;
			} catch (Exception e) {
				throw new MailSendException("Connection to SMTP Server: "
						+ mxr.getDomain() + " failed with exception: "
						+ e.getMessage());
			}
		}
		throw new MailSendException(
				"Could not connect to any SMTP server for domain: " + domain);
	}

	@Override
	public void run() {
		try {
			handleRequest();
			disconnet();
		} catch (Exception e) {
			e.printStackTrace();
			disconnet();
		}
	}

	private void handleRequest() {
		if (getSMTPCode() == 220) {
			writeData("EHLO " + address.getDomain());
			int lastcode = getSMTPCode();
			if (lastcode != 250) {
				writeData("HELO " + address.getDomain());
				lastcode = getSMTPCode();
			}

			if (lastcode == 250) {
				writeData("MAIL FROM:<" + mail.getSender().getAddress() + ">");
			}
			if (getSMTPCode() == 250) {
				writeData("RCPT TO:<" + address.getAddress() + ">");
			}
			if (getSMTPCode() == 250) {
				writeData("DATA");
			}

			if (getSMTPCode() == 354) {

				writeData(new MailAttribute("To", mail.getReceiver()
						.getAddress()));

				if (mail.getCCReceiver().size() > 0) {
					String cc = "";
					for (Address address : mail.getCCReceiver()) {
						cc += address + ", ";
					}
					if (!cc.isEmpty()) {
						writeData(new MailAttribute("CC", cc.substring(0,
								cc.length() - 2)));
					}
				}

				for (MailAttribute ma : mail.getAttribute()) {
					writeData(ma);
				}
				writeMailContents();
				writeData(".");
			}
			if (getSMTPCode() == 250) {
				writeData("QUIT");
			}
			// if (getSMTPCode() == 221) {
			// System.out.println("done!");
			// }
		}
		CoffeeMail.debug("Send an Email to " + address.getAddress());
		new MailSendEvent(mail, address).call();
	}

	private int getSMTPCode() {
		try {
			return Integer.parseInt(readData().substring(0, 3));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private void disconnet() {
		if (connected) {
			try {
				if (socket != null) {
					socket.close();
				}
				connected = false;
				in.close();
				writeData("QUIT");
				out.flush();
				out.close();
			} catch (IOException e) {
				CoffeeMail.log(e.getMessage());
			}
		}
	}

	public Socket getSocket() {
		return socket;
	}

	private void writeData(MailAttribute attribute) {
		writeData(attribute.getAttribute());
	}

	private void writeMailContents() {
		mail.writeContents(this);
	}

	public void writeData(String message) {
		if (connected) {
			out.print(message + "\r\n");
			out.flush();
		}
	}

	private String readData() {
		if (connected && socket.isConnected()) {
			try {
				String input = in.readLine();
				if (input == null) {
					return "   ";
				}
				while (moreToRead(input)) {
					input = in.readLine().trim();
				}
				return input;
			} catch (IOException e) {
				disconnet();
			}
		} else {
			disconnet();
		}
		return "   ";
	}

	private boolean moreToRead(String input) {
		return input.length() >= 4 && input.toCharArray()[3] == '-';
	}

	public PrintWriter getPrintWriter() {
		return out;
	}

	public BufferedReader getBufferedReader() {
		return in;
	}
}