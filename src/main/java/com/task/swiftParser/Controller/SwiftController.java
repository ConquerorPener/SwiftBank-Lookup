package com.task.swiftParser.Controller;

import com.task.swiftParser.Dto.CountryRequest;
import com.task.swiftParser.Dto.HQRequest;
import com.task.swiftParser.Model.SwiftData;
import com.task.swiftParser.Service.SwiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftController {

    @Autowired
    SwiftService swiftService;

    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> getSwiftDetails(
            @PathVariable("swiftCode") String swiftCode
    )
    {
        return ResponseEntity.ok(swiftService.getSwiftDetails(swiftCode));
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<?> getCountryDetails(
            @PathVariable("countryISO2code") String countryISO2code
    )
    {
        return ResponseEntity.ok(swiftService.getByCountryCode(countryISO2code));
    }

    @PostMapping("")
    public ResponseEntity<String> addSwiftCode(
            @RequestBody SwiftData swiftData
            )
    {
        return ResponseEntity.ok(swiftService.addSwiftCode(swiftData));
    }

    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<String> deleteSwiftCode(
          @PathVariable("swiftCode") String swiftCode
    )
    {
        return  ResponseEntity.ok(swiftService.deleteSwiftCode(swiftCode));
    }

}
