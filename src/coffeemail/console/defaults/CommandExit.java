package coffeemail.console.defaults;

import coffeemail.CoffeeMail;
import coffeemail.console.Command;

public class CommandExit extends Command {

	@Override
	public String getName() {
		return "exit";
	}

	@Override
	public String getInfo() {
		return "Exits 'debug' Console-Mode";
	}

	@Override
	public void execute(String command, String... args) {
		CoffeeMail.setDebug(false);
	}

}
