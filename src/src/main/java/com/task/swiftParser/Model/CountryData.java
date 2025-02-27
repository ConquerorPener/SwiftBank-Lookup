package com.task.swiftParser.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryData {
    private String countryISO2;
    private String countryName;
    private List<String> swiftCodes;
}
