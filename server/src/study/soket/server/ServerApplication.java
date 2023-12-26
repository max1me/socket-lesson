package study.soket.server;

public class ServerApplication {
    public static void main(String[] args) {
        Server server = new Server(2222);
        server.run();
    }
}
