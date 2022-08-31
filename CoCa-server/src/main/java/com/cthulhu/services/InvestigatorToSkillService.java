package com.cthulhu.services;

import com.cthulhu.models.InvestigatorToSkill;
import com.cthulhu.repositories.InvestigatorToSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestigatorToSkillService {
    @Autowired
    private InvestigatorToSkillRepository investigatorToSkillRepository;

    public List<InvestigatorToSkill> getAllInvestigatorsToSkills() {
        return investigatorToSkillRepository.findAll();
    }

    public void deleteAll() {
        investigatorToSkillRepository.deleteAll();
    }
}
