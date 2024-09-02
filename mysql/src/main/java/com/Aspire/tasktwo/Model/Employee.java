package com.Aspire.tasktwo.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
//import java.time.format.DateTimeParseException;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//import java.time.LocalDateTime;
//import java.util.Objects;



// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee")
public class Employee {

    

    @Id
    private Integer employeeId;

    //@NotNull(message = "Name cannot be null")
    private String name;

  //  @Pattern(regexp = "Associate|Account Manager", message = "Designation can only be 'Account Manager' or 'Associate'")
    private String designation;

  //  @Pattern(regexp = "^\\S+@\\S+\\.\\S+$", message = "Invalid email format")
    private String email;

  //  @Pattern(regexp = "Delivery|Sales|QA|Engineering|BA", message = "Department can only be one of the following - Delivery / Sales / QA / Engineering / BA")
    private String department;

 //   @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be a 10-digit number")
    private String mobile;

  //  @NotNull(message = "Location cannot be null")
    private String location;

    private Integer managerId;

  //  @NotNull(message = "Date of joining cannot be null")
    private LocalDateTime dateOfJoining;

    public Employee(Integer employeeId, String name, String designation, String email, String department, String mobile,
            String location, Integer managerId, LocalDateTime dateOfJoining) {
        this.employeeId = employeeId;
        this.name = name;
        this.designation = designation;
        this.email = email;
        this.department = department;
        this.mobile = mobile;
        this.location = location;
        this.managerId = managerId;
        this.dateOfJoining = dateOfJoining;
    }

    public Employee() {
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();

        if (employeeId == null || employeeId <= 0) {
            errors.add("Employee ID must be a positive number.");
        }

        if (name == null || name.trim().isEmpty()) {
            errors.add("Name cannot be null or empty.");
        }

        if (!isDesignationValid()) {
            errors.add("Designation can only be 'Associate' or 'Account Manager'.");
        }

        if (!isEmailValid()) {
            errors.add("Invalid email format.");
        }

        if (!isDepartmentValid()) {
            errors.add("Department must be one of the following: Delivery, Sales, QA, Engineering, or BA.");
        }

        if (!isMobileValid()) {
            errors.add("Mobile number must be a 10-digit number.");
        }

        if (!isLocationValid()) {
            errors.add("Location cannot be null or empty.");
        }

        if (dateOfJoining == null) {
            errors.add("Date of joining cannot be null.");
        }

        if (!isManagerIdValid()) {
            errors.add("Manager ID must be 0 if designation is 'Account Manager' or a valid ID otherwise.");
        }

        System.out.println(errors);
        return errors;
    }



    public boolean isDesignationValid() {
        return "Associate".equalsIgnoreCase(designation) || "Account Manager".equalsIgnoreCase(designation);
    }

    public boolean isEmailValid() {
        return email.matches("^\\S+@\\S+\\.\\S+$");
    }

    public boolean isDepartmentValid() {
        return "Delivery".equalsIgnoreCase(department) || "Sales".equalsIgnoreCase(department) ||
               "QA".equalsIgnoreCase(department) || "Engineering".equalsIgnoreCase(department) ||
               "BA".equalsIgnoreCase(department);
    }

    public boolean isMobileValid() {
        return mobile != null && mobile.matches("^\\d{10}$");
    }

    public boolean isLocationValid() {
        return location != null && !location.trim().isEmpty();
    }


    public boolean isManagerIdValid() {
        if ("Account Manager".equalsIgnoreCase(designation)) {
            return managerId == 0;
        } else {
            return managerId != null && managerId > 0;
        }
    }

    // Method to validate the whole object
    // public boolean isValid() {
    //     return  isDesignationValid() && isEmailValid() &&
    //            isDepartmentValid() && isMobileValid() && isLocationValid() &&
    //            isManagerIdValid();
    // }


    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public LocalDateTime getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDateTime dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", designation='" + designation + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", mobile='" + mobile + '\'' +
                ", location='" + location + '\'' +
                ", managerId=" + managerId +
                ", dateOfJoining='" + dateOfJoining + '\'' +
                '}';
    }

    @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Employee employee = (Employee) o;
    return Objects.equals(employeeId, employee.employeeId);
}

@Override
public int hashCode() {
    return Objects.hash(employeeId);
}

    
    
}

/* 
"name": "ken",
    // designation can only be Account Manager or associate
    "designation": "associate",
    //validation for email
    "email": "ken@aspire.com",
    // detpartement can be (sales/delevery/QA/engineering/BA)
    "departement": "sales",
    // validation for mob number
    "mobile": 0000000000,
    "location": "Trivandrum",
    // manager id can be 0 if the designation is manager else it should be a valid manager id 
    "managerId": 1,
    "dateofJoining": "2024-06-28T12:57:59.447+00:00"


    // designation can only be Account Manager or associate
    //validation for email
    // detpartement can be (sales/delevery/QA/engineering/BA)
    // validation for mob number
    // manager id can be 0 if the designation is manager else it should be a valid manager id 


*/
