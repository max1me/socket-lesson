package study.soket.server;

import study.socket.common.ConnectionService;
import study.socket.common.ConnectionThread;
import study.socket.common.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Server implements Runnable{
    private int port;
    private ServerHandler serverHandler = new ServerHandler();

    public Server(int port) {
        this.port = port;
    }
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен");
            int counter = 0;

            while (true) {
                Socket socket = serverSocket.accept();
                try {
                    ConnectionService connectionService = new ConnectionService(socket);
                    counter++;
                    serverHandler.getClients().put(connectionService, counter);
                    System.out.println("Подключился клиент №" + counter);
                    ConnectionThread connectionThread = new ConnectionThread(connectionService, serverHandler.getClients(), serverHandler.getFilesWithDescription());
                    connectionThread.start();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Ошибка подключения клиента");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Ошибка создания ServerSocket");
        }
    }

    /*private void addRequest(Request request) {
        if (requests.containsKey(request)) {
            requests.put(request, requests.get(request) + 1);
        } else {
            requests.put(request, 1);
        }
    }
    private enum Request {
        HELP("/help", "список доступных запросов и их описание"),
        PING("/ping", "время ответа сервера"),
        REQUESTS("/requests", "количество успешно обработанных запросов"),
        POPULAR("/popular", "название самого популярного запроса");
        String command;
        String description;
        Request(String command, String description) {
            this.command = command;
            this.description = description;
        }
    }*/
}
