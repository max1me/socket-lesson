package study.socket.common;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Scanner;

public class ConnectionService implements AutoCloseable{
    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    public ConnectionService(Socket socket) throws IOException {
        this.socket = Objects.requireNonNull(socket, "socket не может быть null");
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
    }

    public void writeMessage(Message message) throws IOException {
        message.setSentAt(ZonedDateTime.now());
        outputStream.writeObject(message);
        outputStream.flush();
    }

    public Message readMessage() throws IOException {
        try {
            return (Message) inputStream.readObject();
        }  catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public TextFile loadFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите путь к файлу");
        Path path = Path.of(scanner.nextLine());
        String fileName = path.getFileName().toString();
        System.out.println("Имя файла: " + fileName);
        if (fileName == null || !fileName.endsWith(".txt") || fileName.length() < 4) {
            System.out.println("Имя файла должно быть в формате *.txt");
        } else {
            System.out.println("Введите описание файла");
            String fileDescription = scanner.nextLine();
            if (fileDescription == null || fileDescription.length() > 64) {
                System.out.println("Описание файла должно быть заполнено и не превышать 64 символа");
            } else {
                File file = path.toFile();
                //1MB
                if (file.length() > 1024 * 1024) {
                    System.out.println("Размер файла должен быть меньше 1 МБ");
                } else {
                    try (FileReader fileReader = new FileReader(file)){
                        char[] text = new char[(int)file.length()];
                        fileReader.read(text);
                        TextFile textFile = new TextFile(fileName, fileDescription, new String(text));
                        return textFile;
                    } catch (FileNotFoundException e) {
                        System.out.println("Файл не найден");
                        //throw new RuntimeException(e);
                    } catch (IOException e) {
                        System.out.println("Ошибка при чтении файла");
                        //throw new RuntimeException(e);
                    }
                }
            }
        }
        return null;
    }
/*    public String getFile(String fileName) {
        return fileName;
    }*/


    @Override
    public void close() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
            System.out.println("Ошибка во время закрытия ресурсов");
        }
    }
}