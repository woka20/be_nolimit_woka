package com.woka.elasticsearchproject.services;

import com.woka.elasticsearchproject.response.MinMaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woka.elasticsearchproject.repository.EmployeeRepository;

import java.io.IOException;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;

    public long getAllEmployeesCount() {
        return employeeRepository.countEmployee();
    }

    public Double getAverageSalary(){
        return employeeRepository.averageSalary();
    }

    public MinMaxResponse getMinMaxSalary(){
        return employeeRepository.minMaxSalary();
    }

    public void getDuplicate() throws IOException {
        employeeRepository.findDuplicates();
    }
}
