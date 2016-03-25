package coffeemail.smtp.receiver;

import coffeemail.server.ReceiveServer;

public class MailReceiveManager extends ReceiveServer {

	// private boolean running = true;

	// private ServerSocket serverSocket;

	public MailReceiveManager() {
		super("MailReceiveManager", 25, MailReceiveClient.class);
		// try {
		// setSocket(new ServerSocket(25));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		//
		// try {
		// serverSocket.setSoTimeout(10 * 1000);
		// } catch (SocketException se) {
		// CoffeeMail.log("Error starting MailReceiveManager");
		// }

	}
	//
	// public void setSocket(ServerSocket serverSocket) {
	// this.serverSocket = serverSocket;
	// }
	//
	// @Override
	// public void run() {
	// while (running) {
	// try {
	// Socket socket = serverSocket.accept();
	// CoffeeMail.log(socket.getInetAddress().getHostAddress());
	// System.out.println("DEBUG1");
	// new Thread(new MailReceiveClient(socket)).start();
	// System.out.println("DEBUG2");
	// } catch (Exception e) {
	// if (!(e instanceof SocketTimeoutException)) {
	// CoffeeMail.error(e);
	// }
	// }
	// }
	// CoffeeMail.log("MailReceiveManager shut down");
	// }
	//
	// public boolean isRunning() {
	// return running;
	// }
	//
	// public void setRunning(boolean running) {
	// this.running = running;
	// }
}
