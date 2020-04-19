package com.sidenis.dealtracker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
public class DealVersion extends AbstractPersistentObject {

    @Column(nullable = false)
    private String name;

    @JsonBackReference
    @ManyToOne(optional = false)
    private Deal deal;

    @ManyToOne(optional = false)
    private Counterparty counterparty;

    @Column(nullable = false)
    private Timestamp createdAt;
}
