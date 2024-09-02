package com.Aspire.tasktwo.Model;


//import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

public class EmployeeDetailsResponse {
    private String message;
    private ManagerResponse details;
    private List<EmployeeResponse> employeeList;

    // Constructors
    public EmployeeDetailsResponse() {}

    public EmployeeDetailsResponse(String message, ManagerResponse details, List<EmployeeResponse> employeeList) {
        this.message = message;
        this.details = details;
        this.employeeList = employeeList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ManagerResponse getDetails() {
        return details;
    }

    public void setDetails(ManagerResponse details) {
        this.details = details;
    }

    public List<EmployeeResponse> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<EmployeeResponse> employeeList) {
        this.employeeList = employeeList;
    }

    
}
