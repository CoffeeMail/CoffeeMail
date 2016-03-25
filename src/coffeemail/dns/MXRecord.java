package coffeemail.dns;

public class MXRecord implements Comparable<MXRecord> {

	private String domain;
	private short priority;
	private short port = 25;

	public MXRecord(String domain, int priority) {
		if (domain.replace(":", "").length() < domain.length()) {
			String[] parts = domain.split(":");
			this.domain = parts[0];
			this.port = Short.parseShort(parts[1]);
		} else {
			this.domain = domain;
		}
		this.priority = (short) priority;
	}

	public String getDomain() {
		return domain;
	}

	public short getPriority() {
		return priority;
	}

	public short getPort() {
		return hasPort() ? port : 25;
	}

	public boolean hasPort() {
		return port != 25;
	}

	@Override
	public String toString() {
		return priority + "-" + domain + ":" + getPort();
	}

	@Override
	public int compareTo(MXRecord mxr) {
		if (getPriority() > mxr.getPriority()) {
			return 10;
		} else if (getPriority() < mxr.getPriority()) {
			return -10;
		} else {
			return 0;
		}
	}
}
