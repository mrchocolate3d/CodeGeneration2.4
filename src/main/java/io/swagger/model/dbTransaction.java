package io.swagger.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.threeten.bp.OffsetDateTime;

import javax.persistence.*;

@Entity
//@Table(name="dbtransaction")
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class dbTransaction {
    @Id
    @GeneratedValue
    @Column(name="ID" , updatable = false, nullable = false)
    private long ID;
//    @OneToMany
//    @JsonBackReference
//    private dbAccount account;
    private String IBAN;
    private OffsetDateTime fromDate;
    private OffsetDateTime toDate;
    private Integer transactionLimit;

    public dbTransaction(String IBAN, OffsetDateTime fromDate, OffsetDateTime toDate, Integer transactionLimit) {
        this.IBAN = IBAN;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.transactionLimit = transactionLimit;
    }
}
