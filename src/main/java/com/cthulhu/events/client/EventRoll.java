package com.cthulhu.events.client;

import com.cthulhu.enums.RollGradation;
import com.cthulhu.events.Event;
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
    private Integer bonusDice;
}
