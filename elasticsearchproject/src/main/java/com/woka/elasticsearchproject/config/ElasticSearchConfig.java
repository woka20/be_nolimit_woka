package com.woka.elasticsearchproject.config;

import java.util.List;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.data.elasticsearch.client.ClientConfiguration;
// import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class ElasticSearchConfig {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder
                    .setDefaultHeaders(List.of(
                            new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString()),
                            new BasicHeader("X-Elastic-Product", "Elasticsearch")))
                    .addInterceptorLast((HttpResponseInterceptor) (response, context) -> {
                        logger.info("Interceptor called...");
                        if (response.getFirstHeader("X-Elastic-Product") == null) {
                            response.addHeader("X-Elastic-Product", "Elasticsearch");
                        }
                    });
                  
            return httpClientBuilder;
        });

        return new RestHighLevelClient(builder);
    }


}

