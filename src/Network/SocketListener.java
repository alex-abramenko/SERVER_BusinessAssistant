package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketListener {
    private static final Logger LOG = Logger.getLogger(SocketListener.class.getName());

    private int port;
    private ServerSocket serverSocket;

    /* Получаем порт, который нужно слушать. */
    public SocketListener(int port) throws IOException {
        this.port = port;
        initServerSocket();
    }

    /* Инициализация serverSocket'а. */
    private void initServerSocket() throws IOException {
        serverSocket = new ServerSocket(port);
    }

    /* Запуск прослушки порта. */
    public Socket startListenerPort() {
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) { LOG.log(Level.SEVERE, "IOException", e); }

        return clientSocket;
    }
}
