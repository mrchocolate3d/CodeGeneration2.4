package io.swagger.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class dbTransaction {
    @Id
    @GeneratedValue
    private long id;


    public dbTransaction(long id) {
        this.id = id;
    }
}
