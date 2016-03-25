package coffeemail.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import coffeemail.CoffeeMail;

public abstract class ReceiveClient extends Thread {

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private boolean connected = false;

	public ReceiveClient() {
	}

	public ReceiveClient(Socket socket) {
		setSocket(socket);
	}

	@Override
	public void run() {
		handle(getSocket());
	}

	public abstract void handle(Socket socket);

	public void setSocket(Socket socket) {
		try {
			this.socket = socket;
			this.in = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "UTF-8"));
			this.out = new PrintWriter(socket.getOutputStream());

			this.connected = true;
			start();
		} catch (IOException e) {
			CoffeeMail.error(e);
		}
	}

	public BufferedReader getIn() {
		return in;
	}

	public PrintWriter getOut() {
		return out;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public Socket getSocket() {
		return socket;
	}
}
