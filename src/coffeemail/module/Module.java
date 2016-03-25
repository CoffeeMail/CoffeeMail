package coffeemail.module;

import java.util.ArrayList;
import java.util.List;

import coffeemail.CoffeeMail;
import coffeemail.MailServer;
import coffeemail.config.Config;
import coffeemail.module.event.EventHandler;
import coffeemail.module.event.Listener;

public class Module {

	private ExtendedModule extendedModule;

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

	public ExtendedModule getExtendedModul() {
		return extendedModule;
	}

	public void setExtendedModul(ExtendedModule extendedModule) {
		this.extendedModule = extendedModule;
	}

	public String getName() {
		return extendedModule.getName();
	}

	public String getAuthor() {
		return extendedModule.getAuthor();
	}

	public String getVersion() {
		return extendedModule.getVersion();
	}

	public String getDependency() {
		return extendedModule.getDependency();
	}

	public String translate(String key) {
		if (extendedModule.getLang().containsKey(
				Config.getInstance().getLanguage() + "." + key)) {
			return extendedModule.getLang().get(
					Config.getInstance().getLanguage() + "." + key);
		} else {
			return "String \"" + key + "\" not found!";
		}
	}

	public String translate(String key, String... args) {
		String trans = translate(key);
		List<String> with = new ArrayList<String>();
		for (String s : args) {
			with.add(s.trim());
		}
		if (with.size() == 0) {
			return trans;
		}
		String work = "-" + trans + "-";

		int count = work.length() - work.replace("%", "").length();

		if (with.size() < count) {
			for (int i = 0; i <= count - with.size(); i++) {
				with.add("null");
			}
		}

		String[] list = work.split("%");
		String result = "";
		for (int i = 0; i < count; i++) {

			result += list[i].substring(1) + with.get(i);
		}
		result += list[count].substring(1);
		result = result.substring(0, result.length() - 1);

		return result;
	}

	public String translateToLang(String key, String lang) {
		if (extendedModule.getLang().containsKey(lang + "." + key)) {
			return extendedModule.getLang().get(lang + "." + key);
		} else {
			return "String \"" + key + "\" not found!";
		}
	}

	public String translateToLang(String key, String lang, String... args) {
		String trans = translateToLang(key, lang);
		List<String> with = new ArrayList<String>();
		for (String s : args) {
			with.add(s.trim());
		}
		if (with.size() == 0) {
			return trans;
		}
		String work = "-" + trans + "-";

		int count = work.length() - work.replace("%", "").length();

		if (with.size() < count) {
			for (int i = 0; i <= count - with.size(); i++) {
				with.add("null");
			}
		}

		String[] list = work.split("%");
		String result = "";
		for (int i = 0; i < count; i++) {

			result += list[i].substring(1) + with.get(i);
		}
		result += list[count].substring(1);
		result = result.substring(0, result.length() - 1);

		return result;
	}
}
