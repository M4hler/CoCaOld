package com.cthulhu.models;

import javax.persistence.*;

@Entity
public class InvestigatorToSkill {
    @EmbeddedId
    private InvestigatorToSkillId id;
    @ManyToOne
    @JoinColumn(name = "investigator_name")
    @MapsId("investigator")
    private Investigator investigator;
    @ManyToOne
    @JoinColumn(name = "skill_name")
    @MapsId("skill")
    private Skill skill;
    private int skillValue;
}
