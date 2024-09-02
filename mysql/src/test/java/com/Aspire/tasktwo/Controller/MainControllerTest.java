package com.Aspire.tasktwo.Controller;

//import com.Aspire.tasktwo.Model.Employee;
import com.Aspire.tasktwo.Model.EmployeeDetailsResponse;
import com.Aspire.tasktwo.Model.EmployeeResponse;
import com.Aspire.tasktwo.Model.ManagerResponse;
// import com.Aspire.tasktwo.Model.EmployeeDetailsResponse;
// import com.Aspire.tasktwo.Model.EmployeeResponse;
// import com.Aspire.tasktwo.Model.ManagerResponse;
import com.Aspire.tasktwo.Model.UpdateEmployee;
import com.Aspire.tasktwo.Service.MainService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.when;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MainService mainService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Initialize ObjectMapper
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddData() throws Exception {
        // Arrange
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        given(mainService.postData(Mockito.anyList())).willReturn(ResponseEntity.ok(response));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/employees/addData")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{\"id\":1,\"name\":\"John Doe\"}]"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateEmployeeManagers() throws Exception {
        // Prepare test data
        List<UpdateEmployee> employeeManagerPairs = List.of(new UpdateEmployee(101, 1), new UpdateEmployee(102, 2));
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("status", "success");

        // Mock the service method
        given(mainService.updateEmployeeManagers(anyList())).willReturn(ResponseEntity.ok(responseMap));

        // Perform the PUT request and verify the results
        mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/update-managers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeManagerPairs)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andDo(MockMvcResultHandlers.print());

        // Verify the service method was called
        //verify(mainService).updateEmployeeManagers(employeeManagerPairs);
    }

    // LocalDateTime dateTime = LocalDateTime.of(2020, 6, 28, 12, 57, 59);

    //     mockEmployee1 = new Employee(101, "John Doe", "Associate", "john.doe@example.com", "Sales", "1234567890", "Office", 1, dateTime);

    @Test
    public void testGetEmployeeDetailsSuccess() throws Exception {
        // Mock data
        EmployeeDetailsResponse response = new EmployeeDetailsResponse(
                "Successfully fetched",
                new ManagerResponse("John Manager", "IT", 101),
                List.of(new EmployeeResponse(
                        "Jane Doe", 1, "Developer", "IT",
                        "jane.doe@example.com", "1234567890",
                        "New York", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now()))
        );

        // Mock service method
        when(mainService.getEmployeeDetails(0, -1)).thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        // Perform the request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/get-employees")
                .param("managerId", "0")
                .param("yearsOfExperience", "-1"))
                .andExpect(status().isOk());
            //    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Successfully fetched"))
            //    .andExpect(MockMvcResultMatchers.jsonPath("$.manager.name").value("John Manager"))
            //    .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].name").value("Jane Doe"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        // Arrange
        Map<String, Object> response = new HashMap<>();
        response.put("status", "deleted");
        given(mainService.deleteEmployee(Mockito.anyInt())).willReturn(ResponseEntity.ok(response));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/deleteEmployee")
                .param("employeeId", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("deleted"))
                .andDo(MockMvcResultHandlers.print());
    }
}


// package com.Aspire.tasktwo.Controller;

// import com.Aspire.tasktwo.Model.Employee;
// import com.Aspire.tasktwo.Model.EmployeeResponse;
// import com.Aspire.tasktwo.Model.ManagerResponse;
// import com.Aspire.tasktwo.Model.EmployeeDetailsResponse;
// import com.Aspire.tasktwo.Service.MainService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import java.time.LocalDateTime;
// import java.util.*;

// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest(MainController.class)
// public class MainControllerTest {

//     @Mock
//     private MainService mainService;

//     @InjectMocks
//     private MainController mainController;

//     private MockMvc mockMvc;

//     @BeforeEach
//     public void setup() {
//         MockitoAnnotations.openMocks(this);
//         mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
//     }

    // @Test
    // public void testGetEmployeeDetails() throws Exception {
    //     // Mock data
    //     LocalDateTime dateOfJoining = LocalDateTime.now().minusYears(3);
    //     Employee employee = new Employee();
    //     employee.setName("John Doe");
    //     employee.setEmployeeId(1);
    //     employee.setDesignation("Developer");
    //     employee.setDepartment("IT");
    //     employee.setEmail("john.doe@example.com");
    //     employee.setMobile("1234567890");
    //     employee.setLocation("New York");
    //     employee.setDateOfJoining(dateOfJoining);
    //     employee.setManagerId(0); // For simplicity, managerId is 0

    //     EmployeeResponse employeeResponse = new EmployeeResponse(
    //             "John Doe", 1, "Developer", "IT",
    //             "john.doe@example.com", "1234567890",
    //             "New York", dateOfJoining, dateOfJoining, dateOfJoining);

    //     ManagerResponse managerResponse = new ManagerResponse("Unknown", "Unknown", 0);

    //     EmployeeDetailsResponse response = new EmployeeDetailsResponse(
    //             "Successfully fetched", managerResponse, List.of(employeeResponse));

    //     // Mock service method
    //     when(mainService.getEmployeeDetails(0, -1)).thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

    //     // Perform the request and verify response
    //     mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/get-employees")
    //             .param("managerId", "0")
    //             .param("yearsOfExperience", "-1"))
    //             .andExpect(status().isOk())
    //             .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Successfully fetched"))
    //             .andExpect(MockMvcResultMatchers.jsonPath("$.manager.name").value("Unknown"))
    //             .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].name").value("John Doe"));
    // }

//     @Test
//     public void testGetEmployeeDetailsNoEmployeesFound() throws Exception {
//         // Mock response for no employees found
//         Map<String, Object> errorResponse = new HashMap<>();
//         errorResponse.put("error", "No employee exists with the given credentials");

//         // Mock service method
//         when(mainService.getEmployeeDetails(0, -1)).thenReturn(new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND));

//         // Perform the request and verify response
//         mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/get-employees")
//                 .param("managerId", "0")
//                 .param("yearsOfExperience", "-1"))
//                 .andExpect(status().isNotFound())
//                 .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("No employee exists with the given credentials"));
//     }
// }

