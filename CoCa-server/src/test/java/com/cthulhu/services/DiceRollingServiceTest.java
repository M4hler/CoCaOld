package com.cthulhu.services;

import com.cthulhu.enums.RollGradation;
import com.cthulhu.events.server.EventDevelopResult;
import com.cthulhu.events.server.EventPushResult;
import com.cthulhu.events.server.EventRollResult;
import com.cthulhu.models.Investigator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DiceRollingServiceTest {
    @MockBean
    private GeneratorService generatorService;
    @Autowired
    private DiceRollingService diceRollingService;
    @Autowired
    private InvestigatorService investigatorService;

    @Test
    public void regularSuccess() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(40);

        Investigator investigator = Investigator.builder().name("Alice").strength(50).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR, 0, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.REGULAR, result.getGradation());
        Assertions.assertEquals(40, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void regularSuccessWithBonusDie() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(10).thenReturn(6).thenReturn(4);

        Investigator investigator = Investigator.builder().name("Alice").strength(50).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR, 1, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.REGULAR, result.getGradation());
        Assertions.assertEquals(40, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void hardSuccess() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(25);

        Investigator investigator = Investigator.builder().name("Alice").strength(50).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR, 0, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.HARD, result.getGradation());
        Assertions.assertEquals(25, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void extremeSuccess() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(10);

        Investigator investigator = Investigator.builder().name("Alice").strength(50).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR, 0, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.EXTREME, result.getGradation());
        Assertions.assertEquals(10, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void criticalSuccess() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(1);

        Investigator investigator = Investigator.builder().name("Alice").strength(50).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR, 0, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.CRITICAL, result.getGradation());
        Assertions.assertEquals(1, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void criticalSuccessButExtremeDifficulty() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(1);

        Investigator investigator = Investigator.builder().name("Alice").strength(4).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.EXTREME, 0, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.CRITICAL, result.getGradation());
        Assertions.assertEquals(1, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void regularSuccessAtLowValue() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(2);

        Investigator investigator = Investigator.builder().name("Alice").strength(3).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR, 0, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.REGULAR, result.getGradation());
        Assertions.assertEquals(2, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void failure() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(60);

        Investigator investigator = Investigator.builder().name("Alice").strength(50).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR, 0, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FAILURE, result.getGradation());
        Assertions.assertEquals(60, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void failureWithPenaltyDie() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(1).thenReturn(2).thenReturn(5);

        Investigator investigator = Investigator.builder().name("Alice").strength(50).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR, -1, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FAILURE, result.getGradation());
        Assertions.assertEquals(51, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void failureWhenRegularSuccessButHardDifficulty() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(40);

        Investigator investigator = Investigator.builder().name("Alice").strength(60).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.HARD, 0, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FAILURE, result.getGradation());
        Assertions.assertEquals(40, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void failureWhenRegularSuccessButExtremeDifficulty() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(40);

        Investigator investigator = Investigator.builder().name("Alice").strength(60).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.EXTREME, 0, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FAILURE, result.getGradation());
        Assertions.assertEquals(40, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void failureWhenHardSuccessButExtremeDifficulty() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(20);

        Investigator investigator = Investigator.builder().name("Alice").strength(60).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.EXTREME, 0, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FAILURE, result.getGradation());
        Assertions.assertEquals(20, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void fumbleWhenSkillGreaterOrEqualTo50() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(100);

        Investigator investigator = Investigator.builder().name("Alice").strength(50).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR, 0, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FUMBLE, result.getGradation());
        Assertions.assertEquals(100, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void fumbleWhenSkillLowerThan50() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(96);

        Investigator investigator = Investigator.builder().name("Alice").strength(49).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR, 0, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FUMBLE, result.getGradation());
        Assertions.assertEquals(96, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void fumbleWhenSkillGreaterThan50ButHardDifficulty() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(98);

        Investigator investigator = Investigator.builder().name("Alice").strength(60).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.HARD, 0, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FUMBLE, result.getGradation());
        Assertions.assertEquals(98, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void fumbleWithPenaltyDie() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(0).thenReturn(1).thenReturn(10);

        Investigator investigator = Investigator.builder().name("Alice").strength(50).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR, -1, false, false);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FUMBLE, result.getGradation());
        Assertions.assertEquals(100, result.getResult());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void developTest() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(50).thenReturn(3);

        Investigator investigator = Investigator.builder().name("Alice").build();
        investigatorService.addSkill(investigator, "accounting", 5, "base", 40);

        investigatorService.saveInvestigator(investigator);

        EventDevelopResult eventResult = diceRollingService.rollDevelopTest(investigator, "accounting");
        Assertions.assertEquals(50, eventResult.getRollResult());
        Assertions.assertEquals(3, eventResult.getSkillGain());
        Assertions.assertEquals("accounting", eventResult.getTargetSkill());
        Assertions.assertEquals("Alice", eventResult.getInvestigatorName());

        Investigator i = investigatorService.getInvestigatorByName("Alice");
        Assertions.assertEquals(43, i.getFieldValueByName("accounting"));
    }

    @Test
    public void developTestWithHighSkill() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(99).thenReturn(3);

        Investigator investigator = Investigator.builder().name("Alice").build();
        investigatorService.addSkill(investigator, "accounting", 5, "base", 96);

        investigatorService.saveInvestigator(investigator);

        EventDevelopResult eventResult = diceRollingService.rollDevelopTest(investigator, "accounting");
        Assertions.assertEquals(99, eventResult.getRollResult());
        Assertions.assertEquals(3, eventResult.getSkillGain());
        Assertions.assertEquals("accounting", eventResult.getTargetSkill());
        Assertions.assertEquals("Alice", eventResult.getInvestigatorName());

        Investigator i = investigatorService.getInvestigatorByName("Alice");
        Assertions.assertEquals(99, i.getFieldValueByName("accounting"));
    }

    @Test
    public void developTestFail() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(40);

        Investigator investigator = Investigator.builder().name("Alice").build();
        investigatorService.addSkill(investigator, "accounting", 5, "base", 40);

        investigatorService.saveInvestigator(investigator);

        EventDevelopResult eventResult = diceRollingService.rollDevelopTest(investigator, "accounting");
        Assertions.assertEquals(40, eventResult.getRollResult());
        Assertions.assertEquals(0, eventResult.getSkillGain());
        Assertions.assertEquals("accounting", eventResult.getTargetSkill());
        Assertions.assertEquals("Alice", eventResult.getInvestigatorName());

        Investigator i = investigatorService.getInvestigatorByName("Alice");
        Assertions.assertEquals(40, i.getFieldValueByName("accounting"));
    }

    @Test
    public void firstTestSuccessThenDevelop() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(40).thenReturn(50).thenReturn(5);

        Investigator investigator = Investigator.builder().name("Alice").build();
        investigatorService.addSkill(investigator, "accounting", 5, "base", 40);

        investigatorService.saveInvestigator(investigator);
        List<Investigator> list = List.of(investigator);

        diceRollingService.rollTestsAgainstTargetValue(100, list, "accounting", RollGradation.REGULAR, 0, false, false);
        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().size());
        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().get("accounting"));

        EventDevelopResult eventResult = diceRollingService.rollDevelopTest(investigator, "accounting");
        Assertions.assertEquals(50, eventResult.getRollResult());
        Assertions.assertEquals(5, eventResult.getSkillGain());
        Assertions.assertEquals("accounting", eventResult.getTargetSkill());
        Assertions.assertEquals("Alice", eventResult.getInvestigatorName());

        Investigator i = investigatorService.getInvestigatorByName("Alice");
        Assertions.assertEquals(45, i.getFieldValueByName("accounting"));
    }

    @Test
    public void pushTestPrevSuccessNowSuccess() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(40);

        Investigator investigator = Investigator.builder().name("Alice").build();
        investigatorService.addSkill(investigator, "accounting", 5, "base", 40);

        investigatorService.saveInvestigator(investigator);
        List<Investigator> list = List.of(investigator);
        diceRollingService.rollTestsAgainstTargetValue(100, list, "accounting", RollGradation.REGULAR, 0, false, false);

        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().size());
        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().get("accounting"));

        EventPushResult eventResult = diceRollingService.rollPushTest(investigator, RollGradation.REGULAR, RollGradation.REGULAR, "accounting");
        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().size());
        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().get("accounting"));
        Assertions.assertEquals(40, eventResult.getRoll());
        Assertions.assertEquals(RollGradation.REGULAR, eventResult.getPreviousGradation());
        Assertions.assertEquals(RollGradation.REGULAR, eventResult.getAchievedGradation());
        Assertions.assertEquals("accounting", eventResult.getTargetSkill());
        Assertions.assertEquals("Alice", eventResult.getInvestigatorName());
    }

    @Test
    public void pushTestPrevFailureNowSuccess() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(50).thenReturn(40);

        Investigator investigator = Investigator.builder().name("Alice").build();
        investigatorService.addSkill(investigator, "accounting", 5, "base", 40);

        investigatorService.saveInvestigator(investigator);
        List<Investigator> list = List.of(investigator);
        diceRollingService.rollTestsAgainstTargetValue(100, list, "accounting", RollGradation.REGULAR, 0, false, false);

        Assertions.assertNull(investigator.getSuccessfullyUsedSkills());

        EventPushResult eventResult = diceRollingService.rollPushTest(investigator, RollGradation.FAILURE, RollGradation.REGULAR, "accounting");
        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().size());
        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().get("accounting"));
        Assertions.assertEquals(40, eventResult.getRoll());
        Assertions.assertEquals(RollGradation.FAILURE, eventResult.getPreviousGradation());
        Assertions.assertEquals(RollGradation.REGULAR, eventResult.getAchievedGradation());
        Assertions.assertEquals("accounting", eventResult.getTargetSkill());
        Assertions.assertEquals("Alice", eventResult.getInvestigatorName());
    }

    @Test
    public void pushTestPrevSuccessNowFail() throws Exception {
        when(generatorService.rollDice(any())).thenReturn(40).thenReturn(50);

        Investigator investigator = Investigator.builder().name("Alice").build();
        investigatorService.addSkill(investigator, "accounting", 5, "base", 40);

        investigatorService.saveInvestigator(investigator);
        List<Investigator> list = List.of(investigator);
        diceRollingService.rollTestsAgainstTargetValue(100, list, "accounting", RollGradation.REGULAR, 0, false, false);

        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().size());
        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().get("accounting"));

        EventPushResult eventResult = diceRollingService.rollPushTest(investigator, RollGradation.REGULAR, RollGradation.REGULAR, "accounting");
        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().size());
        Assertions.assertEquals(0, investigator.getSuccessfullyUsedSkills().get("accounting"));
        Assertions.assertEquals(50, eventResult.getRoll());
        Assertions.assertEquals(RollGradation.REGULAR, eventResult.getPreviousGradation());
        Assertions.assertEquals(RollGradation.FAILURE, eventResult.getAchievedGradation());
        Assertions.assertEquals("accounting", eventResult.getTargetSkill());
        Assertions.assertEquals("Alice", eventResult.getInvestigatorName());
    }
}
