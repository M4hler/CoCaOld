package com.cthulhu.repositories;

import com.cthulhu.models.Investigator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InvestigatorRepositoryTest {
    @Autowired
    private InvestigatorRepository investigatorRepository;

    @Test
    public void findInvestigatorByNameResolvingCorrectly() {
        Investigator investigator1 = Investigator.builder().name("Bob").build();
        investigatorRepository.save(investigator1);
        Investigator investigator2 = Investigator.builder().name("Eve").build();
        investigatorRepository.save(investigator2);

        Assertions.assertEquals("Bob", investigatorRepository.findInvestigatorByName("Bob").getName());
        Assertions.assertEquals("Eve", investigatorRepository.findInvestigatorByName("Eve").getName());
    }
}
