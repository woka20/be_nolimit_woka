package com.woka.elasticsearchproject.repository;

import java.io.IOException;
import java.util.List;

import com.woka.elasticsearchproject.response.MinMaxResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

@Repository
public class CustomEmployeeRepositoryImpl implements CustomEmployeeRepository {

    private final RestHighLevelClient restHighLevelClient;

    public CustomEmployeeRepositoryImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public long countEmployee() {
        SearchRequest searchRequest = new SearchRequest("companydatabase");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        searchSourceBuilder.aggregation(
                AggregationBuilders.terms("distinct_ids_agg")
                        .field("_id")
                        .size(7000000)
        );

        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return  searchResponse.getHits().getTotalHits().value;

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Double averageSalary() {
        SearchRequest searchRequest = new SearchRequest("companydatabase");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.aggregation(
                AggregationBuilders.terms("distinct_ids_agg")
                        .field("_id")
                        .size(7000000) // Adjust this size to fit your dataset
                        .subAggregation(AggregationBuilders.sum("total_salary").field("Salary"))
        );

        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Terms distinctIdsAgg = searchResponse.getAggregations().get("distinct_ids_agg");
            double totalSalary = 0;
            long count=0;
            for (Terms.Bucket bucket : distinctIdsAgg.getBuckets()) {

                ParsedSum sumSalary = bucket.getAggregations().get("total_salary");

                totalSalary += sumSalary.getValue();
                count++;
            }

            System.out.println(totalSalary);
            return totalSalary/count;

        } catch (IOException e) {
            e.printStackTrace();
            return 0.4;
        }
 }

    public MinMaxResponse minMaxSalary() {
        SearchRequest searchRequest = new SearchRequest("companydatabase");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.aggregation(
                AggregationBuilders.min("min_salary").field("Salary")
        );

        searchSourceBuilder.aggregation(
                AggregationBuilders.max("max_salary").field("Salary")
        );

        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            ParsedMin minSalaryAgg = searchResponse.getAggregations().get("min_salary");
            ParsedMax maxSalaryAgg = searchResponse.getAggregations().get("max_salary");
            double salaryMin = minSalaryAgg.getValue();
            double salaryMax=maxSalaryAgg.getValue();

            return new MinMaxResponse(salaryMin, salaryMax);

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
    }

    public void findDuplicates() throws IOException {
        SearchRequest searchRequest = new SearchRequest("companydatabase");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        searchSourceBuilder.aggregation(
                AggregationBuilders.terms("new_distinct_ids_agg")
                        .field("_id")
                        .size(7000000)
        );

        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            ParsedStringTerms distinctIdsAgg = searchResponse.getAggregations().get("new_distinct_ids_agg");
            List<? extends Terms.Bucket> buckets = distinctIdsAgg.getBuckets();
            int count=0;
            for (Terms.Bucket bucket : buckets) {
                count++;
                if (bucket.getDocCount()>1) {
                    System.out.println("ID: " + bucket.getKeyAsString() + ", Doc Count: " + bucket.getDocCount());
                }
            }
           System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
