package com.sidenis.dealtracker.service;

import com.sidenis.dealtracker.model.Counterparty;
import com.sidenis.dealtracker.model.CounterpartyReplacementEvent;
import com.sidenis.dealtracker.model.Deal;
import com.sidenis.dealtracker.util.DbCleaner;
import com.sidenis.dealtracker.util.TimeUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DealHistoryServiceTest {

    @Autowired
    private CounterpartyReplacementEventSerivce counterpartyReplacementEventSerivce;

    @Autowired
    private CounterpartyService counterpartyService;

    @Autowired
    private DealService dealService;

    @Autowired
    private DealHistoryService dealHistoryService;

    @Autowired
    private DealVersionService dealVersionService;

    @Autowired
    private DbCleaner dbCleaner;

    @AfterEach
    public void cleanDB() {
        dbCleaner.clearAll();
    }


    @Test
    public void getHistoryTest_ifDealJustCreated_thenHistoryIsEmpty() {
        // given
        Counterparty partner1 = counterpartyService.createCounterparty("Partner1");
        String dealName = "Deal1";

        // when
        Deal d1 = dealService.createDeal(dealName, partner1, TimeUtil.now());

        // then
        SortedMap<Timestamp, Counterparty> ownershipHistory = dealHistoryService.getCounterpartyHistoryForDealName(dealName);
        assertTrue(ownershipHistory.isEmpty());
    }

    @Test
    public void getHistoryTest_ifPartnerChangedBeforeDealCreated_thenHistoryIsEmpty() {
        /*
        events emulated:
        - partner1 globally replaced by partner2
        - dealV1 created for partner1
         */


        // given
        Counterparty partner1 = counterpartyService.createCounterparty("Partner1");
        Counterparty partner2 = counterpartyService.createCounterparty("Partner2");
        String dealName = "Deal1";

        // when
        CounterpartyReplacementEvent replacement = counterpartyReplacementEventSerivce.createEvent(partner1, partner2, TimeUtil.toTimestamp("2010-01-01"));
        dealService.createDeal(dealName, partner1, TimeUtil.toTimestamp("2020-01-01"));

        // then
        SortedMap<Timestamp, Counterparty> ownershipHistory = dealHistoryService.getCounterpartyHistoryForDealName(dealName);
        assertTrue(ownershipHistory.isEmpty());
    }

    @Test
    public void getHistoryTest_ifPartnerChangedAfterDealCreated_thenHistoryContains() {
        /*
        events emulated:
        - dealV1 created for partner1
        - partner1 globally replaced by partner2
         */

        // given
        Counterparty partner1 = counterpartyService.createCounterparty("Partner1");
        Counterparty partner2 = counterpartyService.createCounterparty("Partner2");
        String dealName = "Dea1";

        // when
        Timestamp replacementDate = TimeUtil.toTimestamp("2022-01-01");
        counterpartyReplacementEventSerivce.createEvent(partner1, partner2, replacementDate);
        dealService.createDeal(dealName, partner1, TimeUtil.toTimestamp("2020-01-01"));

        // then
        SortedMap<Timestamp, Counterparty> ownershipHistory = dealHistoryService.getCounterpartyHistoryForDealName(dealName);
        assertEquals(1, ownershipHistory.size());
        assertTrue(ownershipHistory.containsKey(replacementDate));
        assertEquals(partner2, ownershipHistory.get(replacementDate));
    }

    @Test
    public void getHistoryTest_complex() {
        /*
        events emulated:
        - dealV1 created for partner1
        - partner1 globally replaced by partner2
        - partner2 globally replaced by partner3
        - dealV2 created for partner3
        - partner3 globally replaced by partner4
         */

        // given
        Counterparty partner1 = counterpartyService.createCounterparty("Partner1");
        Counterparty partner2 = counterpartyService.createCounterparty("Partner2");
        Counterparty partner3 = counterpartyService.createCounterparty("Partner3");
        Counterparty partner4 = counterpartyService.createCounterparty("Partner4");

        // when
        Deal deal = dealService.createDeal("Deal", partner1, TimeUtil.toTimestamp("2020-01-01"));
        counterpartyReplacementEventSerivce.createEvent(partner1, partner2, TimeUtil.toTimestamp("2021-01-01"));
        counterpartyReplacementEventSerivce.createEvent(partner2, partner3, TimeUtil.toTimestamp("2022-01-01"));
        dealVersionService.createDealVersion(deal, partner3, "DealV2", TimeUtil.toTimestamp("2023-01-01"));
        counterpartyReplacementEventSerivce.createEvent(partner3, partner4, TimeUtil.toTimestamp("2024-01-01"));

        // then
        SortedMap<Timestamp, Counterparty> ownershipHistory1 = dealHistoryService.getCounterpartyHistoryForDealVersion("Deal", "Deal");
        assertEquals(2, ownershipHistory1.size());
        assertEquals(partner2, ownershipHistory1.get(TimeUtil.toTimestamp("2021-01-01")));
        assertEquals(partner3, ownershipHistory1.get(TimeUtil.toTimestamp("2022-01-01")));

        SortedMap<Timestamp, Counterparty> ownershipHistory2 = dealHistoryService.getCounterpartyHistoryForDealVersion("Deal", "DealV2");
        assertEquals(1, ownershipHistory2.size());
        assertEquals(partner4, ownershipHistory2.get(TimeUtil.toTimestamp("2024-01-01")));
    }

}