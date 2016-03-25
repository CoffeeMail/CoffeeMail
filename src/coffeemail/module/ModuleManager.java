package coffeemail.module;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import coffeemail.CoffeeMail;
import coffeemail.MailServer;
import coffeemail.module.annotation.ModuleMain;
import coffeemail.module.event.EventHandler;
import coffeemail.module.event.module.ModulesLoadedEvent;
import coffeemail.util.Util;

public class ModuleManager {

	private ArrayList<ExtendedModule> modules = new ArrayList<ExtendedModule>();
	private EventHandler handler = new EventHandler();

	public void loadModuls() {
		try {
			File file = new File(new File(MailServer.class
					.getProtectionDomain().getCodeSource().getLocation()
					.toURI().getPath()).getParentFile(), "module");
			for (File f : file.listFiles()) {
				if (f.isFile() && f.getName().endsWith(".cmm")) {

					CoffeeMail.debug("Found "
							+ f.getName()
									.substring(0, f.getName().length() - 4)
							+ "-Module");
					loadModul(f, true);
				} else if (f.isDirectory()) {
					for (File cmm : f.listFiles()) {
						if (cmm.isFile() && cmm.getName().endsWith(".cmm")) {

							CoffeeMail.debug("Found "
									+ f.getName().substring(0,
											cmm.getName().length() - 4)
									+ "-Module");
							loadModul(cmm, false);
						}
					}
				}
			}
			sortAndLoad();
		} catch (URISyntaxException e) {
			// e.printStackTrace();
		}
	}

	// Taken from stackoverflow:
	// http://stackoverflow.com/questions/26949333/load-a-class-in-a-jar-just-with-his-name

	public void loadModul(File file, boolean firstinstall) {
		try {
			boolean loaded = false;
			JarFile jarFile = new JarFile(file);
			Enumeration<JarEntry> e = jarFile.entries();

			URL[] urls = { new URL("jar:file:" + file.getPath() + "!/") };
			URLClassLoader cl = URLClassLoader.newInstance(urls);

			Class<?> mainclass = null;
			int classcount = 0;
			byte[] arraykey = new byte[0];
			String signkey = "";
			HashMap<String, String> lang = new HashMap<String, String>();

			while (e.hasMoreElements()) {
				JarEntry je = (JarEntry) e.nextElement();

				if (!je.isDirectory() && je.getName().endsWith(".class")) {

					classcount++;

					InputStream inputStream = jarFile.getInputStream(je);

					arraykey = Util.merchByteArrays(arraykey,
							Util.getFileStreamChecksum(inputStream));

					inputStream.close();

					String className = je.getName().substring(0,
							je.getName().length() - 6);
					className = className.replace('/', '.');
					Class<?> c = cl.loadClass(className);

					try {
						if (c.isAnnotationPresent(ModuleMain.class)) {

							Method m = c.getMethod("load");
							if (m != null) {
								mainclass = c;
							}
						}
					} catch (NoSuchMethodException ex) {
					}
				} else if (je.getName().endsWith("key.cmk")) {
					InputStream inputStream = jarFile.getInputStream(je);
					Scanner scan = new Scanner(inputStream);
					while (scan.hasNext()) {
						signkey += scan.nextLine();
					}
					scan.close();
					inputStream.close();
				} else if (je.getName().endsWith(".lang")) {
					InputStream inputStream = jarFile.getInputStream(je);
					Scanner scan = new Scanner(inputStream);
					int index = 0;
					if (je.getName().contains("/")) {
						index = je.getName().lastIndexOf('/') + 1;
					}
					String langname = je.getName().substring(index,
							je.getName().length() - 5);
					while (scan.hasNext()) {
						String[] langstrings = scan.nextLine().split(
								"(?<!\\\\)=");
						lang.put(langname + "." + langstrings[0],
								langstrings[1]);
					}
					scan.close();
					inputStream.close();
				}
			}
			jarFile.close();
			cl.close();
			if (mainclass != null) {
				Module module = (Module) mainclass.newInstance();
				arraykey = Util.merchByteArrays(arraykey, new String(classcount
						+ "").getBytes());
				ExtendedModule em = new ExtendedModule(module, arraykey,
						signkey);
				em.setLang(lang);
				module.setExtendedModul(em);
				modules.add(em);
				if (firstinstall) {
					File folder = new File(file.getParent(), em.getName());
					if (!folder.exists()) {
						folder.mkdir();
					}

					file.renameTo(new File(file.getParent(), em.getName() + "/"
							+ em.getName() + ".cmm"));
				}
				loaded = true;
			}
			if (!loaded) {
				CoffeeMail.log("No Main-Class found! [" + file.getName() + "]");
			}
		} catch (Exception e) {
			CoffeeMail.error(e);
		}
	}

	public void sortAndLoad() {

		// ArrayList<ExtendedModul> eml = new ArrayList<ExtendedModul>();
		// eml.addAll(modules);

		for (ExtendedModule em : modules) {
			boolean canboot = false;
			if (em.getDependency().trim().equalsIgnoreCase("null")) {
				canboot = true;
			} else if (em.getDependency().trim().equalsIgnoreCase("CoffeeMail")
					|| em.getDependency().trim().equalsIgnoreCase("init")) {
				canboot = true;
			} else {
				for (ExtendedModule emm : modules) {
					if (em.getDependency().trim()
							.equalsIgnoreCase(emm.getName())) {
						canboot = true;
					}
				}
			}
			if (!canboot) {
				modules.remove(em);
				CoffeeMail.log("Unable to load " + em.getName()
						+ "-Module the dependency \"" + em.getDependency()
						+ "\" does not exist!");
			}
			// if (em.getDependency().equalsIgnoreCase("CoffeeMail")) {
			// if (em.isVerified()) {
			// em.init();
			// }
			// eml.add(em);
			// }
			em.init();
		}
		new ModulesLoadedEvent().call();
	}

	public ArrayList<ExtendedModule> getModuls() {
		return modules;
	}

	public EventHandler getEventHandler() {
		return handler;
	}

	public void stopAll() {
		for (ExtendedModule extendedModule : modules) {
			extendedModule.stop();
		}
	}
}
