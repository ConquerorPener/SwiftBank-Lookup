package com.task.swiftParser.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwiftData {
    private String countryISO2;
    private String swiftCode;
    private String bankName;
    private String address;
    private String countryName;
    @JsonProperty("isHeadquarter")
    private boolean isHeadquarter;
}
