package com.cthulhu.events.server;

import com.cthulhu.enums.RollGradation;
import com.cthulhu.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventUseLuckResult extends Event {
    private String investigatorName;
    private int luckUsed;
    private RollGradation achievedGradation;
    private String targetSkill;
}
