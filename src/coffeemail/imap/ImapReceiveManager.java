package coffeemail.imap;

import java.net.Socket;

import coffeemail.server.ReceiveClient;
import coffeemail.server.ReceiveServer;

public class ImapReceiveManager extends ReceiveServer {

	public ImapReceiveManager() {
		super("ImapReceiveManager", 143, ImapReceiveClient.class);
	}

	class ImapReceiveClient extends ReceiveClient {

		public ImapReceiveClient(Socket socket) {
			super(socket);
		}

		@Override
		public void handle(Socket socket) {

		}

	}

}
