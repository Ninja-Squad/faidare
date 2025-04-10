package fr.inrae.urgi.faidare.config;

import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ElasticSearchConfig {

    private FaidareProperties faidareProperties;

    @Bean
    public FaidareProperties faidareProperties() {
        return new FaidareProperties();
    }

    public ElasticSearchConfig(FaidareProperties faidareProperties) {
        this.faidareProperties = faidareProperties;
    }

    /**
     * Exposes the bean of type FaidareProperties under the name
     * "faidarePropertiesBean"so that "faidarePropertiesBean" can be used
     * in SpEL expressions
     */
    @Primary
    @Bean
    public FaidareProperties faidarePropertiesBean() {
        return this.faidareProperties;
    }


}
