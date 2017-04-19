package com.penck.consumer.domain;

/**
 * Created by peng on 2017/4/19.
 */
public class UplinkMessage {
    private String topic;
    private String message;

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

    @Override
    public String toString() {
        return "UplinkMessage [topic=" + topic + ", message=" + message + "]";
    }
}
