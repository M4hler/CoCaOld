package com.cthulhu.listeners;

import com.cthulhu.events.client.EventRoll;
import com.cthulhu.events.client.EventUseLuck;
import com.cthulhu.events.server.EventRollResult;
import com.cthulhu.events.EventType;
import com.cthulhu.events.server.EventUseLuckResult;
import com.cthulhu.models.Investigator;
import com.cthulhu.services.DiceRollingService;
import com.cthulhu.services.InvestigatorService;
import com.cthulhu.services.LuckService;
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
    private final LuckService luckService;

    public CustomListener(String queueName, DiceRollingService diceRollingService, InvestigatorService investigatorService, LuckService luckService) {
        this.queueName = queueName;
        objectMapper = new ObjectMapper();
        this.diceRollingService = diceRollingService;
        this.investigatorService = investigatorService;
        this.luckService = luckService;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String messageBody = message.getBody(String.class);
            JSONObject json = new JSONObject(messageBody);
            EventType type = EventType.valueOf(json.getString("eventType"));

            switch(type) {
                case ROLL -> {
                    EventRoll event = objectMapper.readValue(messageBody, EventRoll.class);
                    List<Investigator> investigatorTargets = investigatorService.getInvestigatorsWithNames(event.getInvestigatorTargets());
                    List<EventRollResult> rollResults = diceRollingService.rollTestsAgainstTargetValue(event.getDie(),
                            investigatorTargets, event.getTargetSkill(), event.getDifficulty(), event.getBonusDice(),
                            event.isAllowLuck(), event.isAllowPush());

                    for(EventRollResult e : rollResults) {
                        System.out.println("Rolled " + e.getResult() + " from " + event.getDie() + " for " + e.getInvestigatorName() +
                                " with success level of " + e.getGradation() + " at " + queueName);
                    }
                }
                case USE_LUCK -> {
                    EventUseLuck event = objectMapper.readValue(messageBody, EventUseLuck.class);
                    Investigator investigator = investigatorService.getInvestigatorByName(event.getInvestigatorName());
                    EventUseLuckResult eventResult = luckService.useLuck(investigator, event.getResult(), event.getGradation(), event.getTargetSkill());

                    System.out.println("Investigator " + eventResult.getInvestigatorName() + " used " + eventResult.getLuckUsed() +
                            "for skill " + eventResult.getTargetSkill() + " to achieve " + eventResult.getAchievedGradation());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
