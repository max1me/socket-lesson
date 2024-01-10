package study.soket.server;

import study.socket.common.ConnectionService;

import java.util.concurrent.ConcurrentHashMap;

public class ServerHandler {
    private ConcurrentHashMap<ConnectionService, Integer> clients;
    private ConcurrentHashMap<String, String> filesWithDescription;

    public ServerHandler() {
        this.clients = new ConcurrentHashMap<>();
        this.filesWithDescription = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<ConnectionService, Integer> getClients() {
        return clients;
    }

    public ConcurrentHashMap<String, String> getFilesWithDescription() {
        return filesWithDescription;
    }
}
