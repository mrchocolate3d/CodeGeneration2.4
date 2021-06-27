package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class InsertAccount {
    @JsonProperty("userId")
    private Long userId;
    @JsonProperty("accountType")
    private AccountType accountType;



}
