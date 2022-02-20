package com.cthulhu.models;

import com.cthulhu.events.client.EventRoll;
import com.cthulhu.events.client.EventUseLuck;
import com.cthulhu.events.server.EventRollResult;
import com.cthulhu.events.EventType;
import com.cthulhu.services.DiceRollingService;
import com.cthulhu.services.InvestigatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.List;

public class CustomListener implements MessageListener {
    private final String queueName;
    private final ObjectMapper objectMapper;
    private final DiceRollingService diceRollingService;
    private final InvestigatorService investigatorService;

    public CustomListener(String queueName, DiceRollingService diceRollingService, InvestigatorService investigatorService) {
        this.queueName = queueName;
        objectMapper = new ObjectMapper();
        this.diceRollingService = diceRollingService;
        this.investigatorService = investigatorService;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String messageBody = message.getBody(String.class);
            JSONObject json = new JSONObject(messageBody);
            EventType type = EventType.valueOf(json.getString("eventType"));

            switch(type) {
                case ROLL: {
                    EventRoll event = objectMapper.readValue(messageBody, EventRoll.class);
                    List<Investigator> investigatorTargets = investigatorService.getInvestigatorsWithNames(event.getInvestigatorTargets());
                    List<EventRollResult> rollResults = diceRollingService.rollTestsAgainstTargetValue(event.getDie(),
                            investigatorTargets, event.getTargetSkill(), event.getDifficulty(), event.getBonusDice(),
                            event.isAllowLuck(), event.isAllowPush());

                    for(EventRollResult e : rollResults) {
                        System.out.println("Rolled " + e.getResult() + " from " + event.getDie() + " for " + e.getInvestigatorName() +
                                " with success level of " + e.getGradation() + " at " + queueName);
                    }
                    break;
                }

                case USELUCK: {
                    EventUseLuck event = objectMapper.readValue(messageBody, EventUseLuck.class);
                    //TODO create service to use luck
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
