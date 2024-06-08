package com.woka.elasticsearchproject.repository;

import com.woka.elasticsearchproject.response.MinMaxResponse;

import java.io.IOException;

public interface CustomEmployeeRepository {
    long countEmployee();
    Double averageSalary();
    MinMaxResponse minMaxSalary();
    void findDuplicates() throws IOException;
    
}
