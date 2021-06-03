package io.swagger.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class dbTransaction {
    @Id
    @GeneratedValue
    private long ID;
    private String IBAN;

    public dbTransaction(long ID,String IBAN) {
        this.IBAN = IBAN;
        this.ID = ID;
    }

}
