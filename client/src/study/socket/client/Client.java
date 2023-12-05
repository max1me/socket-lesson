package study.socket.client;

import study.socket.common.ConnectionService;
import study.socket.common.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


/*
2. Клиент:
2.1. запрашивает текст сообщения (запрос) у пользователя
2.2. устанавливает соединение с сервером
2.3. создает экземпляр сообщения
2.4. отправляет сообщение на сервер
2.5. получает ответ
2.6. выводит полученный ответ в консоль
2.7. закрывает соединение с сервером
И так до тех пор, пока пользователь не введет '/exit'
*/
public class Client implements Runnable{
    private InetSocketAddress remoteAddress;



    public Client(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
/*        try {

            while (true) {
                System.out.println("Введите сообщение");
                String text = scanner.nextLine();
                if (text.equals("/exit")) break;
                ConnectionService connectionService = new ConnectionService(new Socket(remoteAddress.getHostName(), remoteAddress.getPort()));
                connectionService.writeMessage(new Message(text));
                Message message = connectionService.readMessage();
                System.out.println(message.getText());
                connectionService.close();
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    while (true) {
        System.out.println("Введите сообщение");
        String text = scanner.nextLine();
        if (text.equals("/exit")) break;
        try (Socket socket = new Socket()) {
            socket.connect(remoteAddress);
            try (ConnectionService service = new ConnectionService(socket)) {
                service.writeMessage(new Message(text));
                Message message = service.readMessage();
                System.out.println(message.getText());
            } catch (IOException e) {
                System.out.println("Сервер перестал отвечать");
            }
        } catch (IOException e) {
            System.out.println("Сервер недоступен");
        }
    }
    }
}
