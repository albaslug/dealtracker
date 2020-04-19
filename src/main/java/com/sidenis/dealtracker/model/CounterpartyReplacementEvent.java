package com.sidenis.dealtracker.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
public class CounterpartyReplacementEvent extends AbstractPersistentObject {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Counterparty deprecatedCounterparty;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Counterparty survivorCounterparty;

    @Column(nullable = false)
    private Timestamp deprecatedAt;


}
