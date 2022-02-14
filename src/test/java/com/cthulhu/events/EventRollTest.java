package com.cthulhu.events;

import com.cthulhu.controllers.JmsController;
import com.cthulhu.services.DiceRollingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EventRollTest {
    @MockBean
    private DiceRollingService diceRollingService;
    @Autowired
    private JmsController jmsController;

    @Test
    public void eventRollReceived() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(5);

        jmsController.createQueue("newQueue");
        EventRoll eventRoll = createEvent(10, EventType.ROLL, "newQueue");
        jmsController.sendToQueue(eventRoll);

        //await().until(() -> condition);
        //for now going with simple timeout, will switch to await when listener actually performs some actions
        //that have visible status and can be periodically checked to determine if message was consumed
        verify(diceRollingService, timeout(100).times(1)).rollDice(any());
    }

    @Test
    public void twoEventRollsReceived() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(5);

        jmsController.createQueue("newQueue");
        EventRoll eventRoll = createEvent(10, EventType.ROLL, "newQueue");
        jmsController.sendToQueue(eventRoll);
        jmsController.sendToQueue(eventRoll);

        verify(diceRollingService, timeout(100).times(2)).rollDice(any());
    }

    @Test
    public void twoEventsSendAndOneReceived() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(5);

        jmsController.createQueue("newQueue");

        EventRoll eventRoll1 = createEvent(10, EventType.ROLL, "newQueue");
        jmsController.sendToQueue(eventRoll1);
        EventRoll eventRoll2 = createEvent(10, EventType.ROLL, "oldQueue");
        jmsController.sendToQueue(eventRoll2);

        verify(diceRollingService, timeout(100).times(1)).rollDice(any());
    }

    @Test
    public void twoEventsSendToTwoDifferentQueues() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(5);

        jmsController.createQueue("newQueue1");
        EventRoll eventRoll1 = createEvent(10, EventType.ROLL, "newQueue1");
        jmsController.sendToQueue(eventRoll1);

        jmsController.createQueue("newQueue2");
        EventRoll eventRoll2 = createEvent(10, EventType.ROLL, "newQueue2");
        jmsController.sendToQueue(eventRoll2);

        verify(diceRollingService, timeout(100).times(2)).rollDice(any());
    }

    private EventRoll createEvent(int die, EventType type, String queue) {
        EventRoll eventRoll = new EventRoll(die);
        eventRoll.setEventType(type);
        eventRoll.setTargetQueue(queue);
        return eventRoll;
    }
}
