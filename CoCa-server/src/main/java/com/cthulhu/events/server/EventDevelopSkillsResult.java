package com.cthulhu.events.server;

import com.cthulhu.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDevelopSkillsResult extends Event {
    private String investigatorName;
    private Map<String, Integer> skillsToDevelop;
}
