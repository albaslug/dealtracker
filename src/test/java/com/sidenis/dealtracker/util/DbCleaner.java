package com.sidenis.dealtracker.util;

import com.sidenis.dealtracker.repository.CounterpartyReplacementEventRepository;
import com.sidenis.dealtracker.repository.CounterpartyRepository;
import com.sidenis.dealtracker.repository.DealRepository;
import com.sidenis.dealtracker.repository.DealVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DbCleaner {

    private final CounterpartyReplacementEventRepository counterpartyReplacementEventRepository;
    private final CounterpartyRepository counterpartyRepository;
    private final DealRepository dealRepository;
    private final DealVersionRepository dealVersionRepository;

    public void clearAll() {
        counterpartyReplacementEventRepository.deleteAll();
        dealVersionRepository.deleteAll();
        counterpartyRepository.deleteAll();
        dealRepository.deleteAll();
    }


}
