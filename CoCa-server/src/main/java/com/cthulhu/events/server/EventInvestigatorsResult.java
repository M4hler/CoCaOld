package com.cthulhu.events.server;

import com.cthulhu.events.Event;
import com.cthulhu.models.Investigator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventInvestigatorsResult extends Event {
    List<Investigator> investigatorList;
}
