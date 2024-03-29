package com.cthulhu.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvestigatorToSkill {
    @EmbeddedId
    private InvestigatorToSkillId id;
    @ManyToOne
    @JoinColumn(name = "investigator_name")
    @MapsId("investigator")
    private Investigator investigator;
    @ManyToOne(cascade = CascadeType.MERGE)
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
