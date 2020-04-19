package com.sidenis.dealtracker.repository;

import com.sidenis.dealtracker.model.Deal;
import com.sidenis.dealtracker.model.DealVersion;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface DealVersionRepository extends CrudRepository<DealVersion, String> {
    List<DealVersion> getAllByDeal(Deal deal);

    DealVersion getDealVersionByDealAndName(Deal deal, String name);

    Optional<DealVersion> findFirstByDealAndCreatedAtAfterOrderByCreatedAtAsc(Deal deal, Timestamp createdAt);
}
