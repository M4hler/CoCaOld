package com.cthulhu.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class GeneratorService {
    private final SecureRandom generator;

    public GeneratorService() {
        this.generator = new SecureRandom();
    }

    public int rollDice(Integer number) {
        return generator.nextInt(number) + 1;
    }
}
