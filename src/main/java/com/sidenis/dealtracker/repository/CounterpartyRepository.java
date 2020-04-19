package com.sidenis.dealtracker.repository;

import com.sidenis.dealtracker.model.Counterparty;
import org.springframework.data.repository.CrudRepository;

public interface CounterpartyRepository extends CrudRepository<Counterparty, String> {
    Counterparty getByName(String name);
}
