package com.cthulhu.models;

import lombok.*;
import org.apache.commons.beanutils.PropertyUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
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

    private int luck;

    private int accounting;

    @Transient
    private Map<String, Integer> successfullyUsedSkills;
    private static List<String> cantBeDeveloped = setCantBeDeveloped();

    public int getFieldValueByName(String name) throws Exception {
        return (int)PropertyUtils.getProperty(this, name);
    }

    public void setFieldValueByName(String name, int value) throws Exception {
        PropertyUtils.setProperty(this, name, value);
    }

    public boolean ableToDevelop(String skill) {
        return !cantBeDeveloped.contains(skill);
    }

    private static List<String> setCantBeDeveloped() {
        return List.of("strength", "constitution", "size", "dexterity", "appearance", "intelligence", "power", "education",
                "sanity", "creditRating", "cthulhuMythos");
    }
}
