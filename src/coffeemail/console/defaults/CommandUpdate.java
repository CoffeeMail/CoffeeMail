package coffeemail.console.defaults;

import coffeemail.CoffeeMail;
import coffeemail.console.Command;

public class CommandUpdate extends Command {

	@Override
	public String getName() {
		return "update";
	}

	@Override
	public String getInfo() {
		return "Searching for a newer Verison of CoffeeMail and updated it";
	}

	@Override
	public void execute(String command, String... args) {
		CoffeeMail.update();
		CoffeeMail.shutdown(false);
	}

}
