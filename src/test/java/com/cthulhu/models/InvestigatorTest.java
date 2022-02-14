package com.cthulhu.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InvestigatorTest {
    @Test
    public void getClassPropertyByName() throws Exception {
        Investigator investigator = new Investigator();
        investigator.setStrength(70);
        investigator.setConstitution(50);

        Assertions.assertEquals(70, investigator.getFieldValueByName("strength"));
        Assertions.assertEquals(50, investigator.getFieldValueByName("constitution"));
    }
}
