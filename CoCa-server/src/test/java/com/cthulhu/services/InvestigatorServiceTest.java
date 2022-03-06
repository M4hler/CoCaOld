package com.cthulhu.services;

import com.cthulhu.models.Investigator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InvestigatorServiceTest {
    @Autowired
    private InvestigatorService investigatorService;

    @Test
    public void saveSingleInvestigator() {
        Investigator investigator = Investigator.builder().name("John").build();
        investigatorService.saveInvestigator(investigator);

        Assertions.assertEquals(1, investigatorService.getAllInvestigators().size());

        investigatorService.deleteAll();
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

        investigatorService.deleteAll();
    }

    @Test
    public void tryInsertingInvestigatorWithSameName() {
        Investigator investigator1 = Investigator.builder().name("John").strength(50).build();
        investigatorService.saveInvestigator(investigator1);
        Investigator investigator2 = Investigator.builder().name("John").strength(60).build();
        investigatorService.saveInvestigator(investigator2);

        Assertions.assertEquals(1, investigatorService.getAllInvestigators().size());
        Assertions.assertEquals(60, investigatorService.getInvestigatorByName("John").getStrength());

        investigatorService.deleteAll();
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
}
