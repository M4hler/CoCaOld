package com.cthulhu.services;

import com.cthulhu.models.Investigator;
import com.cthulhu.models.Skill;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SkillServiceTest {
    @Autowired
    private InvestigatorService investigatorService;
    @Autowired
    private InvestigatorToSkillService investigatorToSkillService;
    @Autowired
    private SkillService skillService;

    @BeforeEach
    public void beforeEach() {
        investigatorService.deleteAll();
        investigatorToSkillService.deleteAll();
        skillService.deleteAll();
    }

    @Test
    public void addSkillToInvestigatorAndSaveInSkillTable() {
        List<Skill> skills = skillService.getAllSkills();
        Assertions.assertEquals(0, skills.size());

        Investigator investigator = Investigator.builder().name("John").strength(50).build();
        investigatorService.addSkill(investigator, "diving", 5, "base", 40);
        investigatorService.saveInvestigator(investigator);

        skills = skillService.getAllSkills();
        Assertions.assertEquals(1, skills.size());
    }

    @Test
    public void tryAddingSameSkill() {
        List<Skill> skills = skillService.getAllSkills();
        Assertions.assertEquals(0, skills.size());

        Investigator investigator = Investigator.builder().name("John").strength(50).build();
        investigatorService.addSkill(investigator, "diving", 5, "base", 40);
        investigatorService.saveInvestigator(investigator);
        investigatorService.addSkill(investigator, "diving", 6, "base", 50);
        investigatorService.saveInvestigator(investigator);

        skills = skillService.getAllSkills();
        Assertions.assertEquals(1, skills.size());
    }
}
