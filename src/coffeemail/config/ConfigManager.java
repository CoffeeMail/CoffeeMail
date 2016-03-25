package coffeemail.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import coffeemail.CoffeeMail;

import com.google.gson.Gson;

public class ConfigManager {

	private Config config;

	public ConfigManager() {
		File configfile = new File(CoffeeMail.defaultfolder, "config");
		if (configfile.exists()) {
			config = load();
		} else {
			try {
				configfile.createNewFile();
				config = new Config();
				generate();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Config load() {
		File configfile = new File(CoffeeMail.defaultfolder, "config");
		try {
			String configjson = "";
			Scanner scan = new Scanner(configfile);
			while (scan.hasNext()) {
				configjson += scan.next();
			}
			scan.close();

			config = new Gson().fromJson(configjson, Config.class);
			return config;
		} catch (FileNotFoundException e) {
			System.out.println("##########################################");
			CoffeeMail.log("Error! Config File does not exist!");
			CoffeeMail.log("Type \"java -jar " + CoffeeMail.jarname
					+ " setup\" to run the Setup!");
			System.out.println("##########################################");
			System.exit(-1);
		}
		return null;
	}

	public void generate() {
		String configjson = new Gson().toJson(config);
		File configfile = new File(CoffeeMail.defaultfolder, "config");
		try {
			PrintWriter writer = new PrintWriter(configfile, "UTF-8");
			writer.write(configjson);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public Config getConfig() {
		return config;
	}

}
