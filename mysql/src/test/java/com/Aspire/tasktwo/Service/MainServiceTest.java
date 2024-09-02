package com.Aspire.tasktwo.Service;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.Aspire.tasktwo.Model.Employee;
import com.Aspire.tasktwo.Model.EmployeeDetailsResponse;
import com.Aspire.tasktwo.Model.EmployeeResponse;
import com.Aspire.tasktwo.Model.ManagerResponse;
import com.Aspire.tasktwo.Model.UpdateEmployee;
import com.Aspire.tasktwo.Repository.EmployeeRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



public class MainServiceTest {
    @InjectMocks
    private MainService mainService;

    @Mock
    private EmployeeRepo employeeRepo;
    private Employee mockEmployee;
    private Employee mockCurrentManager;
    private Employee mockNewManager;
    private Employee mockEmployee1;
    private Employee mockEmployee2;
    private Employee mockManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // String dateTimeString = "2024-06-28T12:57:59";
        // LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);

        LocalDateTime dateTime = LocalDateTime.of(2020, 6, 28, 12, 57, 59);

        mockEmployee = new Employee(101, "John Doe", "Associate", "john.doe@example.com", "Sales", "1234567890", "Location", 1, dateTime);
        mockCurrentManager = new Employee(1, "ManSales", "Account Manager", "man@example.com", "Sales", "1234567890", "Location", 0, dateTime);
        mockNewManager = new Employee(2, "ManDelivery", "Account Manager", "man@example.com", "Delivery", "1234567890", "Location", 0, dateTime);

