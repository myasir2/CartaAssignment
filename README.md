# Vesting Calculation Tool
This tool takes a CSV file containing vesting events, and creates enriched events from them. 

## How to run it
1. First build it using `./gradlew build`
2. Then run the app using `./gradlew run -Pargs="./<file_path>,<target_date>,<optional_quantity_precision"`
3. For example: `./gradlew run -Pargs="./stage_3.csv,2021-01-01,1"`

## Project Structure
1. `/bo` - this package contains Bo classes which house the core business logic 
2. `/model` - this package contains the model classes which the business layer works with
3. `/parser` - this package contains different parsers for the data. For this assignment purpose, it contains a CsvParser
4. `/transformer` - this package contains transformers which will transform raw data from CSV files or other sources to business objects
5. `/util` - this package contains utility classes
