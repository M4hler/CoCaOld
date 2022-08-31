package com.cthulhu.services;

import com.cthulhu.models.Skill;
import com.cthulhu.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {
    @Autowired
    private SkillRepository skillRepository;

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public void deleteAll() {
        skillRepository.deleteAll();
    }
}
