# SpringBoot TDD
Workflow + best practices
- [ ] feature-based folder
- [ ] format + checkstyle + lint 
- [ ] business logic in services only
- [ ] coverage report
- [ ] dockerized mysql for tests
- [ ] dev/prod separation deployments

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