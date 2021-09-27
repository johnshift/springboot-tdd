# SpringBoot TDD
Workflow + best practices
- [x] feature-based folder
- [x] formatter: eclipse-java-google-style
- [x] checkstyle: IDE + google_checkstyle
- [x] linting: sonarlint
- [x] coverage: run ```mvn jacoco:prepare-agent test install jacoco:report```
- [x] dockerized mysql for tests
- [x] dev/prod separation deployments
- [ ] business logic in services only
- [ ] use ```@Valid``` annotation for validation

## Running Tests
- Use `mvn clean install > log-file.log` in CI/CD  
  Putting all logs into a logfile is necessary to avoid VM crashes.
- Use IDE test runs when developing locally

## Writing Tests
- Only use ```@Mock``` on unit-tested components
- Unit tests should not use Spring: no ```SpringBootTest```, ```MockBean``` and ```Autowired``` 
- All classes should only use ```@RequiredArgsConstructor``` and ```private final``` fields
- Use ```@ExtendWith(MockitoExtension.class)```  to enable ```@Mock``` annotation. 
- Use ```InjectMocks``` for components requiring mocked dependency
- Mid unit-integration-test types should ```@Mock``` all external dependencies
- Only test atleast one success/failure case for incoming requests on ```Controllers```.  
  Validations and logic for incoming requests should be examined on unit-tests.
- Check serialization for each json response type of ```Controllers``` 
- Integration Tests should reflect overall behaviour of grouped components
- ```Repositories``` are only tested on integration-tests since JPA does the heavy lifting.  
  Testcontainers are more suited to reflect the production database, hence integration-test.
- End-to-end tests should mirror production environment 

## integration-tests:
- It talks to the database
- It communicates across the network
- It touches the file system
- It can’t run at the same time as any of your other unit tests
- You have to do special things to your environment (such as editing config files) to run it
- 
## profiles (changed strat to testcontainers+local_dev only)
Only included for future reference:
- ```mvn spring-boot:run -Dspring-boot.run.profiles=dev```
- ```mvn spring-boot:run -Dspring-boot.run.profiles=prod```
- ```mvn clean package -Pdev```
- ```mvn clean package -Pprod```
- ```-Dspring.profiles.active=dev```
- ```-Dspring.profiles.active=prod```
- ```SPRING_PROFILES_ACTIVE``` ConfigMaps
- 
## paths
- ```GET /users/{username}```
- ```GET /users```
- ```POST /users```
- ```PUT /users```
- ```DELETE /users/{username}```

## user
- ```id```
- ```username```
- ```bio```

## useful guides
- Unit Tests: [Proper Unit Testing](https://www.arhohuttunen.com/spring-boot-unit-testing/)
- Exceptions: 
  - [Spring MVC Exception Handling](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc), [Same Baldeung Guide](https://www.baeldung.com/exception-handling-for-rest-with-spring)
  - [@ControllerAdvice](https://dzone.com/articles/best-practice-for-exception-handling-in-spring-boo)
  - [Best Practices for REST Exceptions](https://www.baeldung.com/rest-api-error-handling-best-practices)
