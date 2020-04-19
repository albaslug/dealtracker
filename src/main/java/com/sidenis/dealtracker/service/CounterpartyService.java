package com.sidenis.dealtracker.service;

import com.sidenis.dealtracker.model.Counterparty;
import com.sidenis.dealtracker.repository.CounterpartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounterpartyService {

    private final CounterpartyRepository counterpartyRepository;

    public Counterparty createCounterparty(String name) {
        Counterparty counterparty = new Counterparty();
        counterparty.setName(name);
        return counterpartyRepository.save(counterparty);
    }

    public Counterparty getCounterparty(String name) {
        return counterpartyRepository.getByName(name);
    }

}
