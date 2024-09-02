package com.Aspire.tasktwo.Model;

public class ManagerResponse {
    private String accountManager;
    private String department;
    private Integer id;

    // Constructors
    public ManagerResponse() {}

    public ManagerResponse(String accountManager, String department, Integer id) {
        this.accountManager = accountManager;
        this.department = department;
        this.id = id;
    }

    public String getAccountManager() {
        return accountManager;
    }

    public void setAccountManager(String accountManager) {
        this.accountManager = accountManager;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    
}

