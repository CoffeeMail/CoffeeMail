package coffeemail.server;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import coffeemail.CoffeeMail;

public class ReceiveServer extends Thread {

	private boolean running = true;
	private ServerSocket serverSocket;
	private String name;
	private Class<? extends ReceiveClient> clienthandler;

	public ReceiveServer(String name, int port,
			Class<? extends ReceiveClient> clienthandler) {
		try {
			this.name = name;
			this.clienthandler = clienthandler;
			setSocket(new ServerSocket(port));
		} catch (IOException e) {
			CoffeeMail.error(e);
			CoffeeMail.shutdown(false);
		}

		try {
			serverSocket.setSoTimeout(10000);
		} catch (SocketException e) {
			CoffeeMail.log("Error starting " + name);
			CoffeeMail.error(e);
		}
	}

	@SuppressWarnings("unchecked")
	public ReceiveServer(String name, int port, String classname) {
		try {
			this.name = name;
			// Class<?> receiveclientclass = Class.forName(classname);
			// if (receiveclientclass != null) {
			// if (receiveclientclass.isAssignableFrom(ReceiveClient.class)) {
			// this.clienthandler = (Class<? extends ReceiveClient>)
			// receiveclientclass;
			// setSocket(new ServerSocket(port));
			// }
			// }

			this.clienthandler = (Class<? extends ReceiveClient>) Class
					.forName(classname);
			setSocket(new ServerSocket(port));
		} catch (IOException | ClassNotFoundException e) {
			CoffeeMail.error(e);
			CoffeeMail.shutdown(false);
		}

		try {
			serverSocket.setSoTimeout(10000);
		} catch (SocketException e) {
			CoffeeMail.log("Error starting " + name);
			CoffeeMail.error(e);
		}
	}

	public void setSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void kill() {
		this.interrupt();
		running = false;
		serverSocket = null;
	}

	public void run() {
		while (running) {
			try {
				Socket socket = serverSocket.accept();
				CoffeeMail.log(socket.getInetAddress().getHostAddress());
				// clienthandler.newInstance().createReceiveClient().setSocket(socket);;
				// for (Method m : clienthandler.getMethods()) {
				// System.out.println(m.getName());
				// }
				Constructor<? extends ReceiveClient> clientconstructor = clienthandler
						.getConstructor(Socket.class);
				clientconstructor.newInstance(socket);
			} catch (Exception e) {
				if (!(e instanceof SocketTimeoutException)) {
					e.printStackTrace();
					CoffeeMail.error(e);
				}
			}
		}
		CoffeeMail.log(name + " shut down");
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
