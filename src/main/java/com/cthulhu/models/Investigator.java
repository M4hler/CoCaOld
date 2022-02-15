package com.cthulhu.models;

import lombok.*;
import org.apache.commons.beanutils.PropertyUtils;

import javax.persistence.Entity;
import javax.persistence.Id;

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

    public int getFieldValueByName(String name) throws Exception {
        return (int)PropertyUtils.getProperty(this, name);
    }
}
