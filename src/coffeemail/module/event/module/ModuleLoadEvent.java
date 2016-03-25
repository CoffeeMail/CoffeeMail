package coffeemail.module.event.module;

import coffeemail.MailServer;
import coffeemail.module.ExtendedModul;
import coffeemail.module.event.CancelableEvent;

public class ModuleLoadEvent extends CancelableEvent {

	private ExtendedModul modul;

	public ModuleLoadEvent(ExtendedModul modul) {
		this.modul = modul;
	}

	public ExtendedModul getModul() {
		return modul;
	}

	public ModuleLoadEvent call() {
		MailServer.getModuleManager().getEventHandler().loadModulEvent(this);
		return this;
	}
}
