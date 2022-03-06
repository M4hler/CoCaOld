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
public class EventRollResult extends Event {
    private String investigatorName;
    private int result;
    private RollGradation gradation;
    private String targetSkill;
    private boolean allowLuck;
    private boolean allowPush;
}
