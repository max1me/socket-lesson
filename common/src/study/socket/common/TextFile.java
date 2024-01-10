package study.socket.common;

import java.io.Serializable;

public class TextFile implements Serializable {
    private String fileName;
    private String description;
    private String text;

    public String getFileName() {
        return fileName;
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }

    public TextFile(String fileName, String description, String text) {
        this.fileName = fileName;
        this.description = description;
        this.text = text;
    }
}
