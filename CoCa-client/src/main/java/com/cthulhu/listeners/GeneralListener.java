package com.cthulhu.listeners;

import com.cthulhu.events.EventType;
import org.json.JSONObject;

import javax.jms.Message;
import javax.jms.MessageListener;

public class GeneralListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            String messageBody = message.getBody(String.class);
            JSONObject json = new JSONObject(messageBody);
            EventType type = EventType.valueOf(json.getString("eventType"));

            switch(type) {

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
