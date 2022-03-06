package com.cthulhu.services;

import com.cthulhu.enums.RollGradation;
import com.cthulhu.events.server.EventUseLuckResult;
import com.cthulhu.models.Investigator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LuckServiceTest {
    @Autowired
    private LuckService luckService;

    @Test
    public void useLuckToGetRegularSuccess() throws Exception {
        Investigator investigator = Investigator.builder().name("Bob").strength(60).luck(50).build();

        EventUseLuckResult event = luckService.useLuck(investigator, 62, RollGradation.REGULAR, "strength");
        Assertions.assertEquals(48, investigator.getLuck());
        Assertions.assertEquals("Bob", event.getInvestigatorName());
        Assertions.assertEquals(2, event.getLuckUsed());
        Assertions.assertEquals(RollGradation.REGULAR, event.getAchievedGradation());
        Assertions.assertEquals("strength", event.getTargetSkill());
    }

    @Test
    public void useLuckToGetHardSuccess() throws Exception {
        Investigator investigator = Investigator.builder().name("Bob").strength(60).luck(50).build();

        EventUseLuckResult event = luckService.useLuck(investigator, 62, RollGradation.HARD, "strength");
        Assertions.assertEquals(18, investigator.getLuck());
        Assertions.assertEquals("Bob", event.getInvestigatorName());
        Assertions.assertEquals(32, event.getLuckUsed());
        Assertions.assertEquals(RollGradation.HARD, event.getAchievedGradation());
        Assertions.assertEquals("strength", event.getTargetSkill());
    }

    @Test
    public void useLuckToGetExtremeSuccess() throws Exception {
        Investigator investigator = Investigator.builder().name("Bob").strength(60).luck(50).build();

        EventUseLuckResult event = luckService.useLuck(investigator, 62, RollGradation.EXTREME, "strength");
        Assertions.assertEquals(0, investigator.getLuck());
        Assertions.assertEquals("Bob", event.getInvestigatorName());
        Assertions.assertEquals(50, event.getLuckUsed());
        Assertions.assertEquals(RollGradation.EXTREME, event.getAchievedGradation());
        Assertions.assertEquals("strength", event.getTargetSkill());
    }

    @Test
    public void notEnoughLuck() throws Exception {
        Investigator investigator = Investigator.builder().name("Bob").strength(60).luck(50).build();

        EventUseLuckResult event = luckService.useLuck(investigator, 62, RollGradation.CRITICAL, "strength");
        Assertions.assertEquals(50, investigator.getLuck());
        Assertions.assertEquals("Bob", event.getInvestigatorName());
        Assertions.assertEquals(0, event.getLuckUsed());
        Assertions.assertEquals(RollGradation.FAILURE, event.getAchievedGradation());
        Assertions.assertEquals("strength", event.getTargetSkill());
    }
}
