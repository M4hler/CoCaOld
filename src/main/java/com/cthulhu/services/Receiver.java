package com.cthulhu.services;

import com.cthulhu.models.Character;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class Receiver {
    @JmsListener(destination = "queue")
    public void receiveMessage(Character character) {
        System.out.println("Received message: " + character.getOwner() + " " + character.getName());
    }
}
