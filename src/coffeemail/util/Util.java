package coffeemail.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Pattern;

import coffeemail.MailServer;

public class Util {

	public static String generateRandomPassword() {
		SecureRandom sr = new SecureRandom();
		String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ234567890123456789&$%§!?=*_-#";

		String pw = "";
		for (int i = 0; i < 20; i++) {
			int index = (int) (sr.nextDouble() * letters.length());
			pw += letters.substring(index, index + 1);
		}
		return pw;
	}

	public static String getHash(String password) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new String(md.digest((password + MailServer.getConfigManager()
				.getConfig().getSalt()).getBytes()));
	}

	public static String fixUrl(String path) {
		if (path == null) {
			path = "/";
		}
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		if (path.endsWith("/") && path.length() > 2) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}

	public static Pattern urlToPattern(String path) {
		path = "^\\" + path.replace("/*/", "\\/((?!\\/).)*\\/");
		return Pattern.compile(path, Pattern.CASE_INSENSITIVE);
	}

	public static byte[] merchByteArrays(byte[] one, byte[] two) {
		byte[] combined = new byte[one.length + two.length];

		for (int i = 0; i < combined.length; ++i) {
			combined[i] = i < one.length ? one[i] : two[i - one.length];
		}
		return combined;
	}

	public static byte[] getFileStreamChecksum(InputStream inputStream)
			throws IOException {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] byteArray = new byte[1024];
		int bytesCount = 0;
		while ((bytesCount = inputStream.read(byteArray)) != -1) {
			md.update(byteArray, 0, bytesCount);
		}
		return md.digest();
	}

}
