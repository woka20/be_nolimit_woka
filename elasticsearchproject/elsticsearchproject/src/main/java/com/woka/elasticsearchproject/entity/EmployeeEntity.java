package com.woka.elasticsearchproject.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@Document(indexName ="employees")
public class EmployeeEntity {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String designation;
    private int salary;
    private String dateOfJoining;
    private String address;
    private String gender;
    private int age;
    private String maritalStatus;
    private String interests;
    
}
