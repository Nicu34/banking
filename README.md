# Banking Application
Banking application for ING Romania.

Write an endpoint that will allow an existing user to open a savings account. 
The account can only be opened from Monday to Friday within working hours and the user can have only one savings account.

# Prerequisites: Java 11, Maven (I've used 3.6.0, but the wrapper is present too).
For persistence layer I've used H2 and JPA.
For unit / integration testing I've used Junit5.

# To Run:
mvn clean install 
To run only unit tests: mvn test 
To run integration tests: mvn failsafe:integration-test@integration
To run Spring Boot application: mvn spring-boot:run

After running open Swagger UI for documentation: http://localhost:8080/swagger-ui/ 

Link for Postman collection for the API: https://www.getpostman.com/collections/daf8b7ef0e50dd56bb23 

# Project implementation details
# Entities details
User {username, password} -> entity responsible for saving a user into database

WorkingDay {DayEnum} -> entity responsible for saving day into database

BankBranch {location, openingTime, closingTime, List<WorkingDay>} -> entity responsible for saving a bank branch (bank agency) alongside its details into the database. One bank branch can belong to multiple WorkingDay entities.
Note: The openingTime and closingTime is assumed to be saved using UTC offset. 
A BankBranch entity is saved during load time of the application (see DataLoader class).
  
BankAccount {amount, accountType, bankBranch, user} -> entity responsible for saving a bank account into the database. A unidirectional many to one relationship exists between this entity and User and BankBranch.
A bank account must belong to a User.
A bank account must belong to a Bank Branch.

# API methods details
/user/register - used for registration of a user.

/user/authenticate - used for authenticating a user. A JWT authorization token is returned which must be used for authorization by putting it into the Authorization header with value "Bearer <received authorization token>
  
/banking/createAccount - used for creating a bank account. A user can create multiple bank accounts of OTHERS type, but can create only one account of SAVINGS type. 
Creation date time is sent alongside its offset, but the server converts the time to UTC time for persisting and business logic. Valid Authorization header needs to be present on request.

/banking/getAccounts - used for retrieving the bank accounts of a User. Valid Authorization header needs to be present on request.

/swagger-ui/ - for REST API documentation
