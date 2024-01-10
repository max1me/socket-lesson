package study.socket.client;


import java.net.InetSocketAddress;
import java.util.Scanner;

public class ClientApplication {
    public static void main(String[] args) {
        Client client = new Client(new InetSocketAddress("127.0.0.1", 2222));
        client.start();
    }
}
