package coffeemail.console;

import coffeemail.CoffeeMail;

public abstract class Command {

	public abstract String getName();

	public abstract String getInfo();

	public abstract void execute(String command, String... args);

	public void log(String log) {
		CoffeeMail.log(log);
	}
}
