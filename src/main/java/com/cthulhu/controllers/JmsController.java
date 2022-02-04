package com.cthulhu.controllers;

import com.cthulhu.models.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JmsController {
    @Autowired
    private JmsTemplate jmsTemplate;

    @GetMapping("/get")
    public void simpleGet() {
        jmsTemplate.convertAndSend("queue", new Character("Oskar", "Lokes"));
    }
}
