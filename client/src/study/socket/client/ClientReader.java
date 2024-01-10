package study.socket.client;

import study.socket.common.ConnectionService;
import study.socket.common.Message;

import java.io.IOException;
import java.util.Objects;

public class ClientReader extends Thread{
    private ConnectionService connectionService;

    public ClientReader(ConnectionService connectionService) {
        this.connectionService = Objects.requireNonNull(connectionService);
    }
    @Override
    public void run() {
        while (true) {
            try {
                Message message = connectionService.readMessage();
                System.out.println(message.getText());
            } catch (IOException e) {
                System.out.println("Ошибка получения сообщения");
                System.out.println(e.getMessage());
                break;
            }
        }
    }
}
