package com.sidenis.dealtracker.model;

import com.sidenis.dealtracker.model.util.IdentifierGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class AbstractPersistentObject {

    @Id
    private String id = IdentifierGenerator.createId();

    @Version
    private Timestamp changedAt;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractPersistentObject)) {
            return false;
        }
        AbstractPersistentObject other = (AbstractPersistentObject)o;
        return id.equals(other.getId());
    }

    public int hashCode() {
        return id.hashCode();
    }

}