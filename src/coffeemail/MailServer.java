package coffeemail;

import coffeemail.config.ConfigManager;
import coffeemail.console.Console;
import coffeemail.lang.Translator;
import coffeemail.mail.file.DefaultFileManager;
import coffeemail.mail.file.FileManager;
import coffeemail.module.ModuleManager;
import coffeemail.smtp.receiver.MailReceiveManager;
import coffeemail.smtp.sender.MailSendManager;

public final class MailServer {

	private static MailSendManager msm;
	private static MailReceiveManager mrm;
	private static ModuleManager mm;
	private static FileManager fm;
	private static ConfigManager cm;
	private static Translator translator;
	private static Console c;

	public static void start() {
		if (!CoffeeMail.newVersionavailable().isEmpty()) {
			System.out.println("##########################################");
			System.out.println("A new Version of " + CoffeeMail.name
					+ " is available!");
			System.out.println("Update with \"update\" or restart");
			System.out.println("with \"update\" as parameter!");
			System.out.println("##########################################");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		CoffeeMail.log("Loading Translations");
		translator = new Translator();
		fm = new DefaultFileManager();
		mm = new ModuleManager();
		c = new Console();
		cm = new ConfigManager();
		CoffeeMail.log("Start MailSendManager-Service");
		msm = new MailSendManager();

		CoffeeMail.log("Start MailReceiveManager-Service");
		mrm = new MailReceiveManager();
		mrm.start();

		CoffeeMail.log("Start ModulManager-Service");
		mm.loadModuls();
		CoffeeMail.log("Done!");
	}

	public static MailSendManager getMailSendManager() {
		return msm;
	}

	public static MailReceiveManager getMailReceiveManager() {
		return mrm;
	}

	public static ModuleManager getModuleManager() {
		return mm;
	}

	public static ConfigManager getConfigManager() {
		return cm;
	}

	public static FileManager getFileManager() {
		return fm;
	}

	public static Translator getTranslator() {
		return translator;
	}

	public static Console getConsole() {
		return c;
	}
}
