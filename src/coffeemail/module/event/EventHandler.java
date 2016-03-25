package coffeemail.module.event;

import java.util.Collection;
import java.util.HashMap;

import coffeemail.CoffeeMail;
import coffeemail.module.Module;
import coffeemail.module.event.mail.MailReceiveEvent;
import coffeemail.module.event.mail.MailSendEvent;
import coffeemail.module.event.mail.PreMailReceiveEvent;
import coffeemail.module.event.mail.PreMailSendEvent;
import coffeemail.module.event.module.ModuleLoadEvent;
import coffeemail.module.event.module.ModulesLoadedEvent;

public class EventHandler {

	private HashMap<Module, Listener> listeners = new HashMap<Module, Listener>();

	public void addListener(Module module, Listener listener) {
		listeners.put(module, listener);
	}

	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}

	public void sendMailEvent(MailSendEvent e) {
		for (Listener listener : listeners.values()) {
			try {
				listener.sendMailEvent(e);
			} catch (Exception exception) {
				CoffeeMail.error(exception);
			}
		}
	}

	public void presendMailEvent(PreMailSendEvent e) {
		for (Listener listener : listeners.values()) {
			try {
				listener.presendMailEvent(e);
			} catch (Exception exception) {
				CoffeeMail.error(exception);
			}
		}
	}

	public void receiveMailEvent(MailReceiveEvent e) {
		for (Listener listener : listeners.values()) {
			try {
				listener.receiveMailEvent(e);
			} catch (Exception exception) {
				CoffeeMail.error(exception);
			}
		}
	}

	public void prereceiveMailEvent(PreMailReceiveEvent e) {
		for (Listener listener : listeners.values()) {
			try {
				listener.prereceiveMailEvent(e);
			} catch (Exception exception) {
				CoffeeMail.error(exception);
			}
		}
	}

	public void loadModulEvent(ModuleLoadEvent e) {
		for (Listener listener : listeners.values()) {
			try {
				listener.loadModulEvent(e);
			} catch (Exception exception) {
				CoffeeMail.error(exception);
			}
		}
	}

	public void loadedModulesEvent(ModulesLoadedEvent e) {
		for (Listener listener : listeners.values()) {
			try {
				listener.loadedModulesEvent(e);
			} catch (Exception exception) {
				CoffeeMail.error(exception);
			}
		}
	}

	public void removeAllListener() {
		listeners.clear();
	}

	public Collection<Listener> getListeners() {
		return listeners.values();
	}
}
