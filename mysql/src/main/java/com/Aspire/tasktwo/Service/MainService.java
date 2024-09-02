package com.Aspire.tasktwo.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
// import java.util.Collections;
import java.util.HashMap;
//import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.Aspire.tasktwo.Model.Employee;
import com.Aspire.tasktwo.Model.EmployeeDetailsResponse;
import com.Aspire.tasktwo.Model.EmployeeResponse;
import com.Aspire.tasktwo.Model.ManagerResponse;
import com.Aspire.tasktwo.Model.UpdateEmployee;
// import com.Aspire.tasktwo.Model.EmployeeDetailsResponse;
// import com.Aspire.tasktwo.Model.GetEmployeeResponse;
// import com.Aspire.tasktwo.Model.UpdateEmployee;
import com.Aspire.tasktwo.Repository.EmployeeRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

@Service
public class MainService {

    @Autowired
    EmployeeRepo employeeRepo;


    public ResponseEntity<Map<String, String>> postData(@Valid List<Employee> employees) {
        Map<String, String> errors = new HashMap<>();
        List<Employee> validEmployees = new ArrayList<>();

        for (Employee employee : employees) {

            List<String> validationErrors = employee.validate();
            System.out.println(validationErrors);
            if (!validationErrors.isEmpty()) {
                String combinedErrors = String.join(", ", validationErrors); // Combine errors into a single string
                errors.put("error", combinedErrors); // Add combined errors to the map
                continue; 
            }
            // Check if employeeId already exists
            if (employeeRepo.existsById(employee.getEmployeeId())) {
                errors.put("error_" + employee.getEmployeeId(), "Employee ID " + employee.getEmployeeId() + " already exists.");
                continue; 
            }
            // Check if department already has a manager
            if ("Account Manager".equals(employee.getDesignation())) {
                if (employeeRepo.existsByDepartmentAndDesignation(employee.getDepartment(), "Account Manager")) {
                    errors.put("error_" + employee.getDepartment(), "Department " + employee.getDepartment() + " already has a manager.");
                    continue;
                }
            }
            // Check whether the managerid and department provided matches
            Optional<Employee> optionalEmployee = employeeRepo.findByDesignationAndDepartment("Account Manager", employee.getDepartment());
            if (optionalEmployee.isPresent()) {
                Integer employeeId = optionalEmployee.get().getEmployeeId();
                if (!employee.getManagerId().equals(employeeId)) {
                    errors.put("error_" + employee.getDepartment(), "Department " + employee.getDepartment() + " have the manager ID "+employeeId);
                    continue;
                }
            }
            optionalEmployee=employeeRepo.findByMobile(employee.getMobile());
            if (optionalEmployee.isPresent()) {
                String mobile=optionalEmployee.get().getMobile();
                if (employee.getMobile().equals(mobile)) {
                    errors.put("error_" + employee.getEmployeeId(), "Another employee has already registered with the same mobile number "+mobile);
                    continue;
                }
            }
            optionalEmployee=employeeRepo.findByEmail(employee.getEmail());
            if (optionalEmployee.isPresent()) {
                String email=optionalEmployee.get().getEmail();
                if (employee.getEmail().equals(email)) {
                    errors.put("error_" + employee.getEmployeeId(), "Another employee has already registered with the same email id "+email);
                    continue;
                }
            }
            validEmployees.add(employee);
        }  

        try {
            employeeRepo.saveAll(validEmployees);
            Map<String, String> successResult = new HashMap<>();
            //successResult.put("message", "Data added to database successfully");
            successResult.putAll(validEmployees.stream()
                .collect(Collectors.toMap(
                    emp -> "employee_" + emp.getEmployeeId(),
                    emp -> "Employee ID " + emp.getEmployeeId() + " is saved."
                )));
            if(errors.isEmpty())
                return new ResponseEntity<>(successResult, HttpStatus.OK);
            else{
                errors.putAll(successResult);
                return new ResponseEntity<>(errors,HttpStatus.OK);
            }
        } catch (Exception e) {
            Map<String, String> errorResult = new HashMap<>();
            errorResult.put("error", "Error adding data to database: " + e.getMessage());
            return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map<String, String>> updateEmployeeManagers(List<UpdateEmployee> employeeManagerPairs) {
        Map<String, String> responseMessages = new HashMap<>();
        Map<String, String> failedUpdates = new HashMap<>();
        //List<UpdateEmployee> validEmployees = new ArrayList<>();



        for(UpdateEmployee updateEmployee:employeeManagerPairs){
            //check if no body is given
            Integer employeeId = updateEmployee.getEmployeeId();
            Integer newManagerId = updateEmployee.getManagerId();
            System.out.println(employeeId+" "+newManagerId);
            if (employeeId==0 || newManagerId == 0) {
                failedUpdates.put("error_" + updateEmployee.getEmployeeId(), "Employee ID or Manager ID is missing");
                continue;
            }

            // check if employee is present or not
            Employee employee = employeeRepo.findById(employeeId).orElse(null);
            if (employee == null) {
                failedUpdates.put("error_" + employeeId, "Employee not found");
                continue;
            }

            // Fetch current manager and check if manager is present or not
            Employee currentManager = employeeRepo.findById(employee.getManagerId()).orElse(null);
            if (currentManager == null) {
                failedUpdates.put("error_" + employeeId, "Current manager not found");
                continue;
            }

            // Fetch new manager and check if manager exists or not
            Employee newManager = employeeRepo.findById(newManagerId).orElse(null);
            if (newManager == null) {
                failedUpdates.put("error_" + employeeId, "New manager not found");
                continue;
            }

            if(currentManager.getEmployeeId()==newManager.getEmployeeId()){
                failedUpdates.put("error_" + employeeId, "New manager is same as the current manager");
                continue;
            }

            // Check if employee is in the current manager's list
            List<Employee> employeesManagedByCurrent = employeeRepo.findByManagerId(employee.getManagerId());

            System.out.println("Employees managed by " + currentManager.getName() + ":");
            for (Employee emp : employeesManagedByCurrent) {
                System.out.println(emp);
            }

            if (!employeesManagedByCurrent.contains(employee)) {
                failedUpdates.put("error_" + employeeId, "Employee is not in the current manager's list");
                continue;
            }
        

            // Remove employee from current manager's list
            employeesManagedByCurrent.remove(employee);
            employeeRepo.saveAll(employeesManagedByCurrent);

            // Update employee's managerId and department
            employee.setManagerId(newManagerId);
            employee.setDepartment(newManager.getDepartment());
            employeeRepo.save(employee);

            // Add employee to new manager's list
            List<Employee> employeesManagedByNew = employeeRepo.findEmployeesByManagerId(newManagerId);
            employeesManagedByNew.add(employee);
            employeeRepo.saveAll(employeesManagedByNew);

            // Success message for this employee
            String message = String.format("%s's manager has been successfully changed from %s to %s.",
                    employee.getName(), currentManager.getName(), newManager.getName());
            responseMessages.put("employee_" + employeeId, message);
        }
    

        // Convert failedUpdates to Map<String, String>
        responseMessages.putAll(failedUpdates);
    
        if (!failedUpdates.isEmpty()) {
            return new ResponseEntity<>(responseMessages, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(responseMessages, HttpStatus.OK);
    }
    

    public ResponseEntity<EmployeeDetailsResponse> getEmployeeDetails(int managerId, int yearsOfExperience) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Employee> employees = employeeRepo.findAll();
    
        // Calculate the cutoff datetime for experience filtering
        LocalDateTime experienceCutoffDateTime = yearsOfExperience != -1
                ? currentDateTime.minusYears(yearsOfExperience)
                : null;
    
        // Filter employees based on experience and manager ID
        List<EmployeeResponse> filteredEmployees = employees.stream()
            .filter(employee -> {
                boolean matchesExperience = experienceCutoffDateTime == null
                        || employee.getDateOfJoining().compareTo(experienceCutoffDateTime) <= 0;
                boolean matchesManager = managerId == 0 || employee.getManagerId() == managerId;
                return matchesExperience && matchesManager;
            })
            .map(emp -> new EmployeeResponse(
                emp.getName(),
                emp.getEmployeeId(),
                emp.getDesignation(),
                emp.getDepartment(),
                emp.getEmail(),
                emp.getMobile(),
                emp.getLocation(),
                emp.getDateOfJoining(),
                emp.getDateOfJoining(), // Assuming you have these methods
                emp.getDateOfJoining()  // Assuming you have these methods
            ))
            .collect(Collectors.toList());

            Optional<Employee> managerOptional = employeeRepo.findById(managerId);
        ManagerResponse managerResponse = new ManagerResponse(
            managerOptional.map(Employee::getName).orElse("Unknown"),
            managerOptional.map(Employee::getDepartment).orElse("Unknown"),
            managerOptional.map(Employee::getEmployeeId).orElse(0)
        );
    
        if (filteredEmployees.isEmpty()) {
           // Map<String, Object> errors = new HashMap<>();
           // errors.put("error", "No employee exists with the given credentials");
           EmployeeDetailsResponse errorResponse = new EmployeeDetailsResponse(
            "No employee exists with the given credentials",
            managerResponse,
            filteredEmployees
        );
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    
        // Fetch manager details
        
    
        // Prepare the response
        EmployeeDetailsResponse response = new EmployeeDetailsResponse(
            "Successfully fetched",
            managerResponse,
            filteredEmployees
        );
    
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    












    // public ResponseEntity<Map<String, Object>> getEmployeeDetails(int managerId, int yearsOfExperience) {
    //     LocalDateTime currentDateTime = LocalDateTime.now();
    //     List<Employee> employees = employeeRepo.findAll();
    //     Map<String, Object> errors = new HashMap<>();
 
    //     // Calculate the cutoff datetime for experience filtering
    //     LocalDateTime experienceCutoffDateTime = yearsOfExperience != -1
    //             ? currentDateTime.minusYears(yearsOfExperience)
    //             : null;
 
    //     List<Employee> filteredEmployees = employees.stream()
    //         .filter(employee -> {
    //             // Calculate experience using compareTo
    //             boolean matchesExperience = experienceCutoffDateTime == null
    //                     || employee.getDateOfJoining().compareTo(experienceCutoffDateTime) <= 0;
    //             boolean matchesManager = managerId == 0 || employee.getManagerId() == managerId;
    //             return matchesExperience && matchesManager;
    //         })
    //         .collect(Collectors.toList());
    //         if(filteredEmployees.isEmpty()){
    //             errors.put("error","No employee exists with the given credentials");
    //             return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    //         }
 
    //         Optional<Employee> accountManagerOptional = employeeRepo.findById(managerId);
    //         Integer accountManagerId= accountManagerOptional.map(Employee::getEmployeeId).orElse(0);
    //         String accountManagerName = accountManagerOptional.map(Employee::getName).orElse("Unknown");
    //         String accountManagerDepartment=accountManagerOptional.map(Employee::getDepartment).orElse("Unknown");
 
 
    //         Map<String, Object> response = new LinkedHashMap<>();
    //         response.put("message", "successfully fetched");
 
    //         Map<String, Object> details = new LinkedHashMap<>();
    //         details.put("accountManager", accountManagerName);
    //         details.put("department", accountManagerDepartment);
    //         details.put("id", accountManagerId);
    //         details.put("employeeList", filteredEmployees.stream().map(emp -> {
    //             Map<String, Object> employeeDetails = new LinkedHashMap<>();
    //             employeeDetails.put("name", emp.getName());
    //             employeeDetails.put("id", emp.getEmployeeId());
    //             employeeDetails.put("designation", emp.getDesignation());
    //             employeeDetails.put("department", emp.getDepartment());
    //             employeeDetails.put("email", emp.getEmail());
    //             employeeDetails.put("mobile", emp.getMobile());
    //             employeeDetails.put("location", emp.getLocation());
    //             employeeDetails.put("dateOfJoining", emp.getDateOfJoining());
    //             employeeDetails.put("createdTime", emp.getDateOfJoining());
    //             employeeDetails.put("updatedTime", emp.getDateOfJoining());
    //             return employeeDetails;
    //         }).collect(Collectors.toList()));
 
    //    response.put("details", List.of(details));
 
    //     // Map the list of employees to a response format
    //     // Map<String, Object> response = Map.of(
    //     //     "employees", filteredEmployees.stream().map(emp -> Map.of(
    //     //         "employeeId", emp.getEmployeeId(),
    //     //         "name", emp.getName(),
    //     //         "designation", emp.getDesignation(),
    //     //         "department", emp.getDepartment(),
    //     //         "location", emp.getLocation(),
    //     //         "dateOfJoining", emp.getDateOfJoining()
    //     //     )).collect(Collectors.toList())
    //     // );
 
    //     return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    public ResponseEntity<Map<String, Object>> deleteEmployee(int employeeId) {
        Optional<Employee> employeeOptional = employeeRepo.findByEmployeeId(employeeId);
        if (employeeOptional.isEmpty()) {
            return new ResponseEntity<>(Map.of("message", "Employee not found"), HttpStatus.NOT_FOUND);
        }

        // Check if there are subordinates
        List<Employee> subordinates = employeeRepo.findByManagerId(employeeId);
        if (!subordinates.isEmpty()) {
            return new ResponseEntity<>(Map.of("message", "Cannot delete employee with subordinates"), HttpStatus.BAD_REQUEST);
        }

        // Delete the employee
        employeeRepo.deleteById(employeeId);

        // Return success message
        return new ResponseEntity<>(Map.of("message", "Successfully deleted employee with ID " + employeeId), HttpStatus.OK);
    }
    
}
