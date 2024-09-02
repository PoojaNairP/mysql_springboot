package com.Aspire.tasktwo.Model;

public class UpdateEmployee {
    int employeeId;
    int managerId;
    public UpdateEmployee(int employeeId, int managerId) {
        this.employeeId = employeeId;
        this.managerId = managerId;
    }
    public UpdateEmployee() {
    }
    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    public int getManagerId() {
        return managerId;
    }
    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }
    
    
}
