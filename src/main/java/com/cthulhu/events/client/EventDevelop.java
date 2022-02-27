package com.cthulhu.events.client;

import com.cthulhu.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDevelop extends Event {
    private String investigatorName;
    private String targetSkill;
}
