package com.cthulhu.services;

import com.cthulhu.enums.RollGraduation;
import com.cthulhu.events.EventRollResult;
import com.cthulhu.models.Investigator;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiceRollingService {
    private final SecureRandom generator;

    public DiceRollingService() {
        generator = new SecureRandom();
    }

    public List<EventRollResult> rollTestsAgainstTargetValue(Integer dice, List<Investigator> investigators, String targetSkill) throws Exception {
        List<EventRollResult> result = new ArrayList<>();

        for(Investigator i : investigators) {
            Object[] rollResult = rollDiceAgainstThreshold(dice, i.getFieldValueByName(targetSkill));
            int roll = (int)rollResult[0];
            RollGraduation graduation = (RollGraduation)rollResult[1];
            result.add(new EventRollResult(i.getName(), roll, graduation));
        }

        return result;
    }

    public int rollDice(Integer number) {
        return generator.nextInt(number) + 1;
    }

    private Object[] rollDiceAgainstThreshold(Integer dice, Integer threshold) {
        int roll = rollDice(dice);

        if(roll > threshold) {
            if((threshold < 50 && roll >= 96) ||
                    (threshold >= 50 && roll == 100)) {
                return new Object[]{roll, RollGraduation.FUMBLE};
            }
            else {
                return new Object[]{roll, RollGraduation.FAILURE};
            }
        }
        else if(roll == 1) {
            return new Object[]{roll, RollGraduation.CRITICAL};
        }
        else if(roll <= 0.2 * threshold){
            return new Object[]{roll, RollGraduation.EXTREME};
        }
        else if(roll <= 0.5 * threshold) {
            return new Object[]{roll, RollGraduation.HARD};
        }
        else {
            return new Object[]{roll, RollGraduation.REGULAR};
        }
    }
}
