# Feature module architecture

The `order`, `payment`, and `cart` features are organized as vertical modules.

```
api -> application -> domain
infrastructure -> domain/application ports
```

## Layers

- `api`: HTTP controllers and transport mapping.
- `application/service`: use-case contracts and orchestration.
- `application/dto`: commands/results at the application boundary.
- `domain/entity`: business model and invariants.
- `domain/repository`: persistence ports owned by the core.
- `infrastructure/persistence`: MyBatis/JPA adapters.

## Compatibility migration

The existing controllers, DTOs, service implementations, and repositories are still
referenced by other features. Module-owned application contracts have therefore been
introduced, and the legacy service interfaces extend those contracts. Existing Spring
beans and HTTP routes remain unchanged during this first extraction step.

## Dependency rules

1. Domain packages must not depend on Spring MVC or persistence frameworks.
2. Application services depend on domain types and ports, not concrete persistence classes.
3. API adapters validate/translate requests and call application use cases.
4. Infrastructure contains framework-specific persistence mappings.
5. Cross-module calls use application contracts, not another module's infrastructure.

## Transition status

This commit establishes package boundaries and compatibility contracts. The existing
service implementations still contain orchestration and persistence dependencies; moving
those implementations, controllers, and SQL mappings is a subsequent migration that must
be covered by compile and integration tests. No endpoint paths or database schema changed.
