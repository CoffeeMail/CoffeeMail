package coffeemail.module.event;

import coffeemail.module.event.mail.MailReceiveEvent;
import coffeemail.module.event.mail.MailSendEvent;
import coffeemail.module.event.mail.PreMailReceiveEvent;
import coffeemail.module.event.mail.PreMailSendEvent;
import coffeemail.module.event.module.ModuleLoadEvent;
import coffeemail.module.event.module.ModulesLoadedEvent;

public interface Listener {

	public void sendMailEvent(MailSendEvent e);

	public void presendMailEvent(PreMailSendEvent e);

	public void receiveMailEvent(MailReceiveEvent e);

	public void prereceiveMailEvent(PreMailReceiveEvent e);

	public void loadModulEvent(ModuleLoadEvent e);

	public void loadedModulesEvent(ModulesLoadedEvent e);

}
