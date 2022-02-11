package com.cthulhu.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class DiceRollingService {
    private final SecureRandom generator;

    public DiceRollingService() {
        generator = new SecureRandom();
    }

    public int rollDice(Integer dice) {
        return generator.nextInt(dice) + 1;
    }
}
