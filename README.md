# SpringBoot TDD
Workflow + best practices
- [x] feature-based folder
- [x] formatter: eclipse-java-google-style
- [x] checkstyle: IDE + google_checkstyle
- [x] linting: sonarlint
- [x] coverage: run ```mvn jacoco:prepare-agent test install jacoco:report```
- [ ] business logic in services only
- [ ] dockerized mysql for tests
- [ ] dev/prod separation deployments
- [ ] use ```@Valid``` annotation for validation

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