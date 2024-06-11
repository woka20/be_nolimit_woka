package com.woka.elasticsearchproject.services;

import com.woka.elasticsearchproject.response.MinMaxResponse;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woka.elasticsearchproject.repository.EmployeeRepository;

import java.io.IOException;
import java.util.Map;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public  Map<String,Long>  getAllEmployeesCount() {
        return employeeRepository.countEmployee();
    }

    public  Map<String,Double>  getAverageSalary() {
        return employeeRepository.averageSalary();
    }

    public MinMaxResponse getMinMaxSalary() {
        return employeeRepository.minMaxSalary();
    }

    public Histogram getAgeDistribution() {
        return employeeRepository.ageDistribution();
    }

    public  Map<String,Long>  getGenderDistribution() throws IOException{
        return employeeRepository.genderDistribution();
    }

    public Map<String, Long> getMaritalDistribution() throws IOException {
        return employeeRepository.maritalDistribution();
    }

    public  Map<String, Long>  getDateOfJoinDistribution() throws IOException {
        return employeeRepository.dateOfJoinDistribution();
    }

    public  Map<String, Long>  getTopInterestDistribution() throws IOException {
        return employeeRepository.interestDistribution();
    }

    public Map<String,Long>  getDesignationDistribution() throws IOException {
        return employeeRepository.designationDistribution();
    }
}
