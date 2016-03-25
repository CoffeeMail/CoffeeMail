package coffeemail.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import coffeemail.config.Config;

public class Translator {

	public HashMap<String, String> lang;

	public Translator() {
		lang = new HashMap<String, String>();
		load("de_DE");
		load("en_US");
		System.out.println(translate("install.success", "CoffeeMail"));
	}

	public void load(String lang) {
		Scanner scan = new Scanner(this.getClass().getResourceAsStream(
				"/coffeemail/lang/" + lang + ".lang"), "utf-8");
		while (scan.hasNext()) {
			String[] langstrings = scan.nextLine().split("(?<!\\\\)=");
			this.lang.put(lang + "." + langstrings[0], langstrings[1]);
		}
		scan.close();
	}

	public String translate(String key) {
		if (this.lang.containsKey(Config.getInstance().getLanguage() + "."
				+ key)) {
			return this.lang
					.get(Config.getInstance().getLanguage() + "." + key);
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
		if (this.lang.containsKey(lang + "." + key)) {
			return this.lang.get(lang + "." + key);
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
