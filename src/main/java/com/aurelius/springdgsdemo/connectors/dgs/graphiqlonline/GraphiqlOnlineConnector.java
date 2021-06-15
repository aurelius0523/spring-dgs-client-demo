package com.aurelius.springdgsdemo.connectors.dgs.graphiqlonline;

import com.aurelius.connectors.dgs.graphiqlonline.generated.client.CountriesGraphQLQuery;
import com.aurelius.connectors.dgs.graphiqlonline.generated.client.CountriesProjectionRoot;
import com.aurelius.connectors.dgs.graphiqlonline.generated.client.Countries_ContinentProjection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.GraphQLClient;
import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.HttpResponse;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import graphql.ExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@DgsComponent
public class GraphiqlOnlineConnector {

    @Autowired
    private GraphQLClient graphQLClient;

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @DgsQuery
    public List<CountriesProjectionRoot> getCountries() throws JsonProcessingException {
        GraphQLQueryRequest request = new GraphQLQueryRequest(
                new CountriesGraphQLQuery.Builder()
                        .build(),
                new CountriesProjectionRoot()
                        .code()
                        .capital()
        );

        GraphQLResponse graphQLResponse = graphQLClient.executeQuery(request.serialize(), new HashMap<>(), (url, headers, body) -> {
            HttpHeaders requestHeaders = new HttpHeaders();
            headers.forEach(requestHeaders::put);

            /**
             * Use RestTemplate to call the GraphQL service.
             * The response type should simply be String, because the parsing will be done by the GraphQLClient.
             */
            ResponseEntity<String> exchange = new RestTemplate().exchange(url, HttpMethod.POST, new HttpEntity(body, requestHeaders), String.class);

            /**
             * Return a HttpResponse, which contains the HTTP status code and response body (as a String).
             * The way to get these depend on the HTTP client.
             */
            return new HttpResponse(exchange.getStatusCodeValue(), exchange.getBody());
        });
        System.out.println(graphQLResponse);
        System.out.println(new ObjectMapper().writeValueAsString(graphQLResponse.getData()));
        return new ArrayList<>();
    }
}
