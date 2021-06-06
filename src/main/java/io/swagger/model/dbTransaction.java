package io.swagger.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.threeten.bp.OffsetDateTime;

import javax.persistence.*;

@Entity

@NoArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class dbTransaction {
    @Id
    @GeneratedValue
    @Column(name="transactionID" , updatable = false, nullable = false)
    private long transactionID;
    private String IBAN;
    private String userPerforming;
    //private String ibanFrom;
    private String ibanTo;
    private double amount;
    private OffsetDateTime time;

    public dbTransaction(long transactionID, String userPerforming, String IBAN, String ibanTo, double amount, OffsetDateTime time) {
        this.transactionID = transactionID;
        this.userPerforming = userPerforming;
        this.IBAN = IBAN;
        this.ibanTo = ibanTo;
        this.amount = amount;
        this.time = time;
    }






}
