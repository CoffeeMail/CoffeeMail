package coffeemail.console.defaults;

import coffeemail.config.Config;
import coffeemail.console.Command;
import coffeemail.exeptions.NoValidEmailAddressException;
import coffeemail.mail.Address;
import coffeemail.mail.Mail;

public class CommandEmail extends Command {

	@Override
	public String getName() {
		return "email";
	}

	@Override
	public String getInfo() {
		return "Send a Email to a Address";
	}

	@Override
	public void execute(String command, String... args) {
		if (args.length == 1) {
			try {
				if (Config.getInstance().getDefaultdomain() != null) {
					Mail mail = new Mail(new Address("coffeemail", Config
							.getInstance().getDefaultdomain(), "CoffeeMail on "
							+ Config.getInstance().getDefaultdomain()),
							new Address(args[0]), "An Email form CoffeeMail",
							"This is a Testmail");
					mail.send();
				} else {
					log("No defaultdoamin set!");
				}
			} catch (NoValidEmailAddressException e) {
				log("Email not found " + args[0]);
			}
		} else {
			log("email <email>");
		}
	}
}
