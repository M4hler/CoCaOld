package com.cthulhu.services;

import com.cthulhu.models.Investigator;
import com.cthulhu.repositories.InvestigatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Investigator saveInvestigator(Investigator investigator) {
        return investigatorRepository.save(investigator);
    }

    public void deleteAll() {
        investigatorRepository.deleteAll();
    }
}
