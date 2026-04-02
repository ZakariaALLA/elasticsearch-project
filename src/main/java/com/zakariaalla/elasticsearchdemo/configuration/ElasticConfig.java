package com.zakariaalla.elasticsearchdemo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.zakariaalla.elasticsearchdemo.repository")
@ComponentScan(basePackages = "com.zakariaalla.elasticsearchdemo")
public class ElasticConfig extends ElasticsearchConfiguration {

    @Value("${elasticsearch.host:localhost}")
    private String host;

    @Value("${elasticsearch.port:9200}")
    private int port;

    @Value("${elasticsearch.username:elastic}")
    private String username;

    @Value("${elasticsearch.password}")
    private String password;

    @Override
    @Bean
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .usingSsl()
                .withBasicAuth(username, password)
                .build();
    }

}