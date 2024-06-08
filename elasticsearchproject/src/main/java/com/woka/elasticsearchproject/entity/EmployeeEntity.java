package com.woka.elasticsearchproject.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@Document(indexName ="companydatabase")
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

    //Getter and Setter
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName(){
        return this.lastName;
    }

    
    public void setDesignation(String designation) {
        this.designation= designation;
    }

    public String getDesignation(){
        return this.designation;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining= dateOfJoining;
    }

    public String getdateOfJoining(){
        return this.dateOfJoining;
    }

    public void setAddress(String address) {
        this.address= address;
    }

    public String getAddress(){
        return this.address;
    }

    public void setGender(String gender) {
        this.gender= gender;
    }

    public String getGender(){
        return this.gender;
    }

    public void setSalary(int salary) {
        this.salary= salary;
    }

    public int getSalary(){
        return this.salary;
    }

    public void setAge(int age) {
        this.age= age;
    }

    public int getAge(){
        return this.age;
    }


    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus= maritalStatus;
    }

    public String getMaritalStatus(){
        return this.maritalStatus;
    }

    public void setInterests(String interest) {
        this.interests= interest;
    }

    public String getInterests(){
        return this.interests;
    }
}
