package io.swagger.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.threeten.bp.OffsetDateTime;

import javax.persistence.*;

@Entity
//@NoArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class dbTransaction {
    @Id
    @GeneratedValue
    @Column(name="ID" , updatable = false, nullable = false)
    private long id;
    private String userPerform;
    private String IBANfrom;
    private String IBANto;
    private Double amount;
    private OffsetDateTime time;
    private OffsetDateTime dateFrom;
    private OffsetDateTime dateTo;

    public dbTransaction() {

    }
}
