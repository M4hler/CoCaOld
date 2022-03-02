package com.cthulhu.services;

import com.cthulhu.enums.RollGradation;
import com.cthulhu.events.EventType;
import com.cthulhu.events.server.EventDevelopResult;
import com.cthulhu.events.server.EventPushResult;
import com.cthulhu.events.server.EventRollResult;
import com.cthulhu.models.Investigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DiceRollingService {
    private final GeneratorService generatorService;
    private final InvestigatorService investigatorService;

    @Autowired
    public DiceRollingService(InvestigatorService investigatorService, GeneratorService generatorService) {
        this.generatorService = generatorService;
        this.investigatorService = investigatorService;
    }

    public List<EventRollResult> rollTestsAgainstTargetValue(Integer dice, List<Investigator> investigators,
                                                             String targetSkill, RollGradation difficulty,
                                                             Integer bonusDice, boolean allowLuck, boolean allowPush) throws Exception {
        List<EventRollResult> result = new ArrayList<>();

        for(Investigator i : investigators) {
            Object[] rollResult = rollDiceAgainstThreshold(i.getFieldValueByName(targetSkill), difficulty, bonusDice);
            int roll = (int)rollResult[0];
            RollGradation gradation = (RollGradation)rollResult[1];

            if(gradation == RollGradation.REGULAR || gradation == RollGradation.HARD ||
                    gradation == RollGradation.EXTREME || gradation == RollGradation.CRITICAL) {
                investigatorService.addToSuccessfullyUsedSkills(i, targetSkill);
            }

            EventRollResult eventRollResult = new EventRollResult(i.getName(), roll, gradation, targetSkill, allowLuck, allowPush);
            eventRollResult.setTargetQueue(i.getName());
            eventRollResult.setEventType(EventType.ROLL_RESULT);
            result.add(eventRollResult);
        }

        return result;
    }

    public EventPushResult rollPushTest(Investigator investigator, RollGradation previousGradation, RollGradation currentGradation,
                                        String targetSkill) throws Exception {
        Object[] rollResult = rollDiceAgainstThreshold(investigator.getFieldValueByName(targetSkill), currentGradation, 0);
        int roll = (int)rollResult[0];
        RollGradation gradation = (RollGradation)rollResult[1];

        if(previousGradation == RollGradation.FAILURE &&
                (gradation == RollGradation.REGULAR || gradation == RollGradation.HARD ||
                gradation == RollGradation.EXTREME || gradation == RollGradation.CRITICAL)) {
            investigatorService.addToSuccessfullyUsedSkills(investigator, targetSkill);
        }
        else if((gradation == RollGradation.FAILURE || gradation == RollGradation.FUMBLE) &&
                    (previousGradation == RollGradation.REGULAR || previousGradation == RollGradation.HARD ||
                            previousGradation == RollGradation.EXTREME || previousGradation == RollGradation.CRITICAL)) {
            investigatorService.reduceSuccessfullyUsedSkill(investigator, targetSkill);
        }

        EventPushResult eventPushResult = new EventPushResult(investigator.getName(), roll, previousGradation, gradation, targetSkill);
        eventPushResult.setTargetQueue(investigator.getName());
        eventPushResult.setEventType(EventType.PUSH_RESULT);
        return eventPushResult;
    }

    public EventDevelopResult rollDevelopTest(Investigator investigator, String targetSkill) throws Exception {
        int roll = generatorService.rollDice(100);
        int skill = investigator.getFieldValueByName(targetSkill);

        EventDevelopResult eventResult = new EventDevelopResult();
        eventResult.setSkillGain(0);

        if(roll > skill || roll > 95) {
            int gain = generatorService.rollDice(10);
            investigator.setFieldValueByName(targetSkill, skill + gain);
            investigatorService.saveInvestigator(investigator);

            eventResult.setSkillGain(gain);
        }

        eventResult.setRollResult(roll);
        eventResult.setInvestigatorName(investigator.getName());
        eventResult.setTargetSkill(targetSkill);
        eventResult.setTargetQueue(investigator.getName());
        eventResult.setEventType(EventType.DEVELOP_RESULT);

        return eventResult;
    }

    private Object[] rollDiceAgainstThreshold(Integer threshold, RollGradation difficulty, Integer bonusDice) {
        int roll;
        if(bonusDice == 0) {
            roll = generatorService.rollDice(100);
        }
        else {
            int unitDigit = generatorService.rollDice(10) % 10;

            List<Integer> tensDigits = new ArrayList<>();
            tensDigits.add(generatorService.rollDice(10));
            for(int i = 0; i < Math.abs(bonusDice); i++) {
                tensDigits.add(generatorService.rollDice(10));
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
