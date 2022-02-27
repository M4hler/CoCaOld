package com.cthulhu.listeners;

import com.cthulhu.models.Investigator;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class Receiver {
    @JmsListener(destination = "queue")
    public void receiveMessage(Investigator investigator) {
        System.out.println("Received message: " + investigator.getOwner() + " " + investigator.getName());
    }
}
