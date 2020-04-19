package com.sidenis.dealtracker.controller;

import com.sidenis.dealtracker.model.Counterparty;
import com.sidenis.dealtracker.model.Deal;
import com.sidenis.dealtracker.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.SortedMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class HistoryController {

    private final DealHistoryService dealHistoryService;
    private final DealService dealService;
    private final DealVersionService dealVersionService;
    private final CounterpartyService counterpartyService;
    private final CounterpartyReplacementEventSerivce counterpartyReplacementEventSerivce;

    @GetMapping("/initWithSampleData")
    @ResponseBody
    public void initSample() {

        Counterparty c1 = counterpartyService.createCounterparty("c1");
        Counterparty c2 = counterpartyService.createCounterparty("c2");
        Deal d = dealService.createDeal("dealV0", c1, new Timestamp(System.currentTimeMillis()));

        dealVersionService.createDealVersion(d, c1, "dealV1", new Timestamp(System.currentTimeMillis()));
        counterpartyReplacementEventSerivce.createEvent(c1, c2, new Timestamp(System.currentTimeMillis()));

    }

    @GetMapping("/history/{dealName}")
    public SortedMap<Timestamp, Counterparty> getDealHistory(@PathVariable String dealName) {
        return dealHistoryService.getCounterpartyHistoryForDealName(dealName);
    }

    @GetMapping("/history/{dealName}/{dealVersionName}")
    public SortedMap<Timestamp, Counterparty> getDealVersionHistory(@PathVariable String dealName, @PathVariable String dealVersionName) {
        return dealHistoryService.getCounterpartyHistoryForDealVersion(dealName, dealVersionName);
    }

}
