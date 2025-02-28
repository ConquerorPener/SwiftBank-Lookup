package com.task.swiftParser.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwiftData {
    @NotBlank(message = "Country ISO2 is required")
    @Size(min = 2, max = 2, message = "Country ISO2 must be exactly 2 characters")
    private String countryISO2;

    @NotBlank(message = "Swift code is required")
    @Size(min = 11, max = 11, message = "Swift code must be exactly 11 characters")
    private String swiftCode;

    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Country name is required")
    private String countryName;

    @JsonProperty("isHeadquarter")
    private boolean isHeadquarter;
}
