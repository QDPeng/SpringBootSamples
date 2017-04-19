package com.penck.producer.domain;

/**
 * Created by peng on 2017/4/19.
 */
public class MessagePayload {
    String topic;
    String message;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
