# Feature module architecture

The `order`, `payment`, and `cart` features are organized as vertical modules, and `docs/REFACTOR_PACKAGE_MAP.md` records the proposed package/file ownership map for the remaining legacy packages.

```text
api -> application -> domain
infrastructure -> domain/application ports
```

## Layers

- `api`: HTTP controllers and transport mapping.
- `application`: use-case contracts, DTOs, validation, and orchestration.
- `domain`: business model, invariants, and ports owned by the core.
- `infrastructure`: framework-specific persistence mappings and integrations.

## Compatibility migration

Existing controllers, DTOs, service implementations, and repositories have callers across features. Migrate one feature at a time and preserve old interfaces as temporary adapters where needed. Existing Spring beans, HTTP routes, payloads, and database schema must remain unchanged during package moves.

## Dependency rules

1. Domain packages must not depend on Spring MVC or persistence frameworks.
2. Application services depend on domain types and ports, not concrete persistence classes.
3. API adapters validate/translate requests and call application use cases.
4. Infrastructure contains framework-specific persistence mappings.
5. Cross-module calls use application contracts, not another module's infrastructure.
6. Do not duplicate Spring beans while old and new packages coexist.

## Transition status

The current repository is in a mixed transitional state: `order`, `payment`, and `cart` contain some feature-oriented packages, while many controllers, services, DTOs, entities, repositories, and MyBatis XML mappings remain in legacy packages. The package map is a migration plan; package moves require updating imports, mapper discovery, and tests atomically. No endpoint paths or database schema should change as part of this refactor.
