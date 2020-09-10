# banking
Banking application for ING Romania.

Write an endpoint that will allow an existing user to open a savings account. 
The account can only be opened from Monday to Friday within working hours and the user can have only one savings account.

# Prerequisites: Java 11, Maven (I've used 3.6.0, but the wrapper is present too).

# To Run:
mvn clean install 
To run only unit tests: mvn test 
To run integration tests: mvn failsafe:integration-test@integration
To run Spring Boot application: mvn spring-boot:run

After running open Swagger UI for documentation: http://localhost:8080/swagger-ui/ 

