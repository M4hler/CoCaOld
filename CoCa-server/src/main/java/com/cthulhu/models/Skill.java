package com.cthulhu.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Skill {
    @Id
    private String skillName;
    @OneToMany(mappedBy = "skill") //cascade = CascadeType.REMOVE
    private List<InvestigatorToSkill> investigators;
    private int baseValue;
    private String tag;
}
