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

1. Java 17+

2. Redis with RedisJSON module installed

3. Docker

## Clone the repository

>$ git clone https://github.com/ConquerorPener/swiftbank-lookup.git</br>
>$ cd Swift

## Configure and start application

    ------TODO!-------


# API Endpoints

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

## Contributing

### Feel free to fork the repository and submit a pull request!

## License

### This project is licensed under the MIT License.

