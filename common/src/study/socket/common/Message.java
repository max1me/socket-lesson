package study.socket.common;


import java.time.ZonedDateTime;

public class Message {
    private final String text;
    private ZonedDateTime sentAt;

    public Message (String text) {
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
