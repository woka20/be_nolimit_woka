package com.woka.elasticsearchproject.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woka.elasticsearchproject.response.MinMaxResponse;
import com.woka.elasticsearchproject.services.EmployeeService;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        this.objectMapper= new ObjectMapper();
    }

    @Test
    public void testCountEmployees() throws Exception {
        Map<String, Long> mapMock= new HashMap<>();
        mapMock.put("Count", 5500L);
        when(employeeService.getAllEmployeesCount()).thenReturn(mapMock);

        mockMvc.perform(get("/companydatabase/count"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mapMock)));
    }

    @Test
    public void testAverageEmployeesSalary() throws Exception {
        Map<String, Double> mapMock= new HashMap<>();
        mapMock.put("Average", 5500.75);
        when(employeeService.getAverageSalary()).thenReturn(mapMock);

        mockMvc.perform(get("/companydatabase/average"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mapMock)));
    }

    @Test
    public void testMinMaxSalary() throws Exception {
        MinMaxResponse response = new MinMaxResponse(30000.0, 120000.0);
        when(employeeService.getMinMaxSalary()).thenReturn(response);

        mockMvc.perform(get("/companydatabase/min-max-salary"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)));
    }


    @Test
    public void testHistogramGender() throws Exception {
        Map<String, Long> genderDistribution = new HashMap<>();
        genderDistribution.put("Male", 80L);
        genderDistribution.put("Female", 70L);
        when(employeeService.getGenderDistribution()).thenReturn(genderDistribution);

        mockMvc.perform(get("/companydatabase/gender-distribution"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(genderDistribution)));
    }

    @Test
    public void testHistogramMarital() throws Exception {
        Map<String, Long> maritalDistribution = new HashMap<>();
        maritalDistribution.put("Single", 90L);
        maritalDistribution.put("Married", 60L);
        when(employeeService.getMaritalDistribution()).thenReturn(maritalDistribution);

        mockMvc.perform(get("/companydatabase/marital-distribution"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(maritalDistribution)));
    }

    @Test
    public void testHistogramDateOfJoin() throws Exception {
        Map<String, Long> joinDateDistribution = new HashMap<>();
        joinDateDistribution.put("2022-01", 15L);
        joinDateDistribution.put("2022-02", 10L);
        when(employeeService.getDateOfJoinDistribution()).thenReturn(joinDateDistribution);

        mockMvc.perform(get("/companydatabase/join-date-distribution"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(joinDateDistribution)));
    }

    @Test
    public void testTopInterest() throws Exception {
        Map<String, Long> interestDistribution = new HashMap<>();
        interestDistribution.put("Reading", 30L);
        interestDistribution.put("Sports", 25L);
        when(employeeService.getTopInterestDistribution()).thenReturn(interestDistribution);

        mockMvc.perform(get("/companydatabase/top-interest-distribution"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(interestDistribution)));
    }

    @Test
    public void testHistogramDesignation() throws Exception {
        Map<String, Long> designationDistribution = new HashMap<>();
        designationDistribution.put("Manager", 20L);
        designationDistribution.put("Developer", 50L);
        when(employeeService.getDesignationDistribution()).thenReturn(designationDistribution);

        mockMvc.perform(get("/companydatabase/designation-distribution"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(designationDistribution)));
    }
}
