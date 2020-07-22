import Network.ClientHandler;
import Network.SocketListener;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartServer {
    private static final Logger LOG = Logger.getLogger(StartServer.class.getName());

    public static void main(String[] args) {
        int PORT = 8888;
        SocketListener lisSock;

        try {
            lisSock = new SocketListener(PORT);
            while(true) {
                Socket clientSocket = lisSock.startListenerPort();
                System.out.print("CONNECTED: " + clientSocket + "\n");
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "IOException", e);
        }
    }
}
