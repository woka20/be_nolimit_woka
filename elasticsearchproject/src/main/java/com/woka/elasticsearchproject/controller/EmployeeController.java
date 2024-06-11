package com.woka.elasticsearchproject.controller;

import com.woka.elasticsearchproject.response.MinMaxResponse;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> countEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployeesCount());
    }

    @GetMapping("/average")
    public ResponseEntity<?> averageEmployeesSalary() {
        return ResponseEntity.ok(employeeService.getAverageSalary());

    }

    @GetMapping("/min-max-salary")
    public ResponseEntity<MinMaxResponse> minMaxSalary() {
        return ResponseEntity.ok(employeeService.getMinMaxSalary());
    }

    @GetMapping("/age-distribution")
    public ResponseEntity<Histogram>  histogramAgeDistribution() {
        return ResponseEntity.ok(employeeService.getAgeDistribution());
    }

    @GetMapping("/gender-distribution")
    public  ResponseEntity<Map<String,Long>>  histogramGender() throws IOException {
        return ResponseEntity.ok(employeeService.getGenderDistribution());
    }

    @GetMapping("/marital-distribution")
    public Map<String, Long> histogramMarital() throws IOException {
        return employeeService.getMaritalDistribution();
    }

    @GetMapping("/join-date-distribution")
    public   ResponseEntity<Map<String,Long>>   histogramDateOfJoin() throws IOException {
        return ResponseEntity.ok(employeeService.getDateOfJoinDistribution());
    }

    @GetMapping("/top-interest-distribution")
    public   ResponseEntity<Map<String,Long>>   topInterest() throws IOException {
        return ResponseEntity.ok(employeeService.getTopInterestDistribution());
    }

    @GetMapping("/designation-distribution")
    public ResponseEntity<Map<String,Long>>   histogramDesignation() throws IOException {
        return ResponseEntity.ok(employeeService.getDesignationDistribution());
    }

}
