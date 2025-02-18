package com.task.swiftParser;

import com.task.swiftParser.Util.CsvImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Objects;

@SpringBootApplication
@EnableAsync
public class SwiftParserApplication{
	@Autowired
	private CsvImporter csvImporter;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SwiftParserApplication.class, args);
	}

	@Async
	@EventListener(ContextRefreshedEvent.class)
	public void importDataIfEmpty() {
		long keyCount = Objects.requireNonNull(redisTemplate.keys("swift:*")).size();

		if (keyCount == 0) {
			System.out.println("No data found in Redis. Starting import...");
			final String FILE_PATH = "src/main/resources/swift_codes.csv";
			csvImporter.importData(FILE_PATH);
			System.out.println("Data import completed successfully.");
		} else {
			System.out.println("Data already exists in Redis. Import skipped.");
		}
	}

}
