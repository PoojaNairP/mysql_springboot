package com.Aspire.tasktwo.Model;


import java.time.LocalDateTime;

public class EmployeeResponse {
    private String name;
    private Integer id;
    private String designation;
    private String department;
    private String email;
    private String mobile;
    private String location;
    private LocalDateTime dateOfJoining;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    // Constructors
    public EmployeeResponse() {}

    public EmployeeResponse(String name, Integer id, String designation, String department, String email,
                            String mobile, String location, LocalDateTime dateOfJoining,
                            LocalDateTime createdTime, LocalDateTime updatedTime) {
        this.name = name;
        this.id = id;
        this.designation = designation;
        this.department = department;
        this.email = email;
        this.mobile = mobile;
        this.location = location;
        this.dateOfJoining = dateOfJoining;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDateTime dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    
}
