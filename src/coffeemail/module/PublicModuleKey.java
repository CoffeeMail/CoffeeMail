package coffeemail.module;

import java.security.PublicKey;
import java.security.Signature;

import javax.xml.bind.DatatypeConverter;

import coffeemail.CoffeeMail;

public final class PublicModuleKey implements PublicKey {

	private static final long serialVersionUID = -405976053828988548L;
	private static PublicModuleKey publicModuleKey;
	protected byte[] encode;

	PublicModuleKey() {
		publicModuleKey = this;
		String publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDitiLmYxIq0Jg+t3y/0TBeEjYM2LsOW7EM6rUaupSgNT2ERbhKD/8qlCftYq15ESPn8BozWvMxvr4zQB9j42orDjYqxnaqub0L5c+e6Rp2rm/HOx1vzUQqEllKeh8GDWt9glnv9W39ox1Aps3/8wE+vIlTnYQ58Kcmf4eginRZYwIDAQAB";
		encode = DatatypeConverter.parseBase64Binary(publickey);
	}

	@Override
	public String getAlgorithm() {
		return "RSA";
	}

	@Override
	public byte[] getEncoded() {
		return encode;
	}

	@Override
	public String getFormat() {
		return "X.509";
	}

	public static PublicModuleKey getInstance() {
		if (publicModuleKey == null) {
			publicModuleKey = new PublicModuleKey();
		}
		return publicModuleKey;
	}

	public boolean verify(byte[] data, byte[] sign) {
		if (data.length > 0 && sign.length > 0) {
			try {
				Signature signer = Signature.getInstance("SHA1withRSA");
				signer.initVerify(this);
				signer.update(data);
				return signer.verify(sign);
			} catch (Exception e) {
				CoffeeMail.error(e);
				return false;
			}
		} else {
			return false;
		}
	}
}
