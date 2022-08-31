package com.cthulhu.services;

import com.cthulhu.models.Investigator;
import com.cthulhu.models.InvestigatorToSkill;
import com.cthulhu.models.Skill;
import com.cthulhu.repositories.InvestigatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvestigatorService {
    @Autowired
    private InvestigatorRepository investigatorRepository;

    public List<Investigator> getAllInvestigators() {
        return  investigatorRepository.findAll();
    }

    public Investigator getInvestigatorByName(String name) {
        return investigatorRepository.findInvestigatorByName(name);
    }

    public List<Investigator> getInvestigatorsWithNames(List<String> names) {
        List<Investigator> investigators = getAllInvestigators();
        investigators = investigators.stream().filter(x -> names.contains(x.getName())).toList();
        return investigators;
    }

    public Investigator saveInvestigator(Investigator investigator) {
        return investigatorRepository.save(investigator);
    }

    public void deleteAll() {
        investigatorRepository.deleteAll();
    }

    public void addToSuccessfullyUsedSkills(Investigator investigator, String skill) {
        if(!investigator.ableToDevelop(skill)) {
            return;
        }

        Map<String, Integer> skills = investigator.getSuccessfullyUsedSkills();
        if(skills == null) {
            skills = new HashMap<>();
        }

        skills.merge(skill, 1, Integer::sum);
        investigator.setSuccessfullyUsedSkills(skills);
    }

    public void reduceSuccessfullyUsedSkill(Investigator investigator, String skill) {
        Map<String, Integer> skills = investigator.getSuccessfullyUsedSkills();
        skills.put(skill, skills.get(skill) - 1);
        investigator.setSuccessfullyUsedSkills(skills);
    }

    public void addSkill(Investigator investigator, String skillName, int baseValue, String tag, int skillValue) {
        List<InvestigatorToSkill> skills = investigator.getSkills();
        if(skills == null) {
            skills = new ArrayList<>();
        }

        if(skills.stream().anyMatch(x -> x.getInvestigator().equals(investigator)
                && x.getSkill().getSkillName().equals(skillName))) {
            return;
        }

        Skill skill = new Skill(skillName, baseValue, tag);
        InvestigatorToSkill skillToAdd = new InvestigatorToSkill(investigator, skill, skillValue);
        skills.add(skillToAdd);

        investigator.setSkills(skills);
    }
}
