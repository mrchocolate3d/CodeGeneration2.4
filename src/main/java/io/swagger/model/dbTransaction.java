package io.swagger.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.threeten.bp.OffsetDateTime;

import javax.persistence.*;

@Entity

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class dbTransaction {
    @Id
    @GeneratedValue
    @Column(name="ID" , updatable = false, nullable = false)
    private long id;
    private int tLimit;
    private OffsetDateTime fromDate;
    private OffsetDateTime toDate;
    private String IBAN;


    public dbTransaction(int tLimit, OffsetDateTime fromDate, OffsetDateTime toDate, String IBAN) {
        this.tLimit = tLimit;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.IBAN = IBAN;
    }
}
