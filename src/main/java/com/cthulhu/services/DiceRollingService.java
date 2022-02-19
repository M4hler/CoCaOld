package com.cthulhu.services;

import com.cthulhu.enums.RollGradation;
import com.cthulhu.events.server.EventRollResult;
import com.cthulhu.models.Investigator;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DiceRollingService {
    private final SecureRandom generator;

    public DiceRollingService() {
        generator = new SecureRandom();
    }

    public List<EventRollResult> rollTestsAgainstTargetValue(Integer dice, List<Investigator> investigators,
                                                             String targetSkill, RollGradation difficulty, Integer bonusDice) throws Exception {
        List<EventRollResult> result = new ArrayList<>();

        for(Investigator i : investigators) {
            Object[] rollResult = rollDiceAgainstThreshold(i.getFieldValueByName(targetSkill), difficulty, bonusDice);
            int roll = (int)rollResult[0];
            RollGradation gradation = (RollGradation)rollResult[1];
            result.add(new EventRollResult(i.getName(), roll, gradation));
        }

        return result;
    }

    public int rollDice(Integer number) {
        return generator.nextInt(number) + 1;
    }

    private Object[] rollDiceAgainstThreshold(Integer threshold, RollGradation difficulty, Integer bonusDice) {
        int roll;
        if(bonusDice == 0) {
            roll = rollDice(100);
        }
        else {
            int unitDigit = rollDice(10) % 10;

            List<Integer> tensDigits = new ArrayList<>();
            tensDigits.add(rollDice(10));
            for(int i = 0; i < Math.abs(bonusDice); i++) {
                tensDigits.add(rollDice(10));
            }

            int tens;
            if(bonusDice < 0) {
                tens = Collections.max(tensDigits);
            }
            else {
                tens = Collections.min(tensDigits);
            }

            roll = 10 * tens + unitDigit;
        }

        int difficultyThreshold = threshold;
        if(difficulty == RollGradation.HARD) {
            difficultyThreshold = (int)(0.5 * threshold);
        }
        else if(difficulty == RollGradation.EXTREME) {
            difficultyThreshold = (int)(0.2 * threshold);
        }

        if(roll == 1) {
            return new Object[]{roll, RollGradation.CRITICAL};
        }
        if(roll > difficultyThreshold) {
            if((difficultyThreshold < 50 && roll >= 96) ||
                    (difficultyThreshold >= 50 && roll == 100)) {
                return new Object[]{roll, RollGradation.FUMBLE};
            }
            else {
                return new Object[]{roll, RollGradation.FAILURE};
            }
        }
        else if(roll <= 0.2 * threshold){
            return new Object[]{roll, RollGradation.EXTREME};
        }
        else if(roll <= 0.5 * threshold) {
            return new Object[]{roll, RollGradation.HARD};
        }
        else {
            return new Object[]{roll, RollGradation.REGULAR};
        }
    }
}
