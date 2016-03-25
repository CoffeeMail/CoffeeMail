package coffeemail.dns;

import java.util.Arrays;
import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class Lookup {

	public static String getBestMX(String domain) throws NamingException {
		if (domain.replace(".", "").length() < domain.length() - 1) {
			return domain;
		} else {
			Hashtable<String, String> env = new Hashtable<>();
			env.put("java.naming.factory.initial",
					"com.sun.jndi.dns.DnsContextFactory");
			DirContext ictx = new InitialDirContext(env);
			Attributes attrs = ictx
					.getAttributes(domain, new String[] { "MX" });
			Attribute attr = attrs.get("MX");
			String record = domain;
			int rank = Integer.MAX_VALUE;
			for (int i = 0; i < attr.size(); i++) {
				String[] parts = ((String) attr.get(i)).split(" ");
				int priority = Integer.parseInt(parts[0]);
				if (priority < rank) {
					rank = priority;
					record = parts[1].substring(0, parts[1].length() - 1);
				}
			}
			return record;
		}
	}

	public static MXRecord[] getMX(String domain) throws NamingException {
		if (domain.replace(".", "").length() < domain.length() - 1) {
			return new MXRecord[] { new MXRecord(domain, 10) };
		} else {
			Hashtable<String, String> env = new Hashtable<>();
			env.put("java.naming.factory.initial",
					"com.sun.jndi.dns.DnsContextFactory");
			DirContext ictx = new InitialDirContext(env);
			Attributes attrs = ictx
					.getAttributes(domain, new String[] { "MX" });
			Attribute attr = attrs.get("MX");
			MXRecord[] records = new MXRecord[attr.size()];
			for (int i = 0; i < attr.size(); i++) {
				String[] parts = ((String) attr.get(i)).split(" ");
				int priority = Integer.parseInt(parts[0]);
				records[i] = new MXRecord(parts[1].substring(0,
						parts[1].length() - 1), priority);
			}
			Arrays.sort(records);
			return records;
		}
	}

	public static String[] getNumericIP(String domain) throws NamingException {
		if (domain.replace(".", "").length() < domain.length() - 1) {
			return new String[] { domain };
		} else {
			Hashtable<String, String> env = new Hashtable<>();
			env.put("java.naming.factory.initial",
					"com.sun.jndi.dns.DnsContextFactory");
			DirContext ictx = new InitialDirContext(env);
			Attributes attrs = ictx.getAttributes(domain, new String[] { "A" });
			Attribute attr = attrs.get("A");
			String[] records = new String[attr.size()];
			for (int i = 0; i < attr.size(); i++) {
				records[i] = (String) attr.get(i);
			}
			return records;
		}
	}
}
