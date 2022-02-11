package com.cthulhu.models;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.jms.Message;
import javax.jms.MessageListener;

public class CustomListener implements MessageListener {
    private final String queueName;
    private final ObjectMapper objectMapper;

    public CustomListener(String queueName) {
        this.queueName = queueName;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message message) {
        try {
            Character character = objectMapper.readValue(message.getBody(String.class), Character.class);
            System.out.println("At queue: " + queueName + " received message: " + character.getOwner() + " " + character.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
