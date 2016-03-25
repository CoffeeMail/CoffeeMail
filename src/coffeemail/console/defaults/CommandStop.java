package coffeemail.console.defaults;

import coffeemail.CoffeeMail;
import coffeemail.console.Command;

public class CommandStop extends Command {

	@Override
	public String getName() {
		return "stop";
	}

	@Override
	public String getInfo() {
		return "Stoping the server and all it's module";
	}

	@Override
	public void execute(String command, String... args) {

		if (args.length == 1 && args[0].equals("false")) {
			CoffeeMail.shutdown(false);
		} else {
			CoffeeMail.shutdown(true);
		}
	}
}
