package com.sidenis.dealtracker.service;

import com.sidenis.dealtracker.model.Counterparty;
import com.sidenis.dealtracker.model.Deal;
import com.sidenis.dealtracker.model.DealVersion;
import com.sidenis.dealtracker.repository.DealVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class DealVersionService {

    private final DealVersionRepository dealVersionRepository;

    public DealVersion createDealVersion(Deal deal, Counterparty counterparty, String dealVersionName, Timestamp versionCreatedAt) {
        DealVersion dealVersion = new DealVersion();
        dealVersion.setDeal(deal);
        dealVersion.setCounterparty(counterparty);
        dealVersion.setName(dealVersionName);
        dealVersion.setCreatedAt(versionCreatedAt);
        return dealVersionRepository.save(dealVersion);
    }

    public List<DealVersion> getDealVersions(Deal deal) {
        return dealVersionRepository.getAllByDeal(deal);
    }

    public Set<DealVersion> getAllDealVersions() {
        return StreamSupport.stream(dealVersionRepository.findAll().spliterator(), false).collect(Collectors.toSet());
    }

    public DealVersion getDealVersion(Deal deal, String versionName) {
        return dealVersionRepository.getDealVersionByDealAndName(deal, versionName);
    }

    public Optional<DealVersion> getSubsequentDealVersion(DealVersion currentDealVersion) {
        return dealVersionRepository.findFirstByDealAndCreatedAtAfterOrderByCreatedAtAsc(currentDealVersion.getDeal(), currentDealVersion.getCreatedAt());
    }

}
