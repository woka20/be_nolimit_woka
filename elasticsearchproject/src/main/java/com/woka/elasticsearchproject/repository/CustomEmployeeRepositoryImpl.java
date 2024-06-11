package com.woka.elasticsearchproject.repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.woka.elasticsearchproject.response.MinMaxResponse;
import com.woka.elasticsearchproject.util.IndexUtil;
import com.woka.elasticsearchproject.util.MappingUtil;
import com.woka.elasticsearchproject.util.PipelineUtil;
import com.woka.elasticsearchproject.util.ReindexUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public class CustomEmployeeRepositoryImpl implements CustomEmployeeRepository {


    @Value("${elasticsearch.main.index}")
    private String mainIndex;

    @Value("${elasticsearch.new.index}")
    private String newIndex;

    @Value("${elasticsearch.terms.size}")
    private int termsSize;

    private MappingUtil mappingUtil;

    private IndexUtil indexUtil;

    private PipelineUtil pipelineUtil;
    private ReindexUtil reindexUtil;




    private final RestHighLevelClient restHighLevelClient;

    public CustomEmployeeRepositoryImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public Map<String,Long> countEmployee() {
        SearchRequest searchRequest = new SearchRequest("companydatabase");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        searchSourceBuilder.aggregation(
                AggregationBuilders.terms("distinct_ids_agg")
                        .field("_id")
                        .size(termsSize)
        );

        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Map<String,Long> mapCount= new HashMap<>();
            mapCount.put("Total Count Employee", searchResponse.getHits().getTotalHits().value);
            return mapCount;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public  Map<String,Double> averageSalary() {
        SearchRequest searchRequest = new SearchRequest(mainIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.aggregation(
                AggregationBuilders.terms("distinct_ids_agg")
                        .field("_id")
                        .size(termsSize) // Adjust this size to fit your dataset
                        .subAggregation(AggregationBuilders.sum("total_salary").field("Salary"))
        );

        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Terms distinctIdsAgg = searchResponse.getAggregations().get("distinct_ids_agg");
            double totalSalary = 0;
            long count = 0;
            for (Terms.Bucket bucket : distinctIdsAgg.getBuckets()) {

                ParsedSum sumSalary = bucket.getAggregations().get("total_salary");

                totalSalary += sumSalary.getValue();
                count++;
            }

            Map<String, Double> mapAverage = new HashMap<>();
            mapAverage.put("Average Salary of All Employees", totalSalary / count);
            return mapAverage;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MinMaxResponse minMaxSalary() {
        SearchRequest searchRequest = new SearchRequest(mainIndex);
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
            double salaryMax = maxSalaryAgg.getValue();

            return new MinMaxResponse(salaryMin, salaryMax);

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
    }

    @Override
    public Histogram ageDistribution() {
        SearchRequest searchRequest = new SearchRequest(mainIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.aggregation(
                AggregationBuilders.histogram("age_distribution").field("Age").interval(5)
        );

        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Histogram ageHistogramAgg = searchResponse.getAggregations().get("age_distribution");

            return ageHistogramAgg;

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }

    }

    @Override
    public Map<String,Long> genderDistribution() throws IOException {
        SearchRequest searchRequest = new SearchRequest(mainIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        indexUtil=new IndexUtil(restHighLevelClient);
        mappingUtil= new MappingUtil(restHighLevelClient);


        indexUtil.allowIndexModification(mainIndex);
        mappingUtil.updateFieldToKeyword(mainIndex, "Gender", "text");


        searchSourceBuilder.aggregation(
                AggregationBuilders.terms("distribution_gender_agg")
                        .field("Gender")
                        .size(termsSize)


        );

        searchRequest.source(searchSourceBuilder);

        try {

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Terms genderAgg = searchResponse.getAggregations().get("distribution_gender_agg");
            Map<String, Long> mapGender= new HashMap<>();

            for (Terms.Bucket bucket : genderAgg.getBuckets()) {
                mapGender.put(bucket.getKeyAsString(), bucket.getDocCount());
            }
            return mapGender;
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }

    }

    @Override
    public  Map<String, Long>  maritalDistribution() throws IOException {
        SearchRequest searchRequest = new SearchRequest(mainIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        indexUtil=new IndexUtil(restHighLevelClient);
        mappingUtil= new MappingUtil(restHighLevelClient);


        indexUtil.allowIndexModification(mainIndex);
        mappingUtil.updateFieldToKeyword(mainIndex, "MaritalStatus", "text");

        searchSourceBuilder.aggregation(
                AggregationBuilders.terms("marital_status_dist_agg")
                        .field("MaritalStatus")
                        .size(termsSize) // Optional: Number of terms to return
        );

        searchRequest.source(searchSourceBuilder);

        try {

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Terms maritalAgg = searchResponse.getAggregations().get("marital_status_dist_agg");

            Map<String, Long> mapMaritalStatus= new HashMap<>();

            for (Terms.Bucket bucket : maritalAgg.getBuckets()) {
                mapMaritalStatus.put(bucket.getKeyAsString(), bucket.getDocCount());

            }
           return mapMaritalStatus;

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }



    }

    @Override
    public  Map<String, Long>  dateOfJoinDistribution() throws IOException {
        SearchRequest searchRequest = new SearchRequest(mainIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        indexUtil=new IndexUtil(restHighLevelClient);
        indexUtil.allowIndexModification(mainIndex);

        searchSourceBuilder.aggregation(
                AggregationBuilders.dateHistogram("joining_date_dist_agg")
                        .field("DateOfJoining")
                        .interval(DateHistogramInterval.MONTH.estimateMillis())
                        .format("yyyy-MM-dd")
        );

        searchRequest.source(searchSourceBuilder);

        try {

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Histogram dateDistAgg = searchResponse.getAggregations().get("joining_date_dist_agg");
            Map<String, Long> mapDateOfJoin = new HashMap<>();

            for (Histogram.Bucket bucket : dateDistAgg.getBuckets()) {
                if(bucket.getDocCount() > 0)
                    mapDateOfJoin.put(bucket.getKeyAsString(), bucket.getDocCount());

            }

            return mapDateOfJoin;

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }


    }

    @Override
    public  Map<String, Long>  interestDistribution() throws IOException {
        SearchRequest searchRequest = new SearchRequest(newIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        String interestField="Interests";
        indexUtil=new IndexUtil(restHighLevelClient);
        pipelineUtil=new PipelineUtil(restHighLevelClient);
        reindexUtil=new ReindexUtil(restHighLevelClient);
        mappingUtil= new MappingUtil(restHighLevelClient);




        indexUtil.allowIndexModification(mainIndex);
        pipelineUtil.createSplitPipeline();
        mappingUtil.updateFieldToKeyword(mainIndex, interestField, "text" );
        indexUtil.allowIndexModification(newIndex);
        reindexUtil.reindex(mainIndex, newIndex);


        searchSourceBuilder.aggregation(
                AggregationBuilders.terms("in_agg")
                        .field(interestField+".keyword")
                        .size(termsSize) // Optional: Number of terms to return
        );

        searchRequest.source(searchSourceBuilder);

        try {

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Terms interestAgg = searchResponse.getAggregations().get("in_agg");
            Long highestCount=0L;
            String interestsName="None";
            Map<String, Long> topInterest= new HashMap<>();
            for (Terms.Bucket bucket : interestAgg.getBuckets()) {
                if(bucket.getDocCount()>highestCount){
                    highestCount= bucket.getDocCount();
                    interestsName= bucket.getKeyAsString();
                }
            }
            topInterest.put(interestsName, highestCount);
            return topInterest;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String,Long>  designationDistribution() throws IOException {
        SearchRequest searchRequest = new SearchRequest(newIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        indexUtil=new IndexUtil(restHighLevelClient);
        pipelineUtil=new PipelineUtil(restHighLevelClient);
        reindexUtil=new ReindexUtil(restHighLevelClient);
        mappingUtil= new MappingUtil(restHighLevelClient);


        indexUtil.allowIndexModification(mainIndex);
        pipelineUtil.createSplitPipeline();
        mappingUtil.updateFieldToKeyword(mainIndex, "Designation", "text" );
        indexUtil.allowIndexModification(newIndex);
        reindexUtil.reindex(mainIndex, newIndex);


        searchSourceBuilder.aggregation(
                AggregationBuilders.terms("designation_dist_agg")
                        .field("Designation.keyword")
                        .size(termsSize)
        );

        searchRequest.source(searchSourceBuilder);

        try {

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Terms designationDistAgg= searchResponse.getAggregations().get("designation_dist_agg");
            Map<String,Long> mapDesignation= new HashMap<>();

            for (Terms.Bucket bucket : designationDistAgg.getBuckets()) {
                mapDesignation.put(bucket.getKeyAsString(), bucket.getDocCount());

            }

            return mapDesignation;
        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }

    }

}
