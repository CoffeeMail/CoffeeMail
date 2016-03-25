package coffeemail.console.defaults;

import coffeemail.CoffeeMail;
import coffeemail.console.Command;

public class CommandDelete extends Command {

	@Override
	public String getName() {
		return "delete";
	}

	@Override
	public String getInfo() {
		return "Delete CoffeeMail";
	}

	@Override
	public void execute(String command, String... args) {
		CoffeeMail.delete();
		CoffeeMail.shutdown(true);
	}

}
