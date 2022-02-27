package com.cthulhu.services;

import com.cthulhu.enums.RollGradation;
import com.cthulhu.events.EventType;
import com.cthulhu.events.server.EventUseLuckResult;
import com.cthulhu.models.Investigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LuckService {
    @Autowired
    private InvestigatorService investigatorService;

    public EventUseLuckResult useLuck(Investigator investigator, int result, RollGradation gradation, String targetSkill) throws Exception {
        int luck = investigator.getLuck();
        int value = investigator.getFieldValueByName(targetSkill);
        int difference = 0;

        switch(gradation) {
            case REGULAR -> difference = result - value;
            case HARD -> difference = (int)(result - 0.5 * value);
            case EXTREME -> difference = (int)(result - 0.2 * value);
            case CRITICAL -> difference = result - 1;
        }

        EventUseLuckResult eventResult = new EventUseLuckResult();
        if(luck < difference) {
            eventResult.setLuckUsed(0);
            eventResult.setAchievedGradation(RollGradation.FAILURE);
        }
        else {
            investigator.setLuck(luck - difference);
            investigatorService.saveInvestigator(investigator);

            eventResult.setLuckUsed(difference);
            eventResult.setAchievedGradation(gradation);
        }

        eventResult.setInvestigatorName(investigator.getName());
        eventResult.setTargetSkill(targetSkill);
        eventResult.setTargetQueue(investigator.getName());
        eventResult.setEventType(EventType.USELUCKRESULT);
        return eventResult;
    }
}
