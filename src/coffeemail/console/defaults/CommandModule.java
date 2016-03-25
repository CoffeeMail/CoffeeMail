package coffeemail.console.defaults;

import coffeemail.CoffeeMail;
import coffeemail.MailServer;
import coffeemail.console.Command;
import coffeemail.module.ExtendedModul;

public class CommandModule extends Command {

	@Override
	public String getName() {
		return "module";
	}

	@Override
	public String getInfo() {
		return "Module-Manage Command [list, download, delete]";
	}

	@Override
	public void execute(String command, String... args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("list")) {
				int count = MailServer.getModuleManager().getModuls().size();
				log("=== Loaded Modules [" + count + "] ===");
				log("There are " + count + " loaded Modules");
				for (ExtendedModul m : MailServer.getModuleManager()
						.getModuls()) {
					if (m.isVerified()) {
						log(m.getName() + "[" + m.getVersion() + "] by "
								+ m.getAuthor() + " (verified)");
					} else {
						log(m.getName() + "[" + m.getVersion() + "] by "
								+ m.getAuthor());
					}
				}
				log("==========================");
			} else if (args[0].equalsIgnoreCase("download")
					|| args[0].equalsIgnoreCase("load")) {
				log("module " + args[0] + " <name/id>");
			} else if (args[0].equalsIgnoreCase("delete")
					|| args[0].equalsIgnoreCase("remove")) {
				log("module " + args[0] + " <name>");
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("download")
					|| args[0].equalsIgnoreCase("load")) {
				log("module " + args[0] + " <name/id>");
			} else if (args[0].equalsIgnoreCase("delete")
					|| args[0].equalsIgnoreCase("remove")) {
				ExtendedModul module = null;
				for (ExtendedModul em : MailServer.getModuleManager()
						.getModuls()) {
					if (em.getName().equalsIgnoreCase(args[1])) {
						module = em;
						break;
					}
				}
				if (module == null) {
					log("No Module '" + args[1] + "' found!");
				} else {
					int count = CoffeeMail.deleteModule(module);
					log("Deleted '" + count + "' files!");
				}
			}
		} else {
			log("module list");
			log("module download <name/id>");
			log("module remove <name>");
		}
	}
}
