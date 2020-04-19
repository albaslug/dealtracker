package com.sidenis.dealtracker.service;

import com.sidenis.dealtracker.model.Counterparty;
import com.sidenis.dealtracker.model.CounterpartyReplacementEvent;
import com.sidenis.dealtracker.repository.CounterpartyReplacementEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CounterpartyReplacementEventSerivce {

    private final CounterpartyReplacementEventRepository counterpartyReplacementEventRepository;

    public CounterpartyReplacementEvent createEvent(Counterparty deprecated, Counterparty survivor, Timestamp deprecatedAt) {
        CounterpartyReplacementEvent event = new CounterpartyReplacementEvent();
        event.setDeprecatedCounterparty(deprecated);
        event.setSurvivorCounterparty(survivor);
        event.setDeprecatedAt(deprecatedAt);
        return counterpartyReplacementEventRepository.save(event);
    }

    public List<CounterpartyReplacementEvent> getEventsBetween(Timestamp start, Timestamp end) {
        return counterpartyReplacementEventRepository.getAllByDeprecatedAtBetween(start, end);
    }

    public List<CounterpartyReplacementEvent> getEventsAfter(Timestamp start) {
        return counterpartyReplacementEventRepository.getAllByDeprecatedAtAfter(start);
    }

}
