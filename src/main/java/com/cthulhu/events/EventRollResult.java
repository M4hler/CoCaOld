package com.cthulhu.events;

import com.cthulhu.enums.RollGraduation;
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
    private int value;
    private RollGraduation graduation;
}
