package com.task.swiftParser.Controller;

import com.task.swiftParser.Dto.CountryRequest;
import com.task.swiftParser.Dto.HQRequest;
import com.task.swiftParser.Model.SwiftData;
import com.task.swiftParser.Service.SwiftService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/swift-codes")
@Validated
public class SwiftController {

    @Autowired
    SwiftService swiftService;

    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> getSwiftDetails(
            @PathVariable("swiftCode") String swiftCode
    ) {
        HQRequest result = swiftService.getSwiftDetails(swiftCode);
        if (result == null) {
            String errorMessage = "Swift code not found: " + swiftCode;
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", errorMessage));
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<?> getCountryDetails(
            @PathVariable("countryISO2code") String countryISO2code
    )
    {
        CountryRequest result = swiftService.getByCountryCode(countryISO2code);
        if (result == null) {
            String errorMessage = "Country ISO not found: " + countryISO2code;
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", errorMessage));
        }
        return ResponseEntity.ok(swiftService.getByCountryCode(countryISO2code));
    }

    @PostMapping("")
    public ResponseEntity<Map<String,String>> addSwiftCode(
            @Valid @RequestBody SwiftData swiftData
            )
    {
        Map<String ,String> response = swiftService.addSwiftCode(swiftData);
        if (response.get("message").equals("Swift code already exists in database"))
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<Map<String,String>> deleteSwiftCode(
          @PathVariable("swiftCode") String swiftCode
    )
    {
        Map<String ,String> response = swiftService.deleteSwiftCode(swiftCode);
        if (response.get("message").equals("Swift Code deleted successfully"))
        {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
