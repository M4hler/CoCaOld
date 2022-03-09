package com.cthulhu.listeners;

import com.cthulhu.events.EventType;
import com.cthulhu.events.server.EventInvestigatorsResult;
import com.cthulhu.scenes.FirstScene;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import javax.jms.Message;
import javax.jms.MessageListener;

public class GeneralListener implements MessageListener {
    private FirstScene scene;
    private final ObjectMapper objectMapper;

    public GeneralListener() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message message) {
        try {
            String messageBody = message.getBody(String.class);
            JSONObject json = new JSONObject(messageBody);
            EventType type = EventType.valueOf(json.getString("eventType"));

            switch(type) {
                case INVESTIGATORS_RESULT -> {
                    EventInvestigatorsResult event = objectMapper.readValue(messageBody, EventInvestigatorsResult.class);
                    for(int i = 0; i < event.getInvestigatorList().size(); i++) {
                        scene.addGridPane(event.getInvestigatorList().get(i));
                    }
                    System.out.println("got investigators list");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setScene(FirstScene scene) {
        this.scene = scene;
    }
}
