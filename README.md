#CoffeeMail

Coffeemail is a Java-Based Mailserver which can be expanded with **Plugins** like a Webserver or Autoresponder. It's easy to understand, open-source and free!

----------

Dependencies
-------------
[GSON](https://github.com/google/gson) by Google
[Java](https://www.java.com/en/) by Oracle

----------
Plugins
-------------

You can create Plugins for CoffeeMail
Here is a example for a plugin

**Import CoffeeMail**
> 
>import coffeemail.mail.Mail;
import coffeemail.module.*;

**Define Module-Data**
>@ModuleMain
@ModuleName(modulename = "AutoResponder")	
@ModuleVersion(moduleversion = "1.0")
@ModuleAuthor(moduleauthor = "podpage")

**Extend Module**	
>public class Main extends Module {

**Create Config by adding *public static* Variables in Mainclass**
>public static String response = "Received your email!";

**load-Method needs to exist**
>public void load() {

**Register a listener for events**

>addListener(new Listener() {
	public void sendMailEvent(MailSendEvent e) {}
	public void receiveMailEvent(MailReceiveEvent e) {
		Mail mail = new Mail(e.getMail().getReceiver().setName("Auto-Reply"), 
		e.getMail().getSender(),
		"Email received!",
		response);
		mail.send();
	}
	public void presendMailEvent(PreMailSendEvent e) {}
	public void prereceiveMailEvent(PreMailReceiveEvent e) {}
	public void loadModulEvent(ModuleLoadEvent e) {}
	public void loadedModulesEvent(ModulesLoadedEvent e) {}
		});

**Close **

>}
}

**Export it as .jar and rename it to .cmm**
**Drop it in the module-Folder**

**Want a verifyed Plugin? - Text me [@podpage](https://twitter.com/podpage)**

License
-------------

CoffeeMail is released under the [Apache 2.0 license](LICENSE).

>Copyright 2016 Lino Brenner (podpage).

>Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

>    http://www.apache.org/licenses/LICENSE-2.0

>Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.