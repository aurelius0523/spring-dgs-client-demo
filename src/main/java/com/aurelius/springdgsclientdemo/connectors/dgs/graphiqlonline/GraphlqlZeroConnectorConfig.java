package com.aurelius.springdgsclientdemo.connectors.dgs.graphiqlonline;

import com.netflix.graphql.dgs.client.DefaultGraphQLClient;
import com.netflix.graphql.dgs.client.GraphQLClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphlqlZeroConnectorConfig {
    @Bean
    public GraphQLClient graphQLClient() {
        return new DefaultGraphQLClient("https://graphqlzero.almansi.me/api");
    }
}
