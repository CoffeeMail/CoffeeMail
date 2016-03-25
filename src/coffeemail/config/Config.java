package coffeemail.config;

import java.util.ArrayList;
import java.util.List;

import coffeemail.MailServer;
import coffeemail.util.Util;

public class Config {

	private int maxmailsize = 5; // in MB
	private int sendtimeout = 10; // in sec
	private int receivetimeout = 10; // in sec
	private String salt;
	private String defaultdomain;
	private String language = "en_US";
	private List<String> domains = new ArrayList<>();

	public Config() {
		this.salt = Util.generateRandomPassword();
	}

	public int getMaxmailsize() {
		return maxmailsize;
	}

	public void setMaxmailsize(int maxmailsize) {
		this.maxmailsize = maxmailsize;
	}

	public int getSendtimeout() {
		return sendtimeout;
	}

	public void setSendtimeout(int sendtimeout) {
		this.sendtimeout = sendtimeout;
	}

	public int getReceivetimeout() {
		return receivetimeout;
	}

	public void setReceivetimeout(int receivetimeout) {
		this.receivetimeout = receivetimeout;
	}

	public String getSalt() {
		return salt;
	}

	public String getDefaultdomain() {
		return defaultdomain;
	}

	public void setDefaultdomain(String defaultdomain) {
		this.defaultdomain = defaultdomain;
	}

	public List<String> getDomains() {
		return domains;
	}

	public void setDomains(List<String> domains) {
		this.domains = domains;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public static Config getInstance() {
		return MailServer.getConfigManager().getConfig();
	}
}
