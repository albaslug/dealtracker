package com.sidenis.dealtracker.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString
public class Counterparty extends AbstractPersistentObject {

    @Column(nullable = false, unique = true)
    private String name;

}
