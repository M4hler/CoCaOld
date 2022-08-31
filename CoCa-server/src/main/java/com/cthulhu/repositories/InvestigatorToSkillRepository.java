package com.cthulhu.repositories;

import com.cthulhu.models.InvestigatorToSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestigatorToSkillRepository extends JpaRepository<InvestigatorToSkill, Long> {
}
