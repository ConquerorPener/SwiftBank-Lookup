package com.task.swiftParser.Dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "countryISO2", "countryName", "swiftCodes"})
public class CountryRequest {
    private String countryISO2;
    private String countryName;
    private List<BranchRequest> swiftCodes;
}
