# CoffeeMail
Mailserver written in Java

Depends on [GSON](https://github.com/google/gson) by Google


###Plugin Example

Here is a example for a plugin

```
package your.project;

import coffeemail.mail.Mail;
import coffeemail.module.Module;
import coffeemail.module.annotation.ModuleAuthor;
import coffeemail.module.annotation.ModuleMain;
import coffeemail.module.annotation.ModuleName;
import coffeemail.module.annotation.ModuleVersion;
import coffeemail.module.event.Listener;
import coffeemail.module.event.mail.MailReceiveEvent;
import coffeemail.module.event.mail.MailSendEvent;
import coffeemail.module.event.mail.PreMailReceiveEvent;
import coffeemail.module.event.mail.PreMailSendEvent;
import coffeemail.module.event.module.ModuleLoadEvent;
import coffeemail.module.event.module.ModulesLoadedEvent;

import java.util.Arrays;

@ModuleMain
@ModuleName(modulename = "AutoResponder")
@ModuleVersion(moduleversion = "1.0")
@ModuleAuthor(moduleauthor = "podpage")
public class Main extends Module {

	public void load() {
		System.out.println(translate("test"));
		System.out.println(translate("hello", "Username"));
		addListener(new Listener() {

			public void sendMailEvent(MailSendEvent e) {

			}

			public void receiveMailEvent(MailReceiveEvent e) {
				Mail mail = new Mail(
						e.getMail().getReceiver().setName("Auto-Reply"),
						e.getMail().getSender(),
						"Email erhalten!",
						Arrays.asList(new String[] { "Vielen Dank für Ihr Email" }));
				mail.send();
			}

			@Override
			public void presendMailEvent(PreMailSendEvent e) {

			}

			@Override
			public void prereceiveMailEvent(PreMailReceiveEvent e) {

			}

			@Override
			public void loadModulEvent(ModuleLoadEvent e) {

			}

			@Override
			public void loadedModulesEvent(ModulesLoadedEvent e) {

			}
		});
	}
}
```

Export it as .jar and rename it to .cmm


###License

CoffeeMail is released under the [Apache 2.0 license](LICENSE).

```
Copyright 2016 Lino Brenner (podpage).

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```