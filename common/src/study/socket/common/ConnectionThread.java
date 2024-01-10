package study.socket.common;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionThread extends Thread{
    private ConcurrentHashMap<ConnectionService, Integer> clients;
    private ConnectionService connectionService;
    private final String fileDirectory = "./common/loadedFiles";
    private ConcurrentHashMap<String, String> filesWithDescription;

    public ConnectionThread(ConnectionService connectionService,
                            ConcurrentHashMap<ConnectionService, Integer> clients,
                            ConcurrentHashMap<String, String> filesWithDescription) {
        this.connectionService = Objects.requireNonNull(connectionService);
        this.clients = Objects.requireNonNull(clients);
        this.filesWithDescription = Objects.requireNonNull(filesWithDescription);
    }

    public ConnectionThread() {
        this.clients = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<ConnectionService, Integer> getClients() {
        return clients;
    }

    public void setClients(ConcurrentHashMap<ConnectionService, Integer> clients) {
        this.clients = clients;
    }

    public ConnectionService getConnectionService() {
        return connectionService;
    }

    public void setConnectionService(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    public ConcurrentHashMap<String, String> getFilesWithDescription() {
        return filesWithDescription;
    }

    @Override
    public void run() {
        while (true) {
            Message message = null;
            try {
                message = connectionService.readMessage();
                System.out.println("Server: received message " + message.getText());
                String text = null;
                if (message.getText().equals("/load")) {
                    text = "От Client_" + clients.get(connectionService) + " получен файл " +
                            message.getTextFile().getFileName() + "\n";
                    text = text + "Описание файла: " + message.getTextFile().getDescription();
                    try(PrintWriter printWriter = new PrintWriter(new FileOutputStream(fileDirectory + "/" + message.getTextFile().getFileName(), false))) {
                        printWriter.print(message.getTextFile().getText());
                        filesWithDescription.put(message.getTextFile().getFileName(), message.getTextFile().getDescription());
                        connectionService.writeMessage(new Message("Файл сохранен на сервер"));
                        //System.out.println("Файл сохранен на сервер");
                    } catch (FileNotFoundException e) {
                        System.out.println("Ошибка сохранения файла на сервер");
                        e.printStackTrace();
                    }
                } else if (message.getText().equals("/files")){
                    text = "Список файлов: ";
                    File[] files = new File(fileDirectory).listFiles();
                    for (File file : files) {
                        text += file.getName() + "\n";
                    }
                    text += "Укажите название файла в формате /files имя файла";
                    connectionService.writeMessage(new Message(text));
                    //connectionService.getFile();
                } else if (message.getText().startsWith("/files ")) {
                    String pathToFile = fileDirectory + "/" + message.getText().substring(7);
                    try (FileReader fileReader = new FileReader(pathToFile)){
                        char[] content = new char[(int)(Path.of(pathToFile).toFile().length())];
                        fileReader.read(content);
                        text = "Содержимое файла : " + new String(content);
                    } catch (FileNotFoundException e) {
                        System.out.println("Файл не найден" + pathToFile);
                        //throw new RuntimeException(e);
                    } catch (IOException e) {
                        System.out.println("Ошибка при чтении файла");
                        //throw new RuntimeException(e);
                    }
                    connectionService.writeMessage(new Message(text));
                }
                else {
                    text = "Message from Client_" + clients.get(connectionService) + ": " + message.getText();
                }
                if (!message.getText().startsWith("/files")) {
                for (Map.Entry<ConnectionService, Integer> entry : clients.entrySet()) {
                    if (!connectionService.equals(entry.getKey())) {
                        System.out.println("Server: sent message " + message.getText());
                        entry.getKey().writeMessage(new Message(text));
                    } else {
                        connectionService.writeMessage(new Message("Сообщение успешно отправлено"));
                    }
                }
                }
            } catch (IOException e) {
                System.out.println("Сервер: Ошибка получения сообщения, соединение будет удалено");
                //System.out.println(e.getMessage());
                //connectionServices.remove(connectionService);
                clients.remove(connectionService);
                //throw new RuntimeException(e);
                break;
            }
        }
    }
}
