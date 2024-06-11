package com.woka.elasticsearchproject.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.woka.elasticsearchproject.response.MinMaxResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedMin;
import org.elasticsearch.search.aggregations.metrics.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

public class CustomEmployeeRepositoryImplTest {

    @Mock
    private RestHighLevelClient restHighLevelClient;

    @InjectMocks
    private CustomEmployeeRepositoryImpl customEmployeeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testCountEmployee() throws IOException {
//        // Arrange
//        SearchResponse mockResponse = mock(SearchResponse.class);
//        when(restHighLevelClient.search(any(SearchRequest.class), eq(RequestOptions.DEFAULT)))
//                .thenReturn(mockResponse);
//        when(mockResponse.getHits().getTotalHits().value).thenReturn(100L);
//
//        // Act
//        Map<String, Long> result = customEmployeeRepository.countEmployee();
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(100L, result.get("Total Count Employee"));
//    }

//    @Test
//    public void testAverageSalary() throws IOException {
//        // Arrange
//        SearchResponse mockResponse = mock(SearchResponse.class);
//        Terms terms = mock(Terms.class);
//        ParsedSum parsedSum = mock(ParsedSum.class);
//        when(parsedSum.getValue()).thenReturn(100000.0);
//        when(terms.getBuckets()).thenReturn(List.of(mock(Terms.Bucket.class)));
//        when(restHighLevelClient.search(any(SearchRequest.class), eq(RequestOptions.DEFAULT)))
//                .thenReturn(mockResponse);
//        when(mockResponse.getAggregations().get("distinct_ids_agg")).thenReturn(terms);
//
//        // Act
//        Map<String, Double> result = customEmployeeRepository.averageSalary();
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(50000.0, result.get("Average Salary of All Employees"));
//    }

//    @Test
//    public void testMinMaxSalary() throws IOException {
//        // Arrange
//        SearchResponse mockResponse = mock(SearchResponse.class);
//        ParsedMin parsedMin = mock(ParsedMin.class);
//        ParsedMax parsedMax = mock(ParsedMax.class);
//        when(parsedMin.getValue()).thenReturn(30000.0);
//        when(parsedMax.getValue()).thenReturn(120000.0);
//        when(restHighLevelClient.search(any(SearchRequest.class), eq(RequestOptions.DEFAULT)))
//                .thenReturn(mockResponse);
//        when(mockResponse.getAggregations().get("min_salary")).thenReturn(parsedMin);
//        when(mockResponse.getAggregations().get("max_salary")).thenReturn(parsedMax);
//
//        // Act
//        MinMaxResponse result = customEmployeeRepository.minMaxSalary();
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(30000.0, result.getMinSalary());
//        assertEquals(120000.0, result.getMaxSalary());
//    }

//    @Test
//    public void testAgeDistribution() throws IOException {
//        // Arrange
//        SearchResponse mockResponse = mock(SearchResponse.class);
//        Histogram histogram = mock(Histogram.class);
//        when(restHighLevelClient.search(any(SearchRequest.class), eq(RequestOptions.DEFAULT)))
//                .thenReturn(mockResponse);
//        when(mockResponse.getAggregations().get("age_distribution")).thenReturn(histogram);
//
//        // Act
//        Histogram result = customEmployeeRepository.ageDistribution();
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(histogram, result);
//    }

    @Test
    public void testGenderDistribution() throws IOException {
        // Arrange
        SearchResponse mockResponse = mock(SearchResponse.class);
        Terms terms = mock(Terms.class);
        when(restHighLevelClient.search(any(SearchRequest.class), eq(RequestOptions.DEFAULT)))
                .thenReturn(mockResponse);
        when(mockResponse.getAggregations().get("distribution_gender_agg")).thenReturn(terms);

        // Act
        Map<String, Long> result = customEmployeeRepository.genderDistribution();

        // Assert
        assertNotNull(result);
        // Add assertions for specific expected values based on your mock setup
    }

    // Similarly, write tests for other methods following the same pattern
}

