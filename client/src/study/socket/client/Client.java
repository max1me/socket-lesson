package study.socket.client;

import study.socket.common.ConnectionService;
import study.socket.common.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread implements Runnable{
    private InetSocketAddress remoteAddress;

    public Client(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            Socket socket = new Socket(remoteAddress.getHostString(), remoteAddress.getPort());
            try  {
                ConnectionService service = new ConnectionService(socket);
                ClientReader clientReader = new ClientReader(service);
                clientReader.start();
                ClientWriter clientWriter = new ClientWriter(service);
                clientWriter.start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("Сервер перестал отвечать");
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Сервер недоступен");
            System.out.println(e.getMessage());
        }
    }
}
