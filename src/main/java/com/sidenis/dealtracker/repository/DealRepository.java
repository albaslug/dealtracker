package com.sidenis.dealtracker.repository;

import com.sidenis.dealtracker.model.Deal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealRepository extends CrudRepository<Deal, String> {
    Deal findByName(String name);
}
