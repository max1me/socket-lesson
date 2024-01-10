package study.socket.client;

import study.socket.common.ConnectionService;
import study.socket.common.Message;
import study.socket.common.TextFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class ClientWriter extends Thread{
    private ConnectionService connectionService;

    public ClientWriter(ConnectionService connectionService) {
        this.connectionService = Objects.requireNonNull(connectionService);
    }
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Для загрузки файла введите /load");
        System.out.println("Для получения списка файлов введите /files");
        System.out.println("Для выхода из программы введите /exit");
        while (true) {
            System.out.println("Введите сообщение");
            String text = scanner.nextLine();
            if (text.equals("/exit")) {
                System.exit(0);
            } else {
                TextFile textFile = null;
                if (text.equals("/load")) {
                    textFile = connectionService.loadFile();
                }
                if (text.startsWith("/files ")) {
                    text = connectionService.getFile();
                    System.out.println(text);
                }
                Message message = null;
                if (textFile != null) {
                    message = new Message("/load", textFile);
                } else {
                    message = new Message(text);
                }
                try {
                    connectionService.writeMessage(message);
                } catch (IOException e) {
                    System.out.println("Client: error while sending message");
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
