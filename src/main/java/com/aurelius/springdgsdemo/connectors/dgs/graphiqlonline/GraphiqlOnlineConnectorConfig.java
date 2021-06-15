package com.aurelius.springdgsdemo.connectors.dgs.graphiqlonline;

import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.client.DefaultGraphQLClient;
import com.netflix.graphql.dgs.client.GraphQLClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DgsAutoConfiguration.class)
public class GraphiqlOnlineConnectorConfig {
    @Bean
    public GraphQLClient graphQLClient() {
        return new DefaultGraphQLClient("https://countries.trevorblades.com/");
    }
}
