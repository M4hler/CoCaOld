package com.cthulhu.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Skill {
    @Id
    private String skillName;
    @ManyToMany(mappedBy = "skills")
    private Set<Investigator> investigators;
    private int skillValue;
    private String tag;
}
