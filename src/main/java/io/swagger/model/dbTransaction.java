package io.swagger.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.threeten.bp.OffsetDateTime;

import javax.persistence.*;

@Entity
@Table(name="dbtransaction")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class dbTransaction {
    @Id
    @GeneratedValue
    @Column(name="id" , updatable = false, nullable = false)
    private long ID;

    private String IBAN;
    private OffsetDateTime fromDate;
    private OffsetDateTime toDate;
    private Integer limit;

    public dbTransaction(String IBAN, OffsetDateTime fromDate, OffsetDateTime toDate, Integer limit) {
        this.IBAN = IBAN;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.limit = limit;
    }
}
