package coffeemail.mail.content;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.DatatypeConverter;

public class FileConverter {

	public MailContent toMailContent(File file) {
		String base64 = DatatypeConverter
				.printBase64Binary(converteFileToByte(file));
		try {
			return new MailContent(base64, Files.probeContentType(Paths
					.get(file.getPath())));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		// Files.probeContentType(Paths.get(file.getPath()));
	}

	private byte[] converteFileToByte(File file) {
		int length = (int) file.length();
		byte[] bytes = new byte[length];
		try {
			BufferedInputStream reader = new BufferedInputStream(
					new FileInputStream(file));

			reader.read(bytes, 0, length);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}
}
