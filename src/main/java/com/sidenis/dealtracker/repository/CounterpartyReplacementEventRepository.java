package com.sidenis.dealtracker.repository;

import com.sidenis.dealtracker.model.CounterpartyReplacementEvent;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface CounterpartyReplacementEventRepository extends CrudRepository<CounterpartyReplacementEvent, String> {

    List<CounterpartyReplacementEvent> getAllByDeprecatedAtBetween(Timestamp start, Timestamp end);

    List<CounterpartyReplacementEvent> getAllByDeprecatedAtAfter(Timestamp start);

}
