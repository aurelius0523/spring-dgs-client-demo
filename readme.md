## spring-dgs-client-demo

Demo repository to find to showcase how to
use [Netflix's dgs](https://netflix.github.io/dgs/)
to make API calls to a graphql server.

## Getting started

1. Generate POJO from graphQL schema (obtained
   from https://lucasconstantino.github.io/graphiql-online/, stored
   in `src/main/resources`) by running `./gradlew generateJava`
1. Run individual test cases in `SpringDgsClientDemoApplicationTests` for different
   scenarios

## Learnings

1. `dgs` client code generator is simply a wrapper. You can manually provide
   GraphQL query string instead of generating query/mutation from generated POJO
   if certain functionalities (alias, nested arguments) are not supported
1. `dgs` provides `gradle` plugin to auto-generate POJO from `.graphqls`
   file. It's not available for `maven`
1. In general, there's an abundance of libraries that help set up a GraphQL
   server but there aren't many that supports calling one (could be wrong).
1. It seems that `dgs` client code generation has a limitation where it _seemingly_ could not query multiple fields in root Query. For example,
   this could not be done via generated client code:
   ```graphql
   query {
      continents(filter: {code:{eq:"AF" } }) { code name }, 
      countries{code name}
   }
   ```
1. `dgs` client code generator also does not seem to
   support [alias](https://graphql.org/learn/queries/#aliases). There is an open
   PR [here](https://github.com/Netflix/dgs-codegen/issues/64). This can still
   be done with manual query but requires custom object/model that matches the
   alias
