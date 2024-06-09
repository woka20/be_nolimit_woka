package com.woka.elasticsearchproject.repository;

import com.woka.elasticsearchproject.response.MinMaxResponse;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;

import java.io.IOException;
import java.util.Map;

public interface CustomEmployeeRepository {
    long countEmployee();
    Double averageSalary();
    MinMaxResponse minMaxSalary();
    Histogram ageDistribution();
    Map<String,Long>  genderDistribution() throws IOException;;
    Map<String, Long> maritalDistribution() throws IOException;
    Map<String, Long>  dateOfJoinDistribution() throws IOException;
    Map<String, Long>  interestDistribution() throws IOException;
    Map<String,Long>  designationDistribution() throws IOException;
    void findDuplicates() throws IOException;
    
}
