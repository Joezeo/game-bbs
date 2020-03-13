package com.joezeo.joefgame.web.config;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolrConfig {
    @Value("${solr.service.url}")
    private String solrUrl;

    @Bean("solrClient")
    public HttpSolrClient httpSolrClient(){
        HttpSolrClient.Builder builder = new HttpSolrClient.Builder(solrUrl);
        return builder.build();
    }
}
