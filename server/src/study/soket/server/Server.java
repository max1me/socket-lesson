package study.soket.server;

import study.socket.common.ConnectionService;
import study.socket.common.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Server implements Runnable{
    private int port;
    private Map<Request, Integer> requests = new HashMap<>();
    private Handler handler;
    private Map<String, MessageGenerator> generators;

    public Server(int port) {
        this.port = port;
        this.handler = new Handler(new DefaultGenerator(), this);
        /*generators.put("/help", new HelpGenerator());
        generators.put("/ping", new PingGenerator());
        generators.put("/popular", new PopularGenerator());
        generators.put("/default", new DefaultGenerator());*/
    }
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен");
            while (true) {
                Socket socket = serverSocket.accept();
                try (ConnectionService connectionService = new ConnectionService(socket)) {
                    String messageText = null;
                    Message messageFromClient = connectionService.readMessage();
                    handler.setGenerator(
                            generators.getOrDefault(messageFromClient.getText(),
                                    generators.get("/default")));
                    Message out = handler.execute();
                    System.out.println(messageFromClient.getText());
                    /*if (messageFromClient.getText().equals("/help")) {
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
    /*@Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен");
            while (true) {
                Socket socket = serverSocket.accept();
                try (ConnectionService connectionService = new ConnectionService(socket)) {
                    String messageText = null;
                    Message messageFromClient = connectionService.readMessage();
                    System.out.println("Сообщение от клиента: " + messageFromClient.getText());
                    connectionService.writeMessage(new Message("Сообщение принято"));
                    if (messageFromClient.getText().equals(Request.HELP.command)) {
                        addRequest(Request.HELP);
                        for (Request request: Request.values()) {
                            messageText = messageText + request.command + " - " + request.description + "\n";
                        }
                    } else if (messageFromClient.getText().equals(Request.PING.command)) {
                        addRequest(Request.PING);
                        long diff = ZonedDateTime.now().toInstant().toEpochMilli()
                                - messageFromClient.getSentAt().toInstant().toEpochMilli();
                        messageText = "Время ответа сервера " + diff + " мс";
                    } else if (messageFromClient.getText().equals(Request.REQUESTS.command)) {
                        int count = 0;
                        for (Map.Entry<Request, Integer> req: requests.entrySet()) {
                            count += req.getValue();
                        }
                        messageText = "Количество успешно обработанных запросов: " + count;
                        addRequest(Request.REQUESTS);
                    } else if (messageFromClient.getText().equals(Request.POPULAR.command)) {
                        addRequest(Request.POPULAR);
                        Optional<Map.Entry<Request, Integer>> popularRequest = requests.entrySet()
                                .stream()
                                .max(Comparator.comparing(Map.Entry::getValue));
                        messageText = "Название самого популярного запроса: " + popularRequest.get().getKey().command;
                    } else if (messageFromClient.getText().startsWith("/")) {
                        messageText = "Введите /help для получения списка доступных команд";
                    }
                    if (messageText != null) connectionService.writeMessage(new Message(messageText));
                    //connectionService.close();
                } catch (IOException e) {
                    Systemystem.out.println(e.getMessage());
                    System.out.println("Ошибка подключения клиента");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Ошибка создания ServerSocket");
        }
    }*/
    private void addRequest(Request request) {
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
    }
}
