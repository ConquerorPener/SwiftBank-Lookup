package com.task.swiftParser.Service;

import com.redislabs.modules.rejson.JReJSON;
import com.redislabs.modules.rejson.Path;
import com.task.swiftParser.Dto.BranchRequest;
import com.task.swiftParser.Dto.CountryRequest;
import com.task.swiftParser.Dto.HQRequest;
import com.task.swiftParser.Exception.CountryNotFoundException;
import com.task.swiftParser.Exception.SwiftNotFoundException;
import com.task.swiftParser.Model.CountryData;
import com.task.swiftParser.Model.SwiftData;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;
@SpringBootTest
public class SwiftServiceTest {

    @InjectMocks
    private SwiftService swiftService;


    @Mock
    private JReJSON redisClient;

    @Mock
    private Jedis jedis;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetSwiftDetails_ExistingSwiftCode() {
        String swiftCode = "ALBPPLP1XXX";
        String branchSwiftCode = "ALBPPLP1BMW";
        HQRequest mockRequest = new HQRequest();
        mockRequest.setSwiftCode(swiftCode);
        mockRequest.setHeadquarter(true);

        when(redisClient.get("swift:" + swiftCode, HQRequest.class)).thenReturn(mockRequest);
        when(redisTemplate.keys("swift:ALBPPLP1*")).thenReturn(Set.of("swift:" + branchSwiftCode));

        BranchRequest branchRequest = new BranchRequest();
        branchRequest.setSwiftCode(branchSwiftCode);
        when(redisClient.mget(eq(BranchRequest.class), any(String[].class))).thenReturn(List.of(branchRequest));
        HQRequest result = swiftService.getSwiftDetails(swiftCode);

        Assert.assertNotNull(result, "The returned value should not be null.");
        Assert.assertTrue(result.isHeadquarter(), "The SWIFT Code should be a headquarter.");
        Assert.assertEquals(result.getSwiftCode(), swiftCode, "The returned SWIFT Code does not match.");
        Assert.assertNotNull(result.getBranches(), "The list of branches should not be null.");
        Assert.assertFalse(result.getBranches().isEmpty(), "The list of branches should not be empty.");
        Assert.assertEquals(result.getBranches().getFirst().getSwiftCode(), branchSwiftCode, "The returned branch has an incorrect SWIFT Code.");

    }

    @Test(expectedExceptions = SwiftNotFoundException.class)
    public void testGetSwiftDetails_SwiftNotFound() {
        String swiftCode = "INVALIDSWIFT";
        when(redisClient.get("swift:" + swiftCode, HQRequest.class)).thenReturn(null);
        swiftService.getSwiftDetails(swiftCode);
    }

    @Test
    public void testGetBranches_ExistingBranches()
    {
        String swiftCode = "ALBPPLP1XXX";
        String branchSwiftCode = "ALBPPLP1BMW";

        when(redisTemplate.keys("swift:ALBPPLP1*")).thenReturn(Set.of("swift:"+branchSwiftCode));

        BranchRequest branchRequest = new BranchRequest();
        branchRequest.setSwiftCode(branchSwiftCode);

        when(redisClient.mget(eq(BranchRequest.class),any(String[].class))).thenReturn(List.of(branchRequest));

        List<BranchRequest> result = swiftService.getBranches(swiftCode);

        Assert.assertNotNull(result,"Branches list cannot be null");
        Assert.assertEquals(result.size(),1,"Should be returned exactly 1 branch");
        Assert.assertEquals(result.getFirst().getSwiftCode(),branchSwiftCode,"Returned Swift code is incorrect");
    }

    @Test
    public void testGetByCountryCode_ExistingCountry() {
        String countryISO = "PL";
        String countryKey = "country:PL:swiftCodes";
        String swiftCode = "ALBPPLP1XXX";

        CountryData mockCountryData = new CountryData();
        mockCountryData.setCountryISO2(countryISO);
        mockCountryData.setCountryName("Poland");
        mockCountryData.setSwiftCodes(List.of(swiftCode));
        when(redisClient.get(countryKey,CountryData.class)).thenReturn(mockCountryData);

        BranchRequest branchRequest = new BranchRequest();
        branchRequest.setSwiftCode(swiftCode);

        when(redisClient.mget(eq(BranchRequest.class), any(String[].class))).thenReturn(List.of(branchRequest));

        CountryRequest result = swiftService.getByCountryCode(countryISO);

        Assert.assertNotNull(result, "The returned CountryRequest should not be null.");
        Assert.assertEquals(result.getCountryISO2(), "PL", "The country code is incorrect.");
        Assert.assertEquals(result.getCountryName(), "Poland", "The country name is incorrect.");
        Assert.assertFalse(result.getSwiftCodes().isEmpty(), "The list of SWIFT codes should not be empty.");

    }

