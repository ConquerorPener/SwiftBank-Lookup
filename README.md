# SwiftBank-Lookup
#### SwiftBank Lookup is a REST API for managing and retrieving SWIFT codes using Redis as a database.
#### The application allows users to search for SWIFT codes, fetch details by country, and manage SWIFT entries efficiently.

## Features
1. Retrieve bank details by SWIFT code.

2. Fetch all SWIFT codes for a given country.

3. Add and delete SWIFT codes.

4. Uses Redis for fast lookup and caching.

## Technologies Used

1. Java Spring Boot

2. Redis with RedisJSON module

3. Jedis & JReJSON

4. Docker

## Installation & Setup

### Prerequisites

1. Git
2. Docker with Docker Compose

## Clone the repository
1. Open a terminal and run the following command to clone the repository:

>$ git clone https://github.com/ConquerorPener/swiftbank-lookup.git</br>

2. Once the process is complete, navigate to the project directory:

>$ cd SwiftBack-Lookup

## Running Docker Containers

Now, you can start the application using Docker Compose:


>$ docker compose up

After a while, application will be ready to accept connections

# API Endpoints

After starting your application you can access all endpoints that are listed below.

## Get SWIFT details

>GET http://localhost:8080/v1/swift-codes/{swiftCode}

### Response:
```
{
    "address": String,
    "bankName": String,
    "countryISO2": String,
    "countryName": String,
    "isHeadquarter": Boolean,
    "swiftCode": String,
    "branches": [
        {
            "address": String,
            "bankName": String,
            "countryISO2": String,
            "isHeadquarter": String,
            "swiftCode": String
        },
        ...
    ]
}
```
## Get SWIFT codes by country

>GET http://localhost:8080/v1/swift-codes/country/{countryISO2code}

### Response:
```
{
    "countryISO2": String,
    "countryName": String,
    "swiftCodes": 
    [
        {
            "address": String,
            "bankName": String,
            "countryISO2": String,
            "isHeadquarter": Boolean,
            "swiftCode": String
        },
        ...
    ]
}
```

## Add a SWIFT code

>POST http://localhost:8080/v1/swift-codes

### Request:
```
{
    "address": String,
    "bankName": String,
    "countryISO2": String,
    "countryName": String,
    "isHeadquarter": Boolean,
    "swiftCode": String
}
```

### Response:
```
{
    "message": String
}
```


### Delete a SWIFT code

>DELETE http://localhost:8080/v1/swift-codes/{swiftCode}

### Response:
```
{
    "message": String
}
```

## Error Handling

### Errors are returned as JSON responses:
```
{
    "error": String
}
```

## Unit & Integration testing

This application is tested by 19 unit and integration tests that are available in src/src/test/ directory.

They run every build of application, but you can also run them by yourself.

To achieve that you have to follow those steps:

Navigate to project directory (SwiftBank-Lookup):
>$ cd SwiftBank-Lookup

Start the application with Docker Compose:
>$ docker compose up

access the backend container:
>$ docker exec -it backend bash

Run tests using Maven:
>$ mvn test

Exit the container:
>$ exit


## Data Import

The application imports all data from a spreadsheet by default to enhance testing and user experience.

If you want to disable this feature, follow these steps:

1. Delete CsvImporter.java from Util directory
2. Remove swift_codes.csv from Resources directory
3. Override SwiftParserApplication as follows:
```
package com.task.swiftParser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SwiftParserApplication{
	public static void main(String[] args) {
		SpringApplication.run(SwiftParserApplication.class, args);
	}
}
```
   
## Contributing

### Feel free to fork the repository and submit a pull request!

## License

### This project is licensed under the MIT License.

