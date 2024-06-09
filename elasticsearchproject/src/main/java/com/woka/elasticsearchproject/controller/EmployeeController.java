package com.woka.elasticsearchproject.controller;

import com.woka.elasticsearchproject.response.MinMaxResponse;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.woka.elasticsearchproject.services.EmployeeService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/companydatabase")
public class EmployeeController {

     @Autowired
    private EmployeeService employeeService;

    @GetMapping("/count")
    public long countEmployees() {
        return employeeService.getAllEmployeesCount();
    }

    @GetMapping("/average")
    public Double averageEmployeesSalary() {
        return employeeService.getAverageSalary();
    }

    @GetMapping("/min-max-salary")
    public MinMaxResponse minMaxSalary() {
        return employeeService.getMinMaxSalary();
    }

    @GetMapping("/age-distribution")
    public Histogram  histogramAgeDistribution() {
        return employeeService.getAgeDistribution();
    }

    @GetMapping("/gender-distribution")
    public  Map<String,Long>  histogramGender() throws IOException {
        return employeeService.getGenderDistribution();
    }

    @GetMapping("/marital-distribution")
    public Map<String, Long> histogramMarital() throws IOException {
        return employeeService.getMaritalDistribution();
    }

    @GetMapping("/join-date-distribution")
    public  Map<String, Long>  histogramDateOfJoin() throws IOException {
        return employeeService.getDateOfJoinDistribution();
    }

    @GetMapping("/top-interest-distribution")
    public  Map<String, Long>  topInterest() throws IOException {
        return employeeService.getTopInterestDistribution();
    }

    @GetMapping("/designation-distribution")
    public Map<String,Long>  histogramDesignation() throws IOException {
        return employeeService.getDesignationDistribution();
    }

    @GetMapping("/duplicate")
    public void duplicates() throws IOException {
        employeeService.getDuplicate();
    }
}
