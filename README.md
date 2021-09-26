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
- ```SPRING_PROFILES_ACTIVE``` env for ConfigMaps
  ### Dev
- run: ```mvn spring-boot:run -Dspring-boot.run.profiles=dev```
- build: ```mvn clean package -Pdev```
- jvm: ```-Dspring.profiles.active=dev```
- ### Production
- run: ```mvn spring-boot:run -Dspring-boot.run.profiles=prod```
- build: ```mvn clean package -Pprod```
- jvm: ```-Dspring.profiles.active=prod```

## Running Tests
### Unit Tests
- all unit tests: ```mvn clean test```
- specific tests: ```mvn clean test -Dtest=<test1>,<test2>```
- package tests: ```mvn clean test -Dtest="com.example.demo.feature.**"```
- skip unit tests: ```mvn clean install -DskipUnitTests```
### Integration Tests
- all integration tests only: ```mvn failsafe:integration-test failsafe:verify -e```
- run with profile: ```mvn failsafe:integration-test failsafe:verify -Dspring.profiles.active=prod```
- skip integration tests: ```mvn clean install -DskipIntegrationTests```
### Unit + Integration Tests
- both: ```mvn verify```
- skip both: ```mvn install -DskipTests```

## user
- ```id```
- ```username```
- ```password```
- ```firstName```
- ```lastName```
- ```description```

## paths
- ```GET /users/{username}```
- ```GET /users```
- ```POST /users```
- ```PUT /users```
- ```DELETE /users/{username}```


## unit tests
- ```controllers```
- ```individual service functions```
- ```db tests```


## integration tests
- ```REST API: Spring Web``` [ + ]
- ```DB: Testcontainers + Flyway``` [ + ]
- ```Business logic: Services```