package coffeemail.console.defaults;

import coffeemail.CoffeeMail;
import coffeemail.console.Command;

public class CommandDebug extends Command {

	@Override
	public String getName() {
		return "debug";
	}

	@Override
	public String getInfo() {
		return "Sets Console-Mode to 'debug'";
	}

	@Override
	public void execute(String command, String... args) {
		CoffeeMail.setDebug(true);
	}

}
