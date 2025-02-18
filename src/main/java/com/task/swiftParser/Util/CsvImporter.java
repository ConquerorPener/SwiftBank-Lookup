package com.task.swiftParser.Util;

import com.redislabs.modules.rejson.JReJSON;
import com.task.swiftParser.Model.SwiftData;
import com.task.swiftParser.Service.SwiftService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.Reader;


@Component
public class CsvImporter {

    @Autowired
    private JReJSON redisJsonClient;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private SwiftService swiftService;



    public void importData(String path)
    {
        try(Reader reader = new FileReader(path)){
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            for (CSVRecord record : csvParser)
            {
                SwiftData swiftData = new SwiftData(
                        record.get("COUNTRY ISO2 CODE"),
                        record.get("SWIFT CODE"),
                        record.get("NAME"),
                        record.get("ADDRESS"),
                        record.get("COUNTRY NAME"),
                        record.get("SWIFT CODE").endsWith("XXX")
                );
                String key = "swift:" + swiftData.getSwiftCode();
                redisJsonClient.set(key,swiftData);
                swiftService.addCountrySwift(swiftData);
            }
            System.out.println("Data has been loaded to Redis.");
        }
        catch (Exception e)
        {
            System.err.println("Error occurred reading CSV File."+e.getMessage());
        }

    }
}
