package coffeemail.module.event;

public class CancelableEvent extends Event {

	private boolean isCancelled = false;

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
}
