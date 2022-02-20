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
public class EventUseLuck extends Event {
    private String investigatorName;
    private int result;
    private RollGradation gradation;
    private String targetSkill;
}
