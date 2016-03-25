package coffeemail.module.event.module;

import coffeemail.MailServer;
import coffeemail.module.event.Event;

public class ModulesLoadedEvent extends Event {

	public ModulesLoadedEvent() {
	}

	public ModulesLoadedEvent call() {
		MailServer.getModuleManager().getEventHandler()
				.loadedModulesEvent(this);
		return this;
	}
}