    @Test(expectedExceptions = CountryNotFoundException.class)
    public void testGetByCountryCode_CountryNotFound() {
        String countryISO = "PL";
        String countryKey = "country:PL:swiftCodes";
        when(redisClient.get(countryKey, CountryData.class)).thenReturn(null);
        swiftService.getByCountryCode(countryISO);
    }


    @Test
    public void testAddSwiftCode_NewSwiftCode() {
        SwiftData swiftData = new SwiftData();
        swiftData.setSwiftCode("ALBPPLP1XXX");
        swiftData.setCountryISO2("PL");
        swiftData.setCountryName("Poland");

        when(jedis.exists("swift:ALBPPLP1XXX")).thenReturn(false);
        doNothing().when(redisClient).set(anyString(), any(SwiftData.class));

        Map<String, String> result = swiftService.addSwiftCode(swiftData);
        Assert.assertNotNull(result, "Returned Map cannot be null");
        Assert.assertTrue(result.get("message").contains("Swift code added successfully"), "Success message should appear");
    }

    @Test
    public void testAddSwiftCode_ExistingSwiftCode() {
        SwiftData swiftData = new SwiftData();
        swiftData.setSwiftCode("ALBPPLP1XXX");
        swiftData.setCountryISO2("PL");
        swiftData.setCountryName("Poland");

        when(jedis.exists("swift:ALBPPLP1XXX")).thenReturn(true);
        doNothing().when(redisClient).set(anyString(), any(SwiftData.class));

        Map<String, String> result = swiftService.addSwiftCode(swiftData);
        Assert.assertNotNull(result, "Returned Map cannot be null");
        Assert.assertTrue(result.get("message").contains("Swift code already exists in database"), "Failure message should appear");
    }

    @Test
    public void testAddCountrySwift_NewCountry() {
        SwiftData swiftData = new SwiftData();
        swiftData.setSwiftCode("ALBPPLP1XXX");
        swiftData.setCountryISO2("PL");
        swiftData.setCountryName("Poland");

        String countryKey = "country:PL:swiftCodes";

        when(redisTemplate.keys(countryKey)).thenReturn(Set.of());

        doNothing().when(redisClient).set(anyString(), any(CountryData.class));
        String result = swiftService.addCountrySwift(swiftData);
        Assert.assertEquals(result, "Successfully added Country data.", "The message should indicate that the addition was successful.");
    }

    @Test
    public void testAddCountrySwift_ExistingCountry() {
        SwiftData swiftData = new SwiftData();
        swiftData.setSwiftCode("ALBPPLP1XXX");
        swiftData.setCountryISO2("PL");
        swiftData.setCountryName("Poland");

        when(redisTemplate.keys("country:PL:swiftCodes")).thenReturn(Set.of("country:PL:swiftCodes"));
        when(redisClient.arrAppend(eq("country:PL:swiftCodes"), eq(Path.of(".swiftCodes")), eq("ALBPPLP1XXX"))).thenReturn(1L);
        String result = swiftService.addCountrySwift(swiftData);
        Assert.assertEquals(result, "Country already exists, code appended.", "The message should indicate that the addition was successful.");
    }

    @Test
    public void testDeleteSwiftCode_ExistingSwiftCode() {
        String swiftCode = "ALBPPLP1XXX";
        String key = "swift:" + swiftCode;
        String countryKey = "country:PL:swiftCodes";

        SwiftData swiftData = new SwiftData();
        swiftData.setSwiftCode(swiftCode);
        swiftData.setCountryISO2("PL");

        List<String> swiftCodes = new ArrayList<>();
        swiftCodes.add(swiftCode);
        when(redisClient.get(key, SwiftData.class)).thenReturn(swiftData);
        when(redisClient.get(countryKey, Path.of(".swiftCodes"))).thenReturn(swiftCodes);
        doNothing().when(redisClient).set(eq(countryKey), any(List.class), eq(Path.of(".swiftCodes")));
        when(redisClient.del(key)).thenReturn(1L);
        Map<String, String> result = swiftService.deleteSwiftCode(swiftCode);
        Assert.assertNotNull(result, "Returned Map cannot be null");
        Assert.assertEquals(result.get("message"), "Swift Code deleted successfully", "Message should confirm successful deletion.");
        Assert.assertFalse(swiftCodes.contains(swiftCode), "Swift code should be removed from the list.");
    }
    @Test(expectedExceptions = SwiftNotFoundException.class)
    public void testDeleteSwiftCode_NonExistentSwiftCode() {
        String swiftCode = "INVALIDSWIFT";
        String key = "swift:" + swiftCode;

        when(redisClient.get(key, SwiftData.class)).thenReturn(null);
        swiftService.deleteSwiftCode(swiftCode);
    }



}
