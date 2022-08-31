package com.cthulhu.services;

import com.cthulhu.models.Investigator;
import com.cthulhu.models.InvestigatorToSkill;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class InvestigatorServiceTest {
    @Autowired
    private InvestigatorService investigatorService;

    @BeforeEach
    public void beforeEach() {
        investigatorService.deleteAll();
    }

    @Test
    public void saveSingleInvestigator() {
        Investigator investigator = Investigator.builder().name("John").build();
        investigatorService.saveInvestigator(investigator);

        Assertions.assertEquals(1, investigatorService.getAllInvestigators().size());
    }

    @Test
    public void saveTwoInvestigators() {
        Investigator investigator1 = Investigator.builder().name("John").strength(50).build();
        investigatorService.saveInvestigator(investigator1);
        Investigator investigator2 = Investigator.builder().name("Alice").strength(60).build();
        investigatorService.saveInvestigator(investigator2);

        Assertions.assertEquals(2, investigatorService.getAllInvestigators().size());
        Assertions.assertEquals(50, investigatorService.getInvestigatorByName("John").getStrength());
        Assertions.assertEquals(60, investigatorService.getInvestigatorByName("Alice").getStrength());
    }

    @Test
    public void tryInsertingInvestigatorWithSameName() {
        Investigator investigator1 = Investigator.builder().name("John").strength(50).build();
        investigatorService.saveInvestigator(investigator1);
        Investigator investigator2 = Investigator.builder().name("John").strength(60).build();
        investigatorService.saveInvestigator(investigator2);

        Assertions.assertEquals(1, investigatorService.getAllInvestigators().size());
        Assertions.assertEquals(60, investigatorService.getInvestigatorByName("John").getStrength());
    }

    @Test
    public void tryAddingWrongSkill() {
        Investigator investigator = Investigator.builder().build();
        investigatorService.addToSuccessfullyUsedSkills(investigator, "strength");

        Assertions.assertNull(investigator.getSuccessfullyUsedSkills());
    }

    @Test
    public void addToSuccessfullyUsedSkills() {
        Investigator investigator = Investigator.builder().build();
        Assertions.assertNull(investigator.getSuccessfullyUsedSkills());

        investigatorService.addToSuccessfullyUsedSkills(investigator, "accounting");
        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().size());
        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().get("accounting"));
    }

    @Test
    public void sameSkillUsedSuccessfullyTwice() {
        Investigator investigator = Investigator.builder().build();
        investigatorService.addToSuccessfullyUsedSkills(investigator, "accounting");
        investigatorService.addToSuccessfullyUsedSkills(investigator, "accounting");


        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().size());
        Assertions.assertEquals(2, investigator.getSuccessfullyUsedSkills().get("accounting"));
    }

    @Test
    public void multipleSkillsUsedSuccessfully() {
        Investigator investigator = Investigator.builder().build();
        investigatorService.addToSuccessfullyUsedSkills(investigator, "accounting");
        investigatorService.addToSuccessfullyUsedSkills(investigator, "history");
        investigatorService.addToSuccessfullyUsedSkills(investigator, "accounting");


        Assertions.assertEquals(2, investigator.getSuccessfullyUsedSkills().size());
        Assertions.assertEquals(2, investigator.getSuccessfullyUsedSkills().get("accounting"));
        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().get("history"));
    }

    @Test
    public void reduceNumberOfSuccessfulAttempts() {
        Investigator investigator = Investigator.builder().build();
        investigatorService.addToSuccessfullyUsedSkills(investigator, "accounting");
        investigatorService.addToSuccessfullyUsedSkills(investigator, "accounting");


        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().size());
        Assertions.assertEquals(2, investigator.getSuccessfullyUsedSkills().get("accounting"));

        investigatorService.reduceSuccessfullyUsedSkill(investigator, "accounting");
        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().size());
        Assertions.assertEquals(1, investigator.getSuccessfullyUsedSkills().get("accounting"));
    }

    @Test
    public void createInvestigatorAndCascadeSkills() {
        Investigator investigator = Investigator.builder().name("John").strength(70).build();
        investigatorService.addSkill(investigator, "history", 5, "base", 30);

        investigatorService.saveInvestigator(investigator);

        investigator = investigatorService.getInvestigatorByName("John");
        Assertions.assertEquals(1, investigator.getSkills().size());

        InvestigatorToSkill investigatorSkill = investigator.getSkills().get(0);
        Assertions.assertEquals("history", investigatorSkill.getSkill().getSkillName());
        Assertions.assertEquals(30, investigatorSkill.getSkillValue());
    }

    @Test
    public void createInvestigatorAndCascadeNewSkill() {
        Investigator investigator = Investigator.builder().name("John").strength(70).build();
        investigatorService.addSkill(investigator, "history", 5, "base", 30);
        investigatorService.addSkill(investigator, "diving", 5, "base", 10);

        investigatorService.saveInvestigator(investigator);

        investigator = investigatorService.getInvestigatorByName("John");
        List<InvestigatorToSkill> skills = investigator.getSkills();
        Assertions.assertEquals(2, skills.size());

        InvestigatorToSkill historySkill =
                skills.stream().filter(x -> x.getSkill().getSkillName().equals("history")).findFirst().orElse(null);
        InvestigatorToSkill divingSkill =
                skills.stream().filter(x -> x.getSkill().getSkillName().equals("diving")).findFirst().orElse(null);

        Assertions.assertNotNull(historySkill);
        Assertions.assertEquals(30, historySkill.getSkillValue());
        Assertions.assertEquals(5, historySkill.getSkill().getBaseValue());

        Assertions.assertNotNull(divingSkill);
        Assertions.assertEquals(10, divingSkill.getSkillValue());
        Assertions.assertEquals(5, divingSkill.getSkill().getBaseValue());
    }
}
