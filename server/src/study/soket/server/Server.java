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
    private CopyOnWriteArraySet<ConnectionService> connectionServices;
    private ConcurrentHashMap<ConnectionService, Integer> clients = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> filesWithDescription;

    public Server(int port) {
        this.port = port;
        this.connectionServices = new CopyOnWriteArraySet<>();
        /*this.handler = new Handler(new DefaultGenerator(), this);
        generators.put("/help", new HelpGenerator());
        generators.put("/ping", new PingGenerator());
        generators.put("/popular", new PopularGenerator());
        generators.put("/default", new DefaultGenerator());*/
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
                    connectionServices.add(connectionService);
                    clients.put(connectionService, counter);
                    System.out.println("Подключился клиент №" + counter);
                    ConnectionThread connectionThread = new ConnectionThread(connectionService, /*connectionServices,*/ clients);
                    connectionThread.start();
                    /*String messageText = null;
                    Message messageFromClient = connectionService.readMessage();
                    /*handler.setGenerator(
                            generators.getOrDefault(messageFromClient.getText(),
                                    generators.get("/default")));
                    Message out = handler.execute();*/
                    //Message out = new Message("Сообщение принято");
                    //connectionService.writeMessage(out);
        /*            try {
                        for (ConnectionService connection : connectionServices) {
                            if (!connection.equals(connectionService)) {
                                System.out.println("1");
                                connection.writeMessage(new Message("От другого клиента " + messageFromClient.getText()));
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("sendMessage " + e.getMessage());
                    }

                     System.out.println(messageFromClient.getText());
                    *//*if (messageFromClient.getText().equals("/help")) {
                        handler.setGenerator(new HelpGenerator());
                    } else if (messageFromClient.getText().equals("/ping")) {
                        handler.setGenerator(new PingGenerator());
                    } else  {
                        handler.setGenerator(new DefaultGenerator());
                    }*/

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
