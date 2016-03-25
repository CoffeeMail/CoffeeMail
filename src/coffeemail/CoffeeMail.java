package coffeemail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import coffeemail.config.ConfigManager;
import coffeemail.module.ExtendedModul;

public final class CoffeeMail {

	// TODO Multilang Support

	private final static String currentversion = "0.1.3";
	public final static String name = "CoffeeMail";
	public final static String domain = "http://podpage.org"; // later
																// coffee.mail
	public static String jarname;
	public static File defaultfolder;
	private static boolean debug = false;
	private static boolean deepdebug = false;
	private static ArrayList<String> log = new ArrayList<String>();
	private static ArrayList<String> debuglog = new ArrayList<String>();

	public static void main(String... args) {
		printLogo();
		startup();

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("update")) {
				update();
				return;
			} else if (args[0].equalsIgnoreCase("delete")) {
				delete();
				return;
			} else if (args[0].equalsIgnoreCase("setup")
					|| args[0].equalsIgnoreCase("install")) {
				setup();
				return;
			} else if (args[0].equalsIgnoreCase("debug")) {
				log("Turn on DeepDebugging!");
				debug = true;
				deepdebug = true;
				// TODO REMOVE ME!
				// debug = true;
				// new Thread(new WebHandler()).start();
				// setup();
			}
		}
		if (isInstalled()) {
			MailServer.start();
		} else {
			System.out.println("##########################################");
			log("It looks like " + name + " has not been installed yet");
			log("Type \"java -jar " + jarname + " setup\" to run the Setup!");
			System.out.println("##########################################");
		}
	}

	private static void startup() {
		try {
			File file = new File(CoffeeMail.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI().getPath());
			jarname = file.getName();
			defaultfolder = file.getParentFile();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private static boolean isInstalled() {
		try {
			File file = new File(CoffeeMail.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI().getPath());
			File moduledir = new File(file.getParent(), "module");
			if (!moduledir.exists()) {
				return false;
			}
			File config = new File(file.getParent(), "config");
			if (!config.exists()) {
				return false;
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static void setup() {
		try {
			File file = new File(CoffeeMail.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI().getPath());
			File moduledir = new File(file.getParent(), "module");
			if (!moduledir.exists()) {
				moduledir.mkdir();
			}

			File config = new File(file.getParent(), "config");
			if (config.exists()) {
				config.delete();
			}
			new ConfigManager();

			System.out.println("##########################################");
			log(name + " was successfully installed!");
			System.out.println("##########################################");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static void delete() {
		log("Are you sure that you want to permanently delete all " + name
				+ "-Data?");
		log("Type \"y\", \"yes\" or \"true\"");
		Scanner scan = new Scanner(System.in);
		String line = scan.nextLine();
		scan.close();
		if (line.equalsIgnoreCase("yes") || line.equalsIgnoreCase("y")
				|| line.equalsIgnoreCase("true")) {
			log("Started deleting files!");
			try {
				File file = new File(CoffeeMail.class.getProtectionDomain()
						.getCodeSource().getLocation().toURI().getPath());
				file.delete();

				File config = new File(file.getParent(), "config");
				if (config.exists()) {
					config.delete();
				}
				File moduledir = new File(file.getParent(), "module");
				int count = 0;
				count += deleteFolderandSub(moduledir);
				log("Deleted \"" + file.getName()
						+ "\", \"config\", \"modules\" and " + count
						+ " other files!");
				Thread.sleep(2000);
				log(":(");
				Thread.sleep(1000);
				log("- bye");
				Thread.sleep(1000);
				System.out.println("##############################");
				log("Erase all " + name + "-Data");
				System.out.println("##############################");
			} catch (URISyntaxException | InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			log("Process aborted! All your files are still there!");
		}
	}

	private static int deleteFolderandSub(File file) {
		int count = 0;
		if (file.exists() && file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.exists() && f.isDirectory()) {
					count += deleteFolderandSub(f);
				} else {
					f.delete();
				}
				count++;
			}
			file.delete();
		}
		return count;
	}

	public static int deleteModule(ExtendedModul em) {

		File module = new File(defaultfolder, "module");
		File folder = new File(module, em.getName());
		int count = 0;
		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles();
			for (File f : files) {
				if (f.exists() && f.isDirectory()) {
					count += deleteFolderandSub(f);
				} else {
					f.delete();
				}
				count++;
			}
			folder.delete();
		}
		return count;
	}

	public static boolean loadModule(String name) {
		// TODO
		return false;
	}

	public static String newVersionavailable() {
		try {
			URL versioncheck = new URL(domain + "/version.info");
			Scanner scan = new Scanner(versioncheck.openStream());
			String onlineversion = scan.nextLine();
			scan.close();

			int currentint = Integer.parseInt(currentversion.replace(".", ""));
			int onlineint = Integer.parseInt(onlineversion.replace(".", ""));

			if (onlineint > currentint) {
				return onlineversion;
			}
		} catch (Exception e) {
			if (e instanceof UnknownHostException) {
				log("Unable to check for a new version of " + name);
			} else {
				error(e);
			}
		}
		return "";
	}

	public static void update() {
		try {
			String version = newVersionavailable();
			if (!version.isEmpty()) {
				File to = new File(CoffeeMail.class.getProtectionDomain()
						.getCodeSource().getLocation().toURI().getPath());
				File tmp = new File(to.getParentFile() + "coffeemail.temp");
				if (!tmp.exists()) {
					tmp.createNewFile();
				}
				URL url = new URL(domain + "/download/" + version
						+ "/coffeemail.jar");
				InputStream is = url.openStream();
				OutputStream os = new FileOutputStream(tmp);
				byte[] buffer = new byte[4096];
				int fetched;
				while ((fetched = is.read(buffer)) != -1) {
					os.write(buffer, 0, fetched);
				}
				is.close();
				os.flush();
				os.close();
				if (to.exists()) {
					to.delete();
				}
				tmp.renameTo(to);
				log(name + " was successfully updatet to the newest Version!");
			} else {
				log(name + " is up-to-date! [Version:" + currentversion + "]");
			}
		} catch (Exception e) {
			log("An Error occurt while updating " + name
					+ "! Please Report this bug to @podpage on Twitter!");
		}
	}

	public static void restart() {
		try {
			File to = new File(CoffeeMail.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI().getPath());
			String javaBin = System.getProperty("java.home") + File.separator
					+ "bin" + File.separator + "java";

			Runtime.getRuntime().exec(javaBin + " -jar " + to.getPath());
			System.exit(0);
		} catch (Exception e) {
			log("An Error occurt while restarting " + name
					+ "! Please Report this bug to @podpage on Twitter!");
		}
	}

	public static void printLogo() {
		System.out.println("");
		System.out.println("   _____     ______        __  ___     _ __");
		System.out.println("  / ___/__  / _/ _/__ ___ /  |/  /__ _(_) /");
		System.out.println(" / /__/ _ \\/ _/ _/ -_) -_) /|_/ / _ `/ / / ");
		System.out.println(" \\___/\\___/_//_/ \\__/\\__/_/  /_/\\_,_/_/_/  ");
		System.out.println("");
		System.out.println("       cccccccccccccccccccccccccccccc      ");
		System.out.println("      cccccccccccccccccccccccccccccccc     ");
		System.out.println("  cccccccccccccccccccccccccccccccccccccccc ");
		System.out.println(" cccccccccccccccccccccccccccccccccccccccccc");
		System.out.println(" cccccccccccccccccccccccccccccccccccccccccc");
		System.out.println("  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");
		System.out.println("  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");
		System.out.println("   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  ");
		System.out.println("   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  ");
		System.out.println("   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  ");
		System.out.println("   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  ");
		System.out.println("   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  ");
		System.out.println("   cooooooooooooooooooooooooooooooooooooc  ");
		System.out.println("   occcccccccccccooooooooooccccccccccccco  ");
		System.out.println("    cccccccccccooooooooooooooccccccccccc   ");
		System.out.println("    ccccccccccooo@@@@@@@@@@Ooocccccccccc   ");
		System.out.println("    cccccccccoooC@@Co@@Co@@Ooocccccccccc   ");
		System.out.println("     ccccccccoooO@@oC@@oo@@Cooccccccccc    ");
		System.out.println("     cccccccccoo8@8oO@@oC@@oooccccccccc    ");
		System.out.println("     ccccccccccoooooooooooooocccccccccc    ");
		System.out.println("      cccccccccccooooooooocccccccccccc     ");
		System.out.println("      OOOO888888888888888888888888888O     ");
		System.out.println("      @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@     ");
		System.out.println("       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@      ");
		System.out.println("       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@      ");
		System.out.println("       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@      ");
		System.out.println("       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@      ");
		System.out.println("       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@      ");
		System.out.println("        @@@@@@@@@@@@@@@@@@@@@@@@@@@@       ");
		System.out.println("        @@@@@@@@@@@@@@@@@@@@@@@@@@@@       ");
		System.out.println("");
		System.out.println("");
	}

	public static void log(String s) {
		if (!debug) {
			System.out.println("\u00BB " + s);
		}
		if (log.size() >= 50) {
			log.remove(0);
		}
		log.add(s);
	}

	public static void debug(String s) {
		if (debug) {
			System.out.println("DEBUG \u00BB " + s);
		}
		if (debuglog.size() >= 50) {
			debuglog.remove(0);
		}
		debuglog.add(s);
	}

	public static void deepdebug(String s) {
		if (deepdebug) {
			System.out.println("DEEPDEBUG \u00BB " + s);
		}
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void error(Exception e) {
		log("An " + e.getClass().getSimpleName() + " occurt '" + e.getMessage()
				+ "'");
		log("Type 'debug' for more Info about this Exeption");

		StackTraceElement[] arrayOfStackTraceElement = e.getStackTrace();
		debug("=== " + e.getClass().getSimpleName() + " ===");
		for (StackTraceElement ste : arrayOfStackTraceElement) {
			debug("||" + ste);
		}
		debug("=========================");
	}

	public static void setDebug(boolean isdebug) {
		if (CoffeeMail.debug != isdebug) {
			CoffeeMail.debug = isdebug;
			if (isdebug) {
				System.out.println("=== DEBUG MODE ===");
				System.out.println("Exit with 'exit'");
				for (String s : debuglog) {
					System.out.println("DEBUG \u00BB " + s);
				}
			} else {
				System.out.println("=== CONSOLE MODE ===");
				for (String s : log) {
					System.out.println("\u00BB " + s);
				}
			}
		}
	}

	public static void shutdown(boolean forced) {
		log("Stopping " + name + "Server");
		MailServer.getModuleManager().getEventHandler().getListeners().clear();
		for (ExtendedModul m : MailServer.getModuleManager().getModuls()) {
			m.unload();
		}
		if (MailServer.getMailReceiveManager() != null) {
			MailServer.getMailReceiveManager().kill();
		}
		if (MailServer.getModuleManager() != null) {
			MailServer.getModuleManager().stopAll();
		}
		if (MailServer.getConsole() != null) {
			MailServer.getConsole().interrupt();
		}
		if (forced) {
			System.exit(0);
		}
	}
}
