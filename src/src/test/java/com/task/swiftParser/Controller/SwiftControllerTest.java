package com.task.swiftParser.Controller;
import com.task.swiftParser.Dto.CountryRequest;
import com.task.swiftParser.Dto.HQRequest;
import com.task.swiftParser.Model.SwiftData;
import com.task.swiftParser.Service.SwiftService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
public class SwiftControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private SwiftController swiftController;

    @Mock
    private SwiftService swiftService;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(swiftController).build();
    }

    @Test
    public void testGetSwiftDetails_Success() throws Exception {
        String swiftCode = "TESTUS33";
        HQRequest mockResponse = new HQRequest();
        mockResponse.setSwiftCode(swiftCode);

        when(swiftService.getSwiftDetails(swiftCode)).thenReturn(mockResponse);

        mockMvc.perform(get("/v1/swift-codes/{swiftCode}", swiftCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value(swiftCode));
    }

    @Test
    public void testGetSwiftDetails_SwiftNotFound() throws Exception {
        String swiftCode = "INVALIDSWIFT";
        String errorMessage = "Swift code not found: " + swiftCode;
        when(swiftService.getSwiftDetails(swiftCode)).thenReturn(null);

        mockMvc.perform(get("/v1/swift-codes/{swiftCode}", swiftCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(errorMessage));
    }

    @Test
    public void testGetCountryDetails_Success() throws Exception {
        String countryISO = "PL";
        CountryRequest mockCountryResponse = new CountryRequest();
        mockCountryResponse.setCountryISO2(countryISO);
        mockCountryResponse.setCountryName("Poland");

        when(swiftService.getByCountryCode(countryISO)).thenReturn(mockCountryResponse);

        mockMvc.perform(get("/v1/swift-codes/country/{countryISO2code}", countryISO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryISO2").value(countryISO))
                .andExpect(jsonPath("$.countryName").value("Poland"));
    }

    @Test
    public void testGetCountryDetails_CountryNotFound() throws Exception {
        String countryISO = "XX";
        String errorMessage = "Country ISO not found: " + countryISO;

        when(swiftService.getByCountryCode(countryISO)).thenReturn(null);
        mockMvc.perform(get("/v1/swift-codes/country/{countryISO2code}", countryISO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(errorMessage));
    }

    @Test
    public void testAddSwiftCode_Success() throws Exception {
        SwiftData swiftData = new SwiftData();
        swiftData.setSwiftCode("ALBPPLP1XXX");
        swiftData.setCountryISO2("PL");
        swiftData.setCountryName("Poland");

        when(swiftService.addSwiftCode(swiftData)).thenReturn(Map.of("message", "Swift code added successfully"));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"swiftCode\": \"ALBPPLP1XXX\", \"countryISO2\": \"PL\", \"countryName\": \"Poland\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Swift code added successfully"));
    }

    @Test
    public void testAddSwiftCode_Failure() throws Exception {
        SwiftData swiftData = new SwiftData();
        swiftData.setSwiftCode("ALBPPLP1XXX");
        swiftData.setCountryISO2("PL");
        swiftData.setCountryName("Poland");

        when(swiftService.addSwiftCode(swiftData)).thenReturn(Map.of("message", "Swift code already exists in database"));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"swiftCode\": \"ALBPPLP1XXX\", \"countryISO2\": \"PL\", \"countryName\": \"Poland\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Swift code already exists in database"));
    }


    @Test
    public void testDeleteSwiftCode_Success() throws Exception {
        String swiftCode = "ALBPPLP1XXX";

        when(swiftService.deleteSwiftCode(swiftCode)).thenReturn(Map.of("message", "Swift Code deleted successfully"));

        mockMvc.perform(delete("/v1/swift-codes/{swiftCode}", swiftCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Swift Code deleted successfully"));
    }

    @Test
    public void testDeleteSwiftCode_Failure() throws Exception {
        String swiftCode = "ALBPPLP1XXX";

        when(swiftService.deleteSwiftCode(swiftCode)).thenReturn(Map.of("message", "Swift code not found: "+swiftCode));

        mockMvc.perform(delete("/v1/swift-codes/{swiftCode}", swiftCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Swift code not found: "+swiftCode));
    }

}