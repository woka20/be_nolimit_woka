package com.woka.elasticsearchproject.controller;

import com.woka.elasticsearchproject.response.MinMaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.woka.elasticsearchproject.services.EmployeeService;

import java.io.IOException;

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


    @GetMapping("/duplicate")
    public void duplicates() throws IOException {
        employeeService.getDuplicate();
    }
}
