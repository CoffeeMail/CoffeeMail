package coffeemail.lang;

public class Translator {

	public static void main(String[] args) {
		new Translator();
	}

	public Translator() {
		System.out.println("TODO!"); //TODO
		String[] strings = "test=%s this is a test with a \\= in the text".split("(?<!\\\\)=");
		for (String s : strings) {
			System.out.println(s);
		}

	}

	public String translate(String key) {
		return "";
	}

	public String translate(String key, String... args) {
		return "";
	}
}
