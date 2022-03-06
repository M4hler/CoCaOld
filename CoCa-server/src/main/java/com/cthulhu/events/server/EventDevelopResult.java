package com.cthulhu.events.server;

import com.cthulhu.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDevelopResult extends Event {
    private String investigatorName;
    private String targetSkill;
    private int rollResult;
    private int skillGain;
}
