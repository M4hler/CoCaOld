package com.cthulhu.repositories;

import com.cthulhu.models.Investigator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestigatorRepository extends JpaRepository<Investigator, Long> {
    Investigator findInvestigatorByName(String name);
}
