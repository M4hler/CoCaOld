package com.cthulhu.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    public InvestigatorToSkill(Investigator investigator, Skill skill, int skillValue) {
        id = new InvestigatorToSkillId(investigator.getName(), skill.getSkillName());
        this.investigator = investigator;
        this.skill = skill;
        this.skillValue = skillValue;
    }
}
