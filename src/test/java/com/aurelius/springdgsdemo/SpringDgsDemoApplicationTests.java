package com.aurelius.springdgsdemo;

import com.aurelius.connectors.dgs.graphiqlonline.generated.client.*;
import com.aurelius.connectors.dgs.graphiqlonline.generated.types.Continent;
import com.aurelius.connectors.dgs.graphiqlonline.generated.types.ContinentFilterInput;
import com.aurelius.connectors.dgs.graphiqlonline.generated.types.Country;
import com.aurelius.connectors.dgs.graphiqlonline.generated.types.StringQueryOperatorInput;
import com.aurelius.springdgsdemo.connectors.dgs.graphiqlonline.GraphiqlOnlineConnector;
import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import graphql.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringDgsDemoApplicationTests {

    @Autowired
    private GraphiqlOnlineConnector graphiqlOnlineConnector;


    @Test
    public void testBasicQueryThatReturnsCollection() {
        GraphQLQueryRequest request = new GraphQLQueryRequest(
                new CountriesGraphQLQuery.Builder()
                        .build(),
                new CountriesProjectionRoot()
                        .code()
                        .capital()
        );

        GraphQLResponse graphQLResponse = graphiqlOnlineConnector.executeGraphQLQuery(request);
        List<Country> countryList = graphQLResponse.extractValueAsObject("countries", new TypeRef<>() {
        });

        Assert.assertFalse(countryList.isEmpty());
        Assertions.assertEquals(250, countryList.size());
    }

    @Test
    void whenProjectionIsAppliedThenMapProjectedFieldsOnly() {
        GraphQLQueryRequest request = new GraphQLQueryRequest(
                new ContinentGraphQLQuery.Builder()
                        .code("AF")
                        .build(),
                new ContinentProjectionRoot()
                        .name()
                        .countries()
                        .name()
                        .code()
        );

        GraphQLResponse graphQLResponse = graphiqlOnlineConnector.executeGraphQLQuery(request);
        Continent continent = graphQLResponse.extractValueAsObject("continent", new TypeRef<>() {
        });

        // Projected fields are present
        Assertions.assertEquals("Africa", continent.getName());
        Assertions.assertFalse(continent.getCountries().isEmpty());

        // Fields that is not projected is empty
        Assertions.assertNull(continent.getCode());
    }


    @Test
    void testQueryFilter() {
        GraphQLQueryRequest request = new GraphQLQueryRequest(
                new ContinentsGraphQLQuery.Builder()
                        .filter(
                                ContinentFilterInput.newBuilder()
                                        .code(StringQueryOperatorInput
                                                .newBuilder()
                                                .eq("AF")
                                                .build())
                                        .build())
                        .build(),
                new ContinentsProjectionRoot()
                        .code()
                        .name()
        );

        GraphQLResponse graphQLResponse = graphiqlOnlineConnector.executeGraphQLQuery(request);
        List<Continent> continentList = graphQLResponse.extractValueAsObject("continents", new TypeRef<>() {
        });

        // Projected fields are present
        Assertions.assertEquals(1, continentList.size());
        Assertions.assertEquals("Africa", continentList.get(0).getName());
        Assertions.assertEquals("AF", continentList.get(0).getCode());
    }


    @Test
    void testQueryFilteringNestedFields() {
        GraphQLQueryRequest request = new GraphQLQueryRequest(
                new ContinentsGraphQLQuery.Builder()
                        .filter(
                                ContinentFilterInput.newBuilder()
                                        .code(StringQueryOperatorInput
                                                .newBuilder()
                                                .eq("AF")
                                                .build())
                                        .build())
                        .build(),
                new ContinentsProjectionRoot()
                        .code()
                        .name()
        );

        GraphQLResponse graphQLResponse = graphiqlOnlineConnector.executeGraphQLQuery(request);
        List<Continent> continentList = graphQLResponse.extractValueAsObject("continents", new TypeRef<>() {
        });

        // Projected fields are present
        Assertions.assertEquals(1, continentList.size());
        Assertions.assertEquals("Africa", continentList.get(0).getName());
        Assertions.assertEquals("AF", continentList.get(0).getCode());
    }
}
