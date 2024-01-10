package study.socket.common;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Message implements Serializable {
    private final String text;
    private ZonedDateTime sentAt;
    private TextFile textFile;

    public Message(String text, TextFile textFile) {
        this.text = text;
        this.textFile = textFile;
    }

    public TextFile getTextFile() {
        return textFile;
    }

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public ZonedDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
    }
}