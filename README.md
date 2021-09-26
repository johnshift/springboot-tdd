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

## SpringBoot Profiles

- ```mvn spring-boot:run -Dspring-boot.run.profiles=dev```
- ```mvn spring-boot:run -Dspring-boot.run.profiles=prod```
- ```mvn clean package -Pdev```
- ```mvn clean package -Pprod```
- ```-Dspring.profiles.active=dev```
- ```-Dspring.profiles.active=prod```
- ```SPRING_PROFILES_ACTIVE``` ConfigMaps

## Running Tests
- unit-test only: ```mvn clean test```
- specific unit-test: ```mvn clean test -Dtest=<test1>,<test2>```
- package unit-test: ```mvn clean test -Dtest="com.example.demo.feature.**"```
- skip unit-test: ```mvn clean install -DskipUnitTests```
- integration-test only: ```mvn failsafe:integration-test failsafe:verify -e```
- using profiles: ```mvn failsafe:integration-test failsafe:verify -Dspring.profiles.active=prod```
- skip integration-test: ```mvn clean install -DskipIntegrationTests```
- test both: ```mvn verify```
- skip both: ```mvn install -DskipTests```

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
- [Proper Unit Testing](https://www.arhohuttunen.com/spring-boot-unit-testing/)
