package coffeemail.console;

import java.util.HashMap;
import java.util.Scanner;

import coffeemail.CoffeeMail;
import coffeemail.console.defaults.CommandDebug;
import coffeemail.console.defaults.CommandDelete;
import coffeemail.console.defaults.CommandEmail;
import coffeemail.console.defaults.CommandExit;
import coffeemail.console.defaults.CommandModule;
import coffeemail.console.defaults.CommandStop;
import coffeemail.console.defaults.CommandUpdate;

public class Console extends Thread {

	private HashMap<String, Command> commandmap = new HashMap<>();
	private boolean running = true;

	public Console() {
		addDefaultCommands();
		start();
	}

	@Override
	public void run() {
		Scanner scan = new Scanner(System.in);
		while (scan.hasNext() && running) {
			String line = scan.nextLine().trim();
			String command = "";
			String[] args = line.split(" ");
			if (args.length > 1) {
				command = args[0].toLowerCase();
				args = line.substring(args[0].length() + 1, line.length())
						.split(" ");
			} else {
				command = line.toLowerCase();
				args = new String[0];
			}

			if (commandmap.containsKey(command)) {
				commandmap.get(command).execute(command, args);
			} else {
				CoffeeMail.log("There is no Command like '" + command + "'");
			}
		}
		scan.close();
	}

	private void addDefaultCommands() {
		addCommand(new CommandStop());
		addCommand(new CommandUpdate());
		addCommand(new CommandDebug());
		addCommand(new CommandExit());
		addCommand(new CommandModule());
		addCommand(new CommandDelete());
		addCommand(new CommandEmail());
		// addCommand(new CommandRestart());
	}

	public void addCommand(Command command) {
		commandmap.put(command.getName(), command);
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public HashMap<String, Command> getCommandMap() {
		return commandmap;
	}

}
