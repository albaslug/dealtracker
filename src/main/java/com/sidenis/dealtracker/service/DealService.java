package com.sidenis.dealtracker.service;

import com.sidenis.dealtracker.model.Counterparty;
import com.sidenis.dealtracker.model.Deal;
import com.sidenis.dealtracker.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;

    private final DealVersionService dealVersionService;
    private final CounterpartyReplacementEventSerivce counterpartyReplacementEventSerivce;

    public Deal createDeal(String dealName, Counterparty counterparty, Timestamp createdAt) {
        // creates a deal entity and its first version pointing to the initial partner
        Deal deal = new Deal();
        deal.setName(dealName);
        deal = dealRepository.save(deal);

        dealVersionService.createDealVersion(deal, counterparty, dealName, createdAt);
        return deal;
    }

    public Deal getDeal(String name) {
        return dealRepository.findByName(name);
    }

    public Set<Deal> getAllDeals() {
        return StreamSupport.stream(dealRepository.findAll().spliterator(), false).collect(Collectors.toSet());
    }




}
