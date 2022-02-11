package com.cthulhu.controllers;

import com.cthulhu.events.EventRoll;
import com.cthulhu.models.Character;
import com.cthulhu.models.CustomListener;
import com.cthulhu.services.DiceRollingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;

@RestController
public class JmsController {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private DiceRollingService diceRollingService;

    @GetMapping("/get")
    public void simpleGet() {
        jmsTemplate.convertAndSend("queue", new Character("Oskar", "Lokes"));
    }

    @PostMapping("/createQueue")
    public void createQueue(@RequestBody Character character) throws JMSException {
        Session session = connectionFactory.createConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(character.getName());
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new CustomListener(character.getName(), diceRollingService));
    }

    @PostMapping("/sendToQueue")
    public void sendToQueue(@RequestBody EventRoll event) {
        jmsTemplate.convertAndSend(event.getTargetQueue(), event);
    }
}
