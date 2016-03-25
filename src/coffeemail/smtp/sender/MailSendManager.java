package coffeemail.smtp.sender;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import coffeemail.CoffeeMail;
import coffeemail.mail.Address;
import coffeemail.mail.Mail;

public class MailSendManager {

	private ExecutorService executorService;

	public MailSendManager() {
		executorService = Executors.newFixedThreadPool(2);
	}

	public boolean send(Mail mail) {
		try {
			executorService
					.execute(new MailSendClient(mail, mail.getReceiver()));
		} catch (RuntimeException e) {
			CoffeeMail.error(e);
			return false;
		}
		for (Address address : mail.getCCReceiver()) {
			try {
				executorService.execute(new MailSendClient(mail, address));
			} catch (RuntimeException e) {
				CoffeeMail.error(e);
				return false;
			}
		}
		for (Address address : mail.getBCCReceiver()) {
			try {
				executorService.execute(new MailSendClient(mail, address));
			} catch (RuntimeException e) {
				CoffeeMail.error(e);
				return false;
			}
		}
		return true;
	}
}
