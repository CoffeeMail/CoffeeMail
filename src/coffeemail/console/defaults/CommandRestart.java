package coffeemail.console.defaults;

import coffeemail.CoffeeMail;
import coffeemail.console.Command;

public class CommandRestart extends Command {

	@Override
	public String getName() {
		return "restart";
	}

	@Override
	public String getInfo() {
		return "Restart CoffeeMail";
	}

	@Override
	public void execute(String command, String... args) {
		CoffeeMail.restart();
	}

}
