package com.cthulhu.controllers;

import com.cthulhu.events.client.EventRoll;
import com.cthulhu.models.Investigator;
import com.cthulhu.listeners.CustomListener;
import com.cthulhu.services.DiceRollingService;
import com.cthulhu.services.InvestigatorService;
import com.cthulhu.services.LuckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.*;

@RestController
public class JmsController {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private DiceRollingService diceRollingService;
    @Autowired
    private InvestigatorService investigatorService;
    @Autowired
    private LuckService luckService;
    @Autowired
    private ConnectionFactory connectionFactory;

    @GetMapping("/get")
    public void simpleGet() {
        jmsTemplate.convertAndSend("queue", Investigator.builder().owner("Oskar").name("John").build());
    }

    @PostMapping("/createQueue")
    public void createQueue(@RequestBody String name) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(name);
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new CustomListener(name, jmsTemplate, diceRollingService, investigatorService, luckService));
    }

    @PostMapping("/sendToQueue")
    public void sendToQueue(@RequestBody EventRoll event) {
        jmsTemplate.convertAndSend(event.getTargetQueue(), event);
    }
}
