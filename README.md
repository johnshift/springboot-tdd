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
- Mid unit-integration-test types should ```@Mock``` all external dependencies
- Integration Tests should reflect overall behaviour of grouped components
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

