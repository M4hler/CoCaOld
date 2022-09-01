package com.cthulhu.models;

import lombok.*;
import org.apache.commons.beanutils.PropertyUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Investigator {
    @Id
    private String name;
    private String owner;

    private int strength;
    private int constitution;
    private int size;
    private int dexterity;
    private int appearance;
    private int intelligence;
    private int power;
    private int education;

    private int sanity;
    private int luck;
    private int hitPoints;
    private int magicPoints;
    private int build;
    private String damageBonus;
    private int movement;

    @OneToMany(mappedBy = "investigator", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<InvestigatorToSkill> skills;

    @Transient
    private Map<String, Integer> successfullyUsedSkills;
    private static List<String> cantBeDeveloped = setCantBeDeveloped();

    public int getFieldValueByName(String name) throws Exception {
        try {
            return (int)PropertyUtils.getProperty(this, name);
        }
        catch(NoSuchMethodException e) {
            InvestigatorToSkill skill =
                    skills.stream().filter(x -> x.getSkill().getSkillName().equals(name)).findFirst().orElse(null);
            if(skill != null) {
                return skill.getSkillValue();
            }
            else {
                return 0;
            }
        }
    }

    public void setFieldValueByName(String name, int value) throws Exception {
        try {
            PropertyUtils.setProperty(this, name, value);
        }
        catch(NoSuchMethodException e) {
            skills.stream().filter(x -> x.getSkill().getSkillName().equals(name)).findFirst()
                    .ifPresent(skill -> skill.setSkillValue(value));
        }
    }

    public boolean ableToDevelop(String skill) {
        return !cantBeDeveloped.contains(skill);
    }

    private static List<String> setCantBeDeveloped() {
        return List.of("strength", "constitution", "size", "dexterity", "appearance", "intelligence", "power", "education",
                "sanity", "credit rating", "cthulhu mythos");
    }
}
