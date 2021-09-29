<h1 align="center"> SpringBoot TDD </h1>

<div align="center">
  [![CI](https://github.com/johnshift/springboot-tdd/actions/workflows/ci.yml/badge.svg)](https://github.com/johnshift/springboot-tdd/actions/workflows/ci.yml)
  [![Coverage](.github/badges/jacoco.svg)](https://github.com/johnshift/springboot-tdd/actions/workflows/ci.yml)
  </span>
</div>

## Implementations
- [x] feature-based folder
- [x] formatter: eclipse-java-google-style
- [x] checkstyle: IDE + google_checkstyle
- [x] linting: sonarlint
- [x] coverage: run `mvn jacoco:prepare-agent test install jacoco:report`
- [x] dockerized mysql for tests
- [x] dev/prod separation deployments
- [x] business logic in services only
- [x] mapstruct dto
- [x] validation
- [x] controller-based exception handling
- [x] ci + coverage stickers

## Best Practices
Code:
- Only services are imported into `RestControllers`. No logic, validation or repositories involved.
- Do not write validations by hand. Use `spring-boot-starter-validation` instead
- Only `Validate` DTO's. This saves network resources if data not in proper format
- Request/Response returned should only be DTO's (avoid 1:1 mapping of db entities and json)  
  Use explicit custom/generic mapping for request/response.  
  The goal is to avoid sending very large models and reduce API calls
- Avoid `@Autowired` and use `@RequiredArgsConstructor` and `private final`  
  This provides more room for unit testing.
- `Services` should contain all business logic
- `DTO/Entity` conversions should happen in `Services`
- `Repositories` should contain all predefined/custom DB logic
- Unless you find a way to progmatically run `checkstyles` and `sonarlint`,  
  make sure push code with no warnings from ide lints.
- Use `@ControllerAdvice` for Global Exception handler i.e. malformed requests, invalid path etc etc  
  otherwise use `@ExceptionHandlers` per feature controller
- Use [easy-random](https://github.com/j-easy/easy-random) for fast creation of pojo's in tests

Tests:
- Only use `@Mock` on unit-tested components
- Unit tests should not use Spring: no `SpringBootTest`, `MockBean` and `Autowired` 
- All classes should only use `@RequiredArgsConstructor` and `private final` fields
- Use `@ExtendWith(MockitoExtension.class)`  to enable `@Mock` annotation. 
- Use `InjectMocks` for components requiring mocked 
- `Validations and logic` for incoming requests should be examined at `unit-tests`
- Mid unit-integration-test types should `@Mock` all external dependencies
- Only test atleast one success/failure case for incoming requests on `Controllers`.  
- `Controllers`: `MockMvc` for unit-tests, `WebTestClient` for integration-test
- Check serialization for each json response type of `Controllers` 
- `Mocks` only for unit-tests (e.g. services, logic, validations)
- `Test slices` for mid unit-integration-tests (e.g. controllers, jpa)
- `SpringBootTest` for integration-tests (e.g. testcontainers, external services)
- Integration Tests should reflect overall behaviour of grouped components
- `Repositories` are only tested on integration-tests since JPA does the heavy lifting.  
  Testcontainers are more suited to reflect the production database, hence integration-test.
- End-to-end tests should represent pre-production environment 
- Always prefer to use `JUnit5`. `@Test` annotation should point to `org.junit.jupiter.api.Test`

## Running Tests
- Use `mvn clean install > log-file.log` in CI/CD  
  Putting all logs into a logfile is necessary to avoid VM crashes.
- Use IDE test runs when developing locally

## integration-tests:
- It talks to the database
- It communicates across the network
- It touches the file system
- It can’t run at the same time as any of your other unit tests
- You have to do special things to your environment (such as editing config files) to run it
  
## paths
- `GET /users/{username}`
- `GET /users`
- `POST /users`
- `PUT /users`
- `DELETE /users/{username}`

## user
- `id`
- `username`
- `bio`


## notes
- put `"coverage-gutters.coverageReportFileName": "target/site/jacoco/index.html"`  
  in `vscode settings.json` when developing in Java to enable coverage gutters

## useful guides
- Testing: 
  - [Test Slices](https://reflectoring.io/spring-boot-test/)
  - [Proper Unit Testing](https://www.arhohuttunen.com/spring-boot-unit-testing/)
  - [Unit Test Naming Best Practices](https://stackoverflow.com/q/155436)
- Architecture:
  - [DTO Pattern](https://www.baeldung.com/java-dto-pattern)
  - [Mapstruct Quick Guide](https://www.baeldung.com/mapstruct)
- Exceptions: 
  - [Spring MVC Exception Handling](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc), [Same Baldeung Guide](https://www.baeldung.com/exception-handling-for-rest-with-spring)
  - [@ControllerAdvice Guide](https://dzone.com/articles/best-practice-for-exception-handling-in-spring-boo)
  - [@ControllerAdvice StackOverflow](https://stackoverflow.com/a/50053782)
  - [Best Practices for REST Exceptions](https://www.baeldung.com/rest-api-error-handling-best-practices)
- Validations:
  - [Validation + Exception handling](https://reflectoring.io/bean-validation-with-**spring**-boot/)
  - [Are you using validations correctly?](https://medium.com/javarevisited/are-you-using-valid-and-validated-annotations-wrong-b4a35ac1bca4)
- Security:
  - Limit Exposed Fields:
    - [Exposing Entity Using DTO](https://auth0.com/blog/automatically-mapping-dto-to-entity-on-spring-boot-apis/#DTOs-and-Spring-Boot-APIs)
    - [Sonarlint DTO Vulnerability](https://rules.sonarsource.com/java/tag/spring/RSPEC-4684)
