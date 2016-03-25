package coffeemail.module.event.module;

import coffeemail.MailServer;
import coffeemail.module.ExtendedModule;
import coffeemail.module.event.CancelableEvent;

public class ModuleLoadEvent extends CancelableEvent {

	private ExtendedModule modul;

	public ModuleLoadEvent(ExtendedModule modul) {
		this.modul = modul;
	}

	public ExtendedModule getModul() {
		return modul;
	}

	public ModuleLoadEvent call() {
		MailServer.getModuleManager().getEventHandler().loadModulEvent(this);
		return this;
	}
}
