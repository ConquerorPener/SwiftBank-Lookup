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
import com.task.swiftParser.Util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.*;

@Service
public class SwiftService {

    @Autowired
    JReJSON redisClient;

    @Autowired
    Jedis jedis;


    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public HQRequest getSwiftDetails(String swiftCode) {
        swiftCode = swiftCode.toUpperCase();
        try{
            HQRequest request = redisClient.get("swift:" + swiftCode, HQRequest.class);
            if (request.isHeadquarter()) {
                request.setBranches(getBranches(swiftCode));
            }
            return request;
        }
        catch (Exception e)
        {
            throw new SwiftNotFoundException("Swift code not found: " + swiftCode);
        }


    }

    public List<BranchRequest> getBranches(String swiftCode) {
        String key = swiftCode.substring(0, 8);
        Set<String> branchCodes = redisTemplate.keys("swift:" + key + "*");
        if (branchCodes == null) {
            return new ArrayList<>();
        }
        String[] swiftKeys = branchCodes.toArray(String[]::new);

        return redisClient.mget(BranchRequest.class, swiftKeys)
                .stream()
                .filter(branchRequest ->
                        !branchRequest.getSwiftCode().equals(swiftCode)
                )
                .toList();
    }


    public CountryRequest getByCountryCode(String countryISO2) {
        countryISO2 = countryISO2.toUpperCase();
        String countryKey = "country:" + countryISO2 + ":swiftCodes";
        try{
            CountryData data = redisClient.get(countryKey, CountryData.class);
            CountryRequest request = new CountryRequest();
            request.setCountryISO2(data.getCountryISO2());
            request.setCountryName(data.getCountryName());

            String[] swiftKeys = data.getSwiftCodes().stream()
                    .map(code -> "swift:" + code)
                    .toArray(String[]::new);

            List<BranchRequest> branches = redisClient.mget(BranchRequest.class, swiftKeys)
                    .stream()
                    .filter(Objects::nonNull)
                    .toList();

            request.setSwiftCodes(branches);
            return request;
        }
        catch (Exception e)
        {
            throw new CountryNotFoundException("CountryISO2 not found: "+countryISO2);
        }
    }

    public Map<String,String> addSwiftCode(@Valid SwiftData swiftData) {
        StringUtils.capitalizeAllStringFields(swiftData);
        Map<String,String> response = new HashMap<>();

        if (swiftData.getSwiftCode().endsWith("XXX") && !swiftData.isHeadquarter()) {
            response.put("message", "If swiftCode ends with 'XXX', isHeadquarter must be true");
            return response;
        } else if (!swiftData.getSwiftCode().endsWith("XXX") && swiftData.isHeadquarter()) {
            response.put("message", "If swiftCode does not end with 'XXX', isHeadquarter must be false");
            return response;
        }

        String key = "swift:" + swiftData.getSwiftCode();
        if (!jedis.exists(key)) {
            redisClient.set(key, swiftData);
            String countryResponse = addCountrySwift(swiftData);
            response.put("message","Swift code added successfully. "+countryResponse);
        }
        else {
            response.put("message","Swift code already exists in database");
        }
        return response;
    }

    public String addCountrySwift(SwiftData swiftData) {
        String countryKey = "country:" + swiftData.getCountryISO2() + ":swiftCodes";
        if (Objects.requireNonNull(redisTemplate.keys(countryKey)).isEmpty()) {
            CountryData countryData = new CountryData();
            countryData.setCountryISO2(swiftData.getCountryISO2());
            countryData.setCountryName(swiftData.getCountryName());
            countryData.setSwiftCodes(List.of(swiftData.getSwiftCode()));
            redisClient.set(countryKey, countryData);
            return "Successfully added Country data.";
        } else {
            redisClient.arrAppend(countryKey, Path.of(".swiftCodes"), swiftData.getSwiftCode());
            return "Country already exists, code appended.";
        }
    }


    public Map<String,String> deleteSwiftCode(String swiftCode) {
        String key = "swift:" + swiftCode;
        try{
            SwiftData data = redisClient.get(key, SwiftData.class);
            String countryKey = "country:" + data.getCountryISO2() + ":swiftCodes";
            List<String> swiftCodes = redisClient.get(countryKey, Path.of(".swiftCodes"));
            swiftCodes.remove(swiftCode);
            redisClient.set(countryKey, swiftCodes, Path.of(".swiftCodes"));
            redisClient.del(key);
            return Map.of("message","Swift Code deleted successfully");
        }
        catch (Exception e)
        {
            throw new SwiftNotFoundException("Swift code not found: "+swiftCode);
        }

    }


}
