package coffeemail.mail;

public class MailAttribute {

	private String key;
	private String value;

	public MailAttribute(String key, String value) {
		this.key = key.replace(":", "").trim();
		this.value = value.trim();
	}

	public MailAttribute(String arrribute) {
		this.key = arrribute.substring(0, arrribute.indexOf(':')).trim();
		this.value = arrribute.substring(arrribute.indexOf(' ') + 1).trim();
	}

	public String getAttribute() {
		return getKey() + ": " + getValue();
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getAttribute();
	}
}
