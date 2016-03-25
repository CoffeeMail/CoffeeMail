package coffeemail.module;

import coffeemail.CoffeeMail;
import coffeemail.MailServer;
import coffeemail.module.event.EventHandler;
import coffeemail.module.event.Listener;

public class Module {

	public void load() {

	}

	public void unload() {

	}

	public ModuleManager getModuleManager() {
		return MailServer.getModuleManager();
	}

	public EventHandler getEventHandler() {
		return MailServer.getModuleManager().getEventHandler();
	}

	public void addListener(Listener listener) {
		MailServer.getModuleManager().getEventHandler()
				.addListener(this, listener);
	}

	public void log(String message) {
		CoffeeMail.log(message);
	}

	public void debug(String message) {
		CoffeeMail.debug(message);
	}

	public void error(Exception exception) {
		CoffeeMail.error(exception);
	}
	
	public String translate(String key) {
		return ""; //TODO
	}

	public String translate(String key, String... args) {
		return ""; //TODO
	}
	
	public String translateToLang(String key, String lang) {
		return ""; //TODO
	}

	public String translateToLang(String key, String lang, String... args) {
		return ""; //TODO
	}
}
