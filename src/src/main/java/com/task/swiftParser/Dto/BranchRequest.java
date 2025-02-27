package com.task.swiftParser.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "address", "bankName", "countryISO2", "isHeadquarter", "swiftCode"})
public class BranchRequest {
    private String address;
    private String bankName;
    private String countryISO2;

    @JsonProperty("isHeadquarter")
    private boolean isHeadquarter;

    private String swiftCode;
}
