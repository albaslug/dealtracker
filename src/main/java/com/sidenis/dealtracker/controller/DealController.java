package com.sidenis.dealtracker.controller;

import com.sidenis.dealtracker.model.Deal;
import com.sidenis.dealtracker.model.DealVersion;
import com.sidenis.dealtracker.service.DealService;
import com.sidenis.dealtracker.service.DealVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deals")
public class DealController {

    private final DealService dealService;

    private final DealVersionService dealVersionService;

    @GetMapping("/all")
    public Set<Deal> getAllDeals() {
        return dealService.getAllDeals();
    }

    @GetMapping("/{dealName}")
    public Deal getDeal(@PathVariable String dealName) {
        return dealService.getDeal(dealName);
    }

    @GetMapping("/versions/all")
    public Set<DealVersion> getAllDealVersions() {
        return dealVersionService.getAllDealVersions();
    }

    @GetMapping("/{dealName}/{dealVersionName}")
    public DealVersion getDealVersion(@PathVariable String dealName, @PathVariable String dealVersionName) {
        Deal deal = dealService.getDeal(dealName);
        return dealVersionService.getDealVersion(deal, dealVersionName);
    }


}
