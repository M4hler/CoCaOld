package com.cthulhu.events.client;

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
public class EventPush extends Event {
    private String investigatorName;
    private RollGradation previousGradation;
    private RollGradation currentGradation;
    private String targetSkill;
}
