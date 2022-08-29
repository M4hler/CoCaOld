package com.cthulhu.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Skill {
    @Id
    private String skillName;
    @OneToMany(mappedBy = "skill")
    private List<InvestigatorToSkill> investigators;
    private int baseValue;
    private String tag;
}
