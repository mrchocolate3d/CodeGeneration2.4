package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class EditAbsoluteLimit {
    @JsonProperty("Iban")
    private String Iban;
    @JsonProperty("absoluteLimit")
    private Double absoluteLimit;
}
