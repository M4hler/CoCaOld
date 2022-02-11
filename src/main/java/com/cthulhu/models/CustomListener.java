package com.cthulhu.models;

import com.cthulhu.events.EventRoll;
import com.cthulhu.events.EventType;
import com.cthulhu.services.DiceRollingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import javax.jms.Message;
import javax.jms.MessageListener;

public class CustomListener implements MessageListener {
    private final String queueName;
    private final ObjectMapper objectMapper;
    private final DiceRollingService diceRollingService;

    public CustomListener(String queueName, DiceRollingService diceRollingService) {
        this.queueName = queueName;
        objectMapper = new ObjectMapper();
        this.diceRollingService = diceRollingService;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String messageBody = message.getBody(String.class);
            JSONObject json = new JSONObject(messageBody);
            EventType type = EventType.valueOf(json.getString("eventType"));

            switch(type) {
                case ROLL:
                    EventRoll event = objectMapper.readValue(messageBody, EventRoll.class);
                    int a = diceRollingService.rollDice(event.getDie());
                    System.out.println("Rolled: " + a + " from " + event.getDie() + " die at " + queueName);
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
