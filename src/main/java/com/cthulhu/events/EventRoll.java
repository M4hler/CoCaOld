package com.cthulhu.events;

import com.cthulhu.enums.RollGradation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRoll extends Event {
    private Integer die;
    private List<String> investigatorTargets;
    private String targetSkill;
    private RollGradation difficulty;
}
