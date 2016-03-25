package coffeemail.user;

public enum Rank {
	ADMIN(20), MOD(10), USER(1), ALL(0), CUSTOM(-1);

	private int level;

	Rank(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Rank getRankbyName(String name) {
		Rank r = Rank.valueOf(name.toUpperCase());
		if (r != null) {
			return r;
		}
		return this;
	}

	public boolean hasPermission(int level) {
		return this.level >= level;
	}

	public boolean hashigherPermission(int level) {
		return this.level > level;
	}
}
