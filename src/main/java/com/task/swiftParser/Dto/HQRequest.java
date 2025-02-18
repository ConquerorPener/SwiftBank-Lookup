package com.task.swiftParser.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "address", "bankName", "countryISO2", "countryName", "isHeadquarter", "swiftCode", "branches" })
public class HQRequest {
    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;

    @JsonProperty("isHeadquarter")
    private boolean isHeadquarter;

    private String swiftCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<BranchRequest> branches;
}