        mockEmployee1 = new Employee(101, "John Doe", "Associate", "john.doe@example.com", "Sales", "1234567890", "Office", 1, dateTime);
        mockEmployee2 = new Employee(102, "Jane Smith", "Associate", "jane.smith@example.com", "Sales", "0987654321", "Office", 1, dateTime);
        mockManager = new Employee(1, "Manager Name", "Account Manager", "manager@example.com", "Sales", "1234567890", "Office", 0, dateTime);
    }
    String dateTimeString = "2024-06-28T12:57:59";
    LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);

     
    
        
        

    @Test
    public void testPostData_AddingEmployees() {
        
        dateTimeString="2024-06-28T12:57:59";
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        Employee validEmployee = new Employee(101,"John Doe","Associate","john.doe@example.com","Sales","1234567890","Location",1,dateTime);

        List<Employee> employees = Collections.singletonList(validEmployee);

        when(employeeRepo.existsById(101)).thenReturn(false);
        when(employeeRepo.existsByDepartmentAndDesignation("Sales", "Account Manager")).thenReturn(false);
        when(employeeRepo.findByDesignationAndDepartment("Account Manager", "Sales")).thenReturn(Optional.empty());

        // When
        ResponseEntity<Map<String, String>> response = mainService.postData(employees);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("employee_101");
        assertThat(response.getBody()).doesNotContainKey("error_101");
    }



    @Test
    public void testPostData_ValidateEmployees(){

        
        
        //LocalDateTime dateTime=null;
        Employee invalidEmployee = new Employee(0,"","invalid designation","invalid email","invalid department","invalid mobile","",0,null);

        List<Employee> employees=Collections.singletonList(invalidEmployee);

        ResponseEntity<Map<String, String>> response = mainService.postData(employees);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("error");
        assertThat(response.getBody()).containsValue("Employee ID must be a positive number., Name cannot be null or empty., Designation can only be 'Associate' or 'Account Manager'., Invalid email format., Department must be one of the following: Delivery, Sales, QA, Engineering, or BA., Mobile number must be a 10-digit number., Location cannot be null or empty., Date of joining cannot be null., Manager ID must be 0 if designation is 'Account Manager' or a valid ID otherwise.");

    }


    @Test
    public void testPostData_DuplicateEmployeeId() {
        // Given
        dateTimeString="2024-06-28T12:57:59";
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        Employee duplicateEmployee = new Employee(101,"John Doe","Associate","john.doe@example.com","Sales","1234567890","Location",1,dateTime);

        List<Employee> employees = Collections.singletonList(duplicateEmployee);

        when(employeeRepo.existsById(101)).thenReturn(true);

        // When
        ResponseEntity<Map<String, String>> response = mainService.postData(employees);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("error_101", "Employee ID 101 already exists.");
    }

    @Test
    public void testPostData_DepartmentAlreadyHasManager() {
        // Given
        dateTimeString="2024-06-28T12:57:59";
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        Employee newEmployee = new Employee(101,"John Doe","Account Manager","john.doe@example.com","Sales","1234567890","Location",0,dateTime);

    
        List<Employee> employees = Collections.singletonList(newEmployee);

        when(employeeRepo.existsById(101)).thenReturn(false);
        when(employeeRepo.existsByDepartmentAndDesignation("Sales", "Account Manager")).thenReturn(true);

        // When
        ResponseEntity<Map<String, String>> response = mainService.postData(employees);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("error_Sales", "Department Sales already has a manager.");
    }

    @Test
    public void testPostData_ManagerIdValidation() {
        // Given
        dateTimeString="2024-06-28T12:57:59";
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        Employee newEmployee = new Employee(103,"John Doe","Associate","john.doe@example.com","Sales","1234567890","Location",1,dateTime);

        List<Employee> employees = Collections.singletonList(newEmployee);

        Employee existingManager = new Employee();
        existingManager.setEmployeeId(2);
        existingManager.setDesignation("Account Manager");
        existingManager.setDepartment("Sales");

        when(employeeRepo.existsById(103)).thenReturn(false);
        when(employeeRepo.existsByDepartmentAndDesignation("Sales", "Account Manager")).thenReturn(false);
        when(employeeRepo.findByDesignationAndDepartment("Account Manager", "Sales")).thenReturn(Optional.of(existingManager));

        // When
        ResponseEntity<Map<String, String>> response = mainService.postData(employees);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("error_Sales", "Department Sales have the manager ID 2");
    }
    @Test
    public void testPostData_MobileValidation() {
        // Given
        dateTimeString="2024-06-28T12:57:59";
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        Employee newEmployee = new Employee(103,"John Doe","Associate","john.doe@example.com","Sales","1234567890","Location",1,dateTime);

        List<Employee> employees = Collections.singletonList(newEmployee);

        Employee existingMobile = new Employee();
        existingMobile.setEmployeeId(102);
        existingMobile.setMobile("1234567890");


        when(employeeRepo.existsById(103)).thenReturn(false);
        when(employeeRepo.existsByDepartmentAndDesignation("Sales", "Account Manager")).thenReturn(false);
        when(employeeRepo.findByDesignationAndDepartment("Account Manager", "Sales")).thenReturn(Optional.empty());
        when(employeeRepo.findByMobile("1234567890")).thenReturn(Optional.of(existingMobile));

        // When
        ResponseEntity<Map<String, String>> response = mainService.postData(employees);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("error_103", "Another employee has already registered with the same mobile number 1234567890");
    }


    @Test
    public void testPostData_EmailValidation() {
        // Given
        dateTimeString="2024-06-28T12:57:59";
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        Employee newEmployee = new Employee(103,"John Doe","Associate","john.doe@example.com","Sales","1234567890","Location",1,dateTime);

        List<Employee> employees = Collections.singletonList(newEmployee);

        Employee existingEmail = new Employee();
        existingEmail.setEmployeeId(102);
        existingEmail.setEmail("john.doe@example.com");


        when(employeeRepo.existsById(103)).thenReturn(false);
        when(employeeRepo.existsByDepartmentAndDesignation("Sales", "Account Manager")).thenReturn(false);
        when(employeeRepo.findByDesignationAndDepartment("Account Manager", "Sales")).thenReturn(Optional.empty());
        when(employeeRepo.findByEmail("john.doe@example.com")).thenReturn(Optional.of(existingEmail));

        // When
        ResponseEntity<Map<String, String>> response = mainService.postData(employees);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("error_103", "Another employee has already registered with the same email id john.doe@example.com");
    }

    @Test
    @DisplayName("xxxxxx")
    public void testPostData_ExceptionHandling() {
        // Given
        dateTimeString="2024-06-28T12:57:59";
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        Employee validEmployee = new Employee(104,"John Doe","Associate","john.doe@example.com","Sales","1234567890","Location",1,dateTime);


        List<Employee> employees = Collections.singletonList(validEmployee);

        when(employeeRepo.existsById(104)).thenReturn(false);
        when(employeeRepo.existsByDepartmentAndDesignation("Sales", "Account Manager")).thenReturn(false);
        when(employeeRepo.findByDesignationAndDepartment("Account Manager", "Sales")).thenReturn(Optional.empty());
        doThrow(new RuntimeException("Database error")).when(employeeRepo).saveAll(anyList());

        // When
        ResponseEntity<Map<String, String>> response = mainService.postData(employees);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).containsEntry("error", "Error adding data to database: Database error");
    }
    @Test
    public void updateDetails_ValidEmployee() {
        UpdateEmployee updateEmployee = new UpdateEmployee(101, 2);
        List<UpdateEmployee> updateEmp = Collections.singletonList(updateEmployee);

        // Create mutable lists for mocks
        List<Employee> employeesManagedByCurrent = new ArrayList<>();
        employeesManagedByCurrent.add(mockEmployee);

        // Mock repository responses
        when(employeeRepo.findById(101)).thenReturn(Optional.of(mockEmployee));
        when(employeeRepo.findById(1)).thenReturn(Optional.of(mockCurrentManager));
        when(employeeRepo.findById(2)).thenReturn(Optional.of(mockNewManager));
        when(employeeRepo.findByManagerId(1)).thenReturn(employeesManagedByCurrent);
        when(employeeRepo.findByManagerId(2)).thenReturn(new ArrayList<>());

        // Call the service method
        ResponseEntity<Map<String, String>> response = mainService.updateEmployeeManagers(updateEmp);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("employee_101");
        assertThat(response.getBody()).containsValue("John Doe's manager has been successfully changed from ManSales to ManDelivery.");
    }

    @Test
    public void updateDetails_NullEmployeeIdAndManagerId() {
        UpdateEmployee updateEmployee = new UpdateEmployee();
        List<UpdateEmployee> updateEmp = Collections.singletonList(updateEmployee);

        // Create mutable lists for mocks
        List<Employee> employeesManagedByCurrent = new ArrayList<>();
        employeesManagedByCurrent.add(mockEmployee);

        // Mock repository responses
        when(employeeRepo.findById(101)).thenReturn(Optional.of(mockEmployee));
        when(employeeRepo.findById(1)).thenReturn(Optional.of(mockCurrentManager));
        when(employeeRepo.findById(2)).thenReturn(Optional.of(mockNewManager));
        when(employeeRepo.findByManagerId(1)).thenReturn(employeesManagedByCurrent);
        when(employeeRepo.findByManagerId(2)).thenReturn(new ArrayList<>());

        // Call the service method
        ResponseEntity<Map<String, String>> response = mainService.updateEmployeeManagers(updateEmp);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("error_0");
        assertThat(response.getBody()).containsValue("Employee ID or Manager ID is missing");
    }

    @Test
    public void updateDetails_EmployeeIdNotExist(){
        UpdateEmployee updateEmployee = new UpdateEmployee(101, 2);
        List<UpdateEmployee> updateEmp = Collections.singletonList(updateEmployee);

        when(employeeRepo.findById(101)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, String>> response = mainService.updateEmployeeManagers(updateEmp);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("error_101");
        assertThat(response.getBody()).containsValue("Employee not found");


    }

    @Test
    public void updateDetails_CurrentManagerNotExist(){
        UpdateEmployee updateEmployee=new UpdateEmployee(101, 2);
        List<UpdateEmployee> updateEmp=Collections.singletonList(updateEmployee);

        when(employeeRepo.findById(101)).thenReturn(Optional.of(mockEmployee));
        when(employeeRepo.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Map<String,String>> response=mainService.updateEmployeeManagers(updateEmp);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("error_101");
        assertThat(response.getBody()).containsValue("Current manager not found");
    }

    @Test
    public void updateDetails_NewManagerNotExist(){
        UpdateEmployee updateEmployee=new UpdateEmployee(101, 2);
        List<UpdateEmployee> updateEmp=Collections.singletonList(updateEmployee);

        when(employeeRepo.findById(101)).thenReturn(Optional.of(mockEmployee));
        when(employeeRepo.findById(1)).thenReturn(Optional.of(mockCurrentManager));
        when(employeeRepo.findById(2)).thenReturn(Optional.empty());

        ResponseEntity<Map<String,String>> response=mainService.updateEmployeeManagers(updateEmp);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("error_101");
        assertThat(response.getBody()).containsValue("New manager not found");
    }

    @Test
    public void updateDetails_CurrentManagerAndNewManagerSame(){
        UpdateEmployee updateEmployee=new UpdateEmployee(101, 1);
        List<UpdateEmployee> updateEmp=Collections.singletonList(updateEmployee);

        when(employeeRepo.findById(101)).thenReturn(Optional.of(mockEmployee));
        when(employeeRepo.findById(1)).thenReturn(Optional.of(mockCurrentManager));
        

        ResponseEntity<Map<String,String>> response=mainService.updateEmployeeManagers(updateEmp);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("error_101");
        assertThat(response.getBody()).containsValue("New manager is same as the current manager");
    }

    

    @Test
    public void updateDetails_EmployeeNotInCurrentManagerList(){
        UpdateEmployee updateEmployee=new UpdateEmployee(101, 2);
        List<UpdateEmployee> updateEmp=Collections.singletonList(updateEmployee);

        Employee employee=new Employee();
        List<Employee> employeesManagedByCurrent = new ArrayList<>();
        employeesManagedByCurrent.add(employee);


        when(employeeRepo.findById(101)).thenReturn(Optional.of(mockEmployee));
        when(employeeRepo.findById(1)).thenReturn(Optional.of(mockCurrentManager));
        when(employeeRepo.findById(2)).thenReturn(Optional.of(mockNewManager));
        when(employeeRepo.findByManagerId(1)).thenReturn(employeesManagedByCurrent);

        ResponseEntity<Map<String,String>> response=mainService.updateEmployeeManagers(updateEmp);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("error_101");
        assertThat(response.getBody()).containsValue("Employee is not in the current manager's list");

    }

    @Test
    public void getEmployeeDetails_ValidData() {
        List<Employee> employees = Arrays.asList(mockEmployee1, mockEmployee2, mockManager);

        when(employeeRepo.findAll()).thenReturn(employees);
        when(employeeRepo.findById(1)).thenReturn(Optional.of(mockManager));

        ResponseEntity<EmployeeDetailsResponse> response = mainService.getEmployeeDetails(1, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        //@SuppressWarnings("unchecked")
        EmployeeDetailsResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("Successfully fetched");

       // @SuppressWarnings("unchecked")
        ManagerResponse detailsList = responseBody.getDetails();
        assertThat(detailsList).isNotNull();

        
        //detailsList.getAccountManager();
        assertThat(detailsList.getAccountManager()).isEqualTo("Manager Name");
        assertThat(detailsList.getDepartment()).isEqualTo("Sales");

       // @SuppressWarnings("unchecked")
        List<EmployeeResponse> employeeList = responseBody.getEmployeeList();
        assertThat(employeeList).hasSize(2);
        assertThat(employeeList).extracting("name").containsExactlyInAnyOrder("John Doe", "Jane Smith");
    }

    @Test
    public void getEmployeeDetails_NoEmployeesFound() {
        when(employeeRepo.findAll()).thenReturn(Collections.emptyList());
        when(employeeRepo.findById(1)).thenReturn(Optional.of(mockManager));

        ResponseEntity<EmployeeDetailsResponse> response = mainService.getEmployeeDetails(1, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        //@SuppressWarnings("unchecked")
        EmployeeDetailsResponse responseBody = response.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("No employee exists with the given credentials");
    }

    @Test
    public void getEmployeeDetails_EmptyResponseWhenNoManagerFound() {
        List<Employee> employees = Arrays.asList(mockEmployee1, mockEmployee2);

        when(employeeRepo.findAll()).thenReturn(employees);
        when(employeeRepo.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<EmployeeDetailsResponse> response = mainService.getEmployeeDetails(1, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        EmployeeDetailsResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("Successfully fetched");

        ManagerResponse detailsList = responseBody.getDetails();
        assertThat(detailsList).isNotNull();

        
        //detailsList.getAccountManager();
        assertThat(detailsList.getAccountManager()).isEqualTo("Unknown");
        assertThat(detailsList.getDepartment()).isEqualTo("Unknown");

       // @SuppressWarnings("unchecked")
        List<EmployeeResponse> employeeList = responseBody.getEmployeeList();
        assertThat(employeeList).hasSize(2);
        assertThat(employeeList).extracting("name").containsExactlyInAnyOrder("John Doe", "Jane Smith");
    }


    @Test
    public void deleteEmployee_ValidEmployee(){

        
        List<Employee> employeesManagedByCurrent = new ArrayList<>();
        

        when(employeeRepo.findByEmployeeId(101)).thenReturn(Optional.of(mockEmployee));
        when(employeeRepo.findByManagerId(101)).thenReturn(employeesManagedByCurrent);


        ResponseEntity<Map<String, Object>> response = mainService.deleteEmployee(101);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("message");
        assertThat(response.getBody()).containsValue("Successfully deleted employee with ID 101");

    }

    @Test
    public void deleteEmployee_InvalidEmployeeId(){
        when(employeeRepo.findByEmployeeId(101)).thenReturn(Optional.empty());

        ResponseEntity<Map<String,Object>> response=mainService.deleteEmployee(101);

        assertEquals(response.getStatusCode().value(),404);
        assertThat(response.getBody()).containsKey("message");
        assertThat(response.getBody()).containsValue("Employee not found");


    }

    @Test
    public void deleteEmployee_EmployeeWithAssociates(){

        Employee mockEmployeeManager = new Employee(1,"test", "Account Manager", "test@example.com", "Sales", "0000000000", "Trivandrum", 0, dateTime);

        List<Employee> employeesManagedByCurrent = new ArrayList<>();
        employeesManagedByCurrent.add(mockEmployee);
        

        when(employeeRepo.findByEmployeeId(1)).thenReturn(Optional.of(mockEmployeeManager));
        when(employeeRepo.findByManagerId(1)).thenReturn(employeesManagedByCurrent);


        ResponseEntity<Map<String, Object>> response = mainService.deleteEmployee(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("message");
        assertThat(response.getBody()).containsValue("Cannot delete employee with subordinates");

    }
}




