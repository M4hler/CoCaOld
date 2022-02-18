package com.cthulhu.services;

import com.cthulhu.enums.RollGradation;
import com.cthulhu.events.EventRollResult;
import com.cthulhu.models.Investigator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DiceRollingServiceTest {
    @MockBean
    private DiceRollingService diceRollingService;

    @BeforeEach
    public void beforeEach() throws Exception {
        when(diceRollingService.rollTestsAgainstTargetValue(any(), any(), any(), any())).thenCallRealMethod();
    }

    @Test
    public void regularSuccess() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(40);

        Investigator investigator = Investigator.builder().name("Alice").strength(50).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.REGULAR, result.getGraduation());
        Assertions.assertEquals(40, result.getValue());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void hardSuccess() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(25);

        Investigator investigator = Investigator.builder().name("Alice").strength(50).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.HARD, result.getGraduation());
        Assertions.assertEquals(25, result.getValue());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void extremeSuccess() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(10);

        Investigator investigator = Investigator.builder().name("Alice").strength(50).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.EXTREME, result.getGraduation());
        Assertions.assertEquals(10, result.getValue());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void criticalSuccess() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(1);

        Investigator investigator = Investigator.builder().name("Alice").strength(50).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.CRITICAL, result.getGraduation());
        Assertions.assertEquals(1, result.getValue());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void criticalSuccessButExtremeDifficulty() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(1);

        Investigator investigator = Investigator.builder().name("Alice").strength(4).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.EXTREME);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.CRITICAL, result.getGraduation());
        Assertions.assertEquals(1, result.getValue());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void regularSuccessAtLowValue() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(2);

        Investigator investigator = Investigator.builder().name("Alice").strength(3).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.REGULAR, result.getGraduation());
        Assertions.assertEquals(2, result.getValue());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void failure() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(60);

        Investigator investigator = Investigator.builder().name("Alice").strength(50).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FAILURE, result.getGraduation());
        Assertions.assertEquals(60, result.getValue());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void failureWhenRegularSuccessButHardDifficulty() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(40);

        Investigator investigator = Investigator.builder().name("Alice").strength(60).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.HARD);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FAILURE, result.getGraduation());
        Assertions.assertEquals(40, result.getValue());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void failureWhenRegularSuccessButExtremeDifficulty() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(40);

        Investigator investigator = Investigator.builder().name("Alice").strength(60).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.EXTREME);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FAILURE, result.getGraduation());
        Assertions.assertEquals(40, result.getValue());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void failureWhenHardSuccessButExtremeDifficulty() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(20);

        Investigator investigator = Investigator.builder().name("Alice").strength(60).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.EXTREME);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FAILURE, result.getGraduation());
        Assertions.assertEquals(20, result.getValue());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void fumbleWhenSkillGreaterOrEqualTo50() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(100);

        Investigator investigator = Investigator.builder().name("Alice").strength(50).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FUMBLE, result.getGraduation());
        Assertions.assertEquals(100, result.getValue());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void fumbleWhenSkillLowerThan50() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(96);

        Investigator investigator = Investigator.builder().name("Alice").strength(49).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.REGULAR);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FUMBLE, result.getGraduation());
        Assertions.assertEquals(96, result.getValue());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }

    @Test
    public void fumbleWhenSkillGreaterThan50ButHardDifficulty() throws Exception {
        when(diceRollingService.rollDice(any())).thenReturn(98);

        Investigator investigator = Investigator.builder().name("Alice").strength(60).build();
        List<Investigator> list = List.of(investigator);
        List<EventRollResult> resultList = diceRollingService.rollTestsAgainstTargetValue(100, list, "strength", RollGradation.HARD);

        Assertions.assertEquals(1, resultList.size());
        EventRollResult result = resultList.get(0);
        Assertions.assertEquals(RollGradation.FUMBLE, result.getGraduation());
        Assertions.assertEquals(98, result.getValue());
        Assertions.assertEquals("Alice", result.getInvestigatorName());
    }
}
