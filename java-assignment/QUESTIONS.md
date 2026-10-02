# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
Yes, I would refactor the database access layer to follow a more consistent approach.

Currently, different entities use different strategies for database access. This makes the code harder to understand and maintain because developers need to know different patterns depending on the entity.

I would prefer having repositories responsible for database access and keeping business logic inside the service/use-case layer.

For example, operations such as finding, saving, updating and deleting entities should stay in repositories, while validations and business rules should remain in the service layer.

This would make the code more consistent, easier to test and easier to extend in the future.

I would not refactor everything at once. I would gradually refactor the most frequently changed or complex parts first to reduce the risk of introducing regressions.

----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
Both approaches have advantages.

Using an OpenAPI specification first provides a clear contract between the API and its consumers. It can also generate models and endpoint interfaces, which reduces manual work and keeps the implementation aligned with the API specification.

The disadvantage is that changes to the API require maintaining the specification and regenerating the code. It can also add some complexity for smaller APIs.

Coding the endpoints directly is simpler and faster for a small project. However, the API contract is less explicit and it can become harder to keep documentation and implementation synchronized as the project grows.

For this project, I would prefer the OpenAPI-first approach for all public APIs so that Warehouse, Product and Store follow the same standard.

It would provide consistency and make the API contract easier for other teams to understand and consume.

----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
I would prioritize tests based on business importance and risk.

First, I would test the main business rules, such as Warehouse creation, validation, replacement and archiving. I would also test the relationships and constraints introduced by the fulfilment functionality.

I would use unit tests for service/use-case logic because they are fast and can cover many business scenarios.

I would also keep integration/API tests for the important REST endpoints to verify that the API, database and validation work together correctly.

For example, I would cover:

- Successful operations
- Invalid input
- Duplicate records
- Not-found cases
- Business rule violations
- Boundary conditions

For code coverage, I would configure the CI/CD pipeline to run the test suite and fail the build if coverage falls below the agreed threshold, such as 80%.

I would also review coverage when new features are added rather than relying only on the overall percentage. This helps ensure that important business logic remains tested over time.