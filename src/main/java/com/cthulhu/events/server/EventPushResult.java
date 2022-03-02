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
public class EventPushResult extends Event {
    private String investigatorName;
    private int roll;
    private RollGradation previousGradation;
    private RollGradation achievedGradation;
    private String targetSkill;
}
