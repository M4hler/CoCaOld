package com.cthulhu.models;

import lombok.*;
import org.apache.commons.beanutils.PropertyUtils;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Investigator {
    private String owner;
    private String name;

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
