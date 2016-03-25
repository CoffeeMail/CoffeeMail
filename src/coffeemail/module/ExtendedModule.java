
package coffeemail.module;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import coffeemail.CoffeeMail;
import coffeemail.module.annotation.ModuleAuthor;
import coffeemail.module.annotation.ModuleConfig;
import coffeemail.module.annotation.ModuleDependency;
import coffeemail.module.annotation.ModuleName;
import coffeemail.module.annotation.ModuleVersion;
import coffeemail.module.event.module.ModuleLoadEvent;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ExtendedModule {

	private Module module;
	private HashMap<String, String> lang;
	private String name;
	private String author;
	private String version;
	private String dependency;
	protected byte[] verifyed;
	protected byte[] sign;
	private boolean start = false;

	public ExtendedModule(Module module, byte[] verifyed, String sign) {
		this.verifyed = verifyed;
		this.sign = DatatypeConverter.parseBase64Binary(sign);
		this.module = module;

		try {
			Method m = module.getClass().getMethod("load");
			Class<? extends Module> c = module.getClass();

			if (m != null) {
				String modulname = "Unknown";
				String modulauthor = "Unknown";
				String moduleversion = "1.0";
				String moduledependency = "NULL";
				if (c.isAnnotationPresent(ModuleName.class)) {
					Annotation annotation = c.getAnnotation(ModuleName.class);
					ModuleName mn = (ModuleName) annotation;
					modulname = mn.modulename();
				}
				if (c.isAnnotationPresent(ModuleAuthor.class)) {
					Annotation annotation = c.getAnnotation(ModuleAuthor.class);
					ModuleAuthor mn = (ModuleAuthor) annotation;
					modulauthor = mn.moduleauthor();
				}
				if (c.isAnnotationPresent(ModuleVersion.class)) {
					Annotation annotation = c
							.getAnnotation(ModuleVersion.class);
					ModuleVersion mn = (ModuleVersion) annotation;
					moduleversion = mn.moduleversion();
				}
				if (c.isAnnotationPresent(ModuleDependency.class)) {
					Annotation annotation = c
							.getAnnotation(ModuleDependency.class);
					ModuleDependency mn = (ModuleDependency) annotation;
					moduledependency = mn.moduledependency();
				}

				this.name = modulname;
				this.author = modulauthor;
				this.version = moduleversion;
				this.dependency = moduledependency;
				this.start = true;
			}
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	public void init() {
		if (this.start) {

			ModuleLoadEvent mle = new ModuleLoadEvent(this);
			mle.call();
			if (!mle.isCancelled()) {
				if (isVerified()) {
					CoffeeMail.log("Loading " + name + "[" + version + "] by "
							+ author + " (verified)");
				} else {
					CoffeeMail.log("Loading " + name + "[" + version + "] by "
							+ author);
				}
				config();
				load();
			} else {
				CoffeeMail.log("Unable to load " + name + "[" + version + "]");
			}
		} else {
			CoffeeMail.log("Unable to load " + name + "[" + version + "]");
		}
	}

	public void config() {
		File moduledir = new File(CoffeeMail.defaultfolder, "module/"
				+ getName());
		if (!moduledir.exists()) {
			moduledir.mkdir();
		}
		File config = new File(CoffeeMail.defaultfolder, "module/" + getName()
				+ "/config");
		if (!config.exists()) {
			try {
				config.createNewFile();
				saveConfig(config);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			loadConfig(config);
		}
	}

	public void saveConfig(File config) {
		Field[] fields = getModul().getClass().getFields();
		String json = "{";
		for (Field field : fields) {
			if (field.isAnnotationPresent(ModuleConfig.class)) {
				try {
					json += "\"" + field.getName() + "\":"
							+ new Gson().toJson(field.get(getModul())) + ",";
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}
		json = json.substring(0, json.length() - 1) + "}";
		if (fields.length == 0) {
			json = "{}";
		}
		try {
			PrintWriter writer = new PrintWriter(config, "UTF-8");
			writer.write(json);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void loadConfig(File config) {
		String json = "";
		try {
			Scanner scan = new Scanner(config);
			while (scan.hasNext()) {
				json += scan.next();
			}
			scan.close();
		} catch (FileNotFoundException e) {
		}
		JsonObject jobj = new Gson().fromJson(json, JsonObject.class);
		Set<Entry<String, JsonElement>> set = jobj.entrySet();

		Iterator<Entry<String, JsonElement>> i = set.iterator();

		while (i.hasNext()) {
			Entry<String, JsonElement> ex = i.next();
			try {
				Field f = getModul().getClass().getField(ex.getKey());
				if (f != null) {
					Class<?> typeclass = f.getType();
					if (typeclass.equals(String.class)) {
						f.set(getModul(), ex.getValue().getAsString());
					} else if (typeclass.equals(Integer.class)
							|| typeclass.equals(int.class)) {
						f.setInt(getModul(), ex.getValue().getAsInt());
					} else if (typeclass.equals(Character.class)
							|| typeclass.equals(char.class)) {
						f.setChar(getModul(), ex.getValue().getAsCharacter());
					} else if (typeclass.equals(Boolean.class)
							|| typeclass.equals(boolean.class)) {
						f.setBoolean(getModul(), ex.getValue().getAsBoolean());
					} else if (typeclass.equals(Double.class)
							|| typeclass.equals(double.class)) {
						f.setDouble(getModul(), ex.getValue().getAsDouble());
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException
					| NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}

	public void load() {
		getModul().load();
	}

	public void unload() {
		getModul().unload();
	}

	public Module getModul() {
		return module;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public String getVersion() {
		return version;
	}

	public String getDependency() {
		return dependency;
	}

	public void stop() {
		module.unload();
	}

	public HashMap<String, String> getLang() {
		return lang;
	}

	public void setLang(HashMap<String, String> lang) {
		this.lang = lang;
	}

	public boolean isVerified() {
		return PublicModuleKey.getInstance().verify(this.verifyed, this.sign);
	}
}
