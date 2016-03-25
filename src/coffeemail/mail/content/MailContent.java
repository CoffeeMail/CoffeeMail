package coffeemail.mail.content;

public class MailContent {

	public ContentType type;
	public String content;

	// public abstract String[] write();

	public MailContent(String base64, ContentType type) {
		this.content = base64;
		this.type = type;
	}

	public MailContent(String base64, String type) {
		this.content = base64;
		this.type = ContentType.NULL.setType(type);
	}

	public enum ContentType {
		HTML("text/html"), TXT("text/plain"), PNG("image/png"), JPEG(
				"image/jpg"), NULL("");

		private String type;

		ContentType(String type) {
			this.type = type;
		}

		public ContentType setType(String type) {
			this.type = type;
			return this;
		}

		public String typeToString() {
			return type;
		}
	}
}
