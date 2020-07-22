package Network;

import Data.MessageHandler;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    private static final Logger LOG = Logger.getLogger(ClientHandler.class.getName());

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    /* Получаем сокет клиента и инициализируем поток ввода/вывода */
    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "IOException", e);
        }

    }

    @Override
    public void run() {
        String messageFromClient;
        try {
            while(sockIsOpen()) {
                messageFromClient = reader.readLine();
                System.out.println(">>> Clients message: " + messageFromClient);
                sendMessage(new MessageHandler().startHandling(messageFromClient));
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "IOException", e);
        }

    }

    private boolean sockIsOpen() {
        if (socket != null && !socket.isClosed())
            return true;
        else
            return false;
    }

    /* Отправка сообщения клиенту */
    private void sendMessage(String message)  {

        try {
            System.out.println(">>> Send msg: " + message);
            writer.println(message);
            writer.flush();
            socket.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "IOException", e);
        }
    }


}
