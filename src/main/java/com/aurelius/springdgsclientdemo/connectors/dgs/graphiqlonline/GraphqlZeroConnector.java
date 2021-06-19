package com.aurelius.springdgsclientdemo.connectors.dgs.graphiqlonline;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.client.GraphQLClient;
import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.HttpResponse;
import com.netflix.graphql.dgs.client.RequestExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@DgsComponent
public class GraphqlZeroConnector {

    @Autowired
    private GraphQLClient graphQLClient;

    /**
     * Execute graphql query by using auto-generated client POJO
     *
     * @param graphQLQueryRequest generated graphQL request
     * @return response
     */
    public GraphQLResponse executeGraphQLQuery(GraphQLQueryRequest graphQLQueryRequest) {
        return graphQLClient.executeQuery(graphQLQueryRequest.serialize(), new HashMap<>(), requestExecutor);
    }


    /**
     * Execute graphql queries by passing in request string directly
     *
     * @param request manually written GraphQL request string
     * @return response
     */
    public GraphQLResponse executeGraphQLQuery(String request) {
        return graphQLClient.executeQuery(request, new HashMap<>(), requestExecutor);
    }

    /**
     * Request executor responsible for making http call, adding header, etc
     */
    private final RequestExecutor requestExecutor = (url, headers, body) -> {
        HttpHeaders requestHeaders = new HttpHeaders();
        headers.forEach(requestHeaders::put);
        ResponseEntity<String> exchange = new RestTemplate().exchange(url, HttpMethod.POST, new HttpEntity(body, requestHeaders), String.class);

        return new HttpResponse(exchange.getStatusCodeValue(), exchange.getBody());
    };

}
