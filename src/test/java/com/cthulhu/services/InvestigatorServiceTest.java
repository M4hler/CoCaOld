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
}
