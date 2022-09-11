package com.cthulhu.events;

import com.cthulhu.controllers.JmsController;
import com.cthulhu.enums.RollGradation;
import com.cthulhu.events.client.EventRoll;
import com.cthulhu.models.Investigator;
import com.cthulhu.services.DiceRollingService;
import com.cthulhu.services.GeneratorService;
import com.cthulhu.services.InvestigatorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class EventRollTest {
    @MockBean
    private GeneratorService generatorService;
    @Autowired
    private DiceRollingService diceRollingService;
    @Autowired
    private InvestigatorService investigatorService;
    @Autowired
    private JmsController jmsController;

    @BeforeEach
    public void beforeAll() {
        Investigator investigator = Investigator.builder().name("John").strength(50).build();
        investigatorService.saveInvestigator(investigator);
    }

    @AfterEach
    public void afterEach() {
        investigatorService.deleteAll();
    }

    @Test
    public void eventRollReceived() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(40);

        jmsController.createQueue("newQueue");
        EventRoll eventRoll = createEvent(100,"newQueue", List.of("John"), "strength");
        jmsController.sendToQueue(eventRoll);

        //await().until(() -> condition);
        //for now going with simple timeout, will switch to await when listener actually performs some actions
        //that have visible status and can be periodically checked to determine if message was consumed
        verify(generatorService, timeout(200).times(1)).rollDice(any());
        //verify(diceRollingService, times(1)).rollTestsAgainstTargetValue(any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());
    }

    @Test
    public void twoEventRollsReceived() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(40);

        jmsController.createQueue("newQueue");
        EventRoll eventRoll = createEvent(100,"newQueue", List.of("John"), "strength");
        jmsController.sendToQueue(eventRoll);
        jmsController.sendToQueue(eventRoll);

        verify(generatorService, timeout(400).times(2)).rollDice(any());
        //verify(diceRollingService, times(2)).rollTestsAgainstTargetValue(any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());
    }

    @Test
    public void twoEventsSendAndOneReceived() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(40);

        jmsController.createQueue("newQueue");

        EventRoll eventRoll1 = createEvent(100,"newQueue", List.of("John"), "strength");
        jmsController.sendToQueue(eventRoll1);
        EventRoll eventRoll2 = createEvent(100,"oldQueue", List.of("John"), "strength");
        jmsController.sendToQueue(eventRoll2);

        verify(generatorService, timeout(200).times(1)).rollDice(any());
    }

    @Test
    public void twoEventsSendToTwoDifferentQueues() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(40);

        jmsController.createQueue("newQueue1");
        EventRoll eventRoll1 = createEvent(100,"newQueue1", List.of("John"), "strength");
        jmsController.sendToQueue(eventRoll1);

        jmsController.createQueue("newQueue2");
        EventRoll eventRoll2 = createEvent(100,"newQueue2", List.of("John"), "strength");
        jmsController.sendToQueue(eventRoll2);

        verify(generatorService, timeout(200).times(2)).rollDice(any());
    }

    private EventRoll createEvent(int die, String queue, List<String> investigatorTargets, String targetSkill) {
        EventRoll eventRoll = new EventRoll(die, investigatorTargets, targetSkill, RollGradation.REGULAR, 0, false, false);
        eventRoll.setEventType(EventType.ROLL);
        eventRoll.setTargetQueue(queue);
        return eventRoll;
    }
}
