package coffeemail.smtp.receiver;

import java.io.IOException;
import java.net.Socket;

import coffeemail.CoffeeMail;
import coffeemail.exeptions.MailReceiveException;
import coffeemail.exeptions.NoValidEmailAddressException;
import coffeemail.mail.Address;
import coffeemail.mail.Mail;
import coffeemail.module.event.mail.MailReceiveEvent;
import coffeemail.server.ReceiveClient;

public class MailReceiveClient extends ReceiveClient {

	// private Socket socket;

	public MailReceiveClient(Socket socket) {
		super(socket);
		// try {
		// socket.setSoTimeout(10 * 1000);
		// in = new BufferedReader(new InputStreamReader(
		// socket.getInputStream()));
		// out = new PrintWriter(socket.getOutputStream(), true);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void handle(Socket socket) {
		writeData(SMTPCodes.WELCOME);
		try {
			boolean maildata = false;
			SMTPCommand lastcommand = SMTPCommand.NULL;
			Mail mail = new Mail();
			while (isConnected()) {
				String s = readData();
				CoffeeMail.deepdebug("<< " + s);
				if (s == null) {
					disconnet();
					return;
				}
				if (maildata) {
					if (s.equals(".")) {
						maildata = false;
						writeData(SMTPCodes.OK);
					} else {
						mail.addLine(s);
					}
				} else {
					SMTPCommand command = getSMTPCommand(s);
					switch (command) {
					case HELO:
						if (lastcommand != SMTPCommand.NULL) {
							throw new MailReceiveException(
									"SMTPCommand no as expected!");
						}
						lastcommand = SMTPCommand.EHLO;
						writeData("250 Hello " + getArgs(s));
						break;
					case EHLO:
						if (lastcommand != SMTPCommand.NULL) {
							throw new MailReceiveException(
									"SMTPCommand no as expected!");
						}
						lastcommand = SMTPCommand.EHLO;
						writeData("250 Hello " + getArgs(s));
						break;
					case MAIL:
						if (lastcommand != SMTPCommand.EHLO) {
							System.out.println(lastcommand);
							throw new MailReceiveException(
									"Invalid Sender-Address");
						}
						lastcommand = SMTPCommand.MAIL;
						writeData(SMTPCodes.OK);
						try {
							mail.setSender(new Address(s.substring(11,
									s.length() - 1)));
						} catch (NoValidEmailAddressException e) {
							e.printStackTrace();
						}
						break;
					case RCPT:
						if (lastcommand != SMTPCommand.MAIL) {
							throw new MailReceiveException(
									"SMTPCommand no as expected!");
						}
						lastcommand = SMTPCommand.RCPT;
						writeData(SMTPCodes.OK);
						try {
							mail.setReceiver(new Address(s.substring(9,
									s.length() - 1)));
						} catch (NoValidEmailAddressException e) {
							throw new MailReceiveException(
									"Invalid Receiver-Address");
						}
						break;
					case DATA:
						if (lastcommand != SMTPCommand.RCPT) {
							throw new MailReceiveException(
									"SMTPCommand no as expected!");
						}
						lastcommand = SMTPCommand.DATA;
						maildata = true;
						writeData(SMTPCodes.STARTDATA);
						break;
					case QUIT:
						maildata = true;
						disconnet();
						break;
					case ERROR:
						disconnet();
						break;
					default:
						disconnet();
					}
				}
			}

			CoffeeMail.debug("Receive an Email from " + mail.getSender());
			new MailReceiveEvent(mail).call();

		} catch (MailReceiveException e) {
			CoffeeMail.error(e);
		}

		disconnet();
	}

	private SMTPCommand getSMTPCommand(String s) {
		try {
			return SMTPCommand.valueOf(getCommand(s));
		} catch (IllegalArgumentException e) {
			return SMTPCommand.ERROR;
		}
	}

	private String getCommand(String input) {
		if (input.contains(" ")) {
			return input.split(" ")[0];
		} else {
			return input;
		}
	}

	private String getArgs(String input) {
		if (input.contains(" ")) {
			return input.substring(input.split(" ")[0].length() + 1);
		} else {
			return input;
		}
	}

	private void disconnet() {
		if (isConnected()) {
			try {
				if (getSocket() != null) {
					getSocket().close();
				}
				setConnected(false);
				getIn().close();
				writeData(SMTPCodes.DISCONNECT);
				getOut().flush();
				getOut().close();
			} catch (IOException ioe) {
				CoffeeMail.log(ioe.getMessage());
			}
		}
	}

	private void writeData(SMTPCodes code) {
		writeData(code.toString());
	}

	private void writeData(String message) {
		CoffeeMail.deepdebug(">> " + message);
		if (isConnected()) {
			getOut().print(message + "\r\n");
			getOut().flush();
		}
	}

	private String readData() {
		if (isConnected() && getSocket().isConnected()) {
			try {
				String input = getIn().readLine();
				if (input == null) {
					return null;
				} else {
					return input.trim();
				}
			} catch (IOException e) {
				disconnet();
			}
		} else {
			disconnet();
		}
		return null;
	}

	public enum SMTPCodes {
		WELCOME(220,
				"Welcome to CoffeeMail-MailServer - visit CoffeeMail-Project on "
						+ CoffeeMail.domain), OK(250, "OK"), STARTDATA(354,
				"Write the Maildata and end with <CRLF>.<CRLF>"), DISCONNECT(
				221, "Closing transmission"),
		// TODO ERROR(),
		// TODO SIZETOBIG(),
		STARTMAILDATA(354, "Start mail input; end with <CRLF>.<CRLF>");

		private int code;
		private String message;

		SMTPCodes(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public String getMessage() {
			return code + " " + message;
		}

		@Override
		public String toString() {
			return getMessage();
		}
	}

	public enum SMTPCommand {
		NULL, HELO, EHLO, DATA, MAIL, RCPT, ERROR, QUIT;
	}
}