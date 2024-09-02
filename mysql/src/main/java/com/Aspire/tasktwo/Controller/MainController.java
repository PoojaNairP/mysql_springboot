package com.Aspire.tasktwo.Controller;

import java.util.List;
import java.util.Map;

import com.Aspire.tasktwo.Model.Employee;
import com.Aspire.tasktwo.Model.EmployeeDetailsResponse;
import com.Aspire.tasktwo.Model.UpdateEmployee;
import com.Aspire.tasktwo.Service.MainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class MainController {

    @Autowired
    private final MainService mainService;

    // @Autowired
    // private PostService postService;
    // private PutService putService;
    // private GetService getService;
    // private DeleteService deleteService;

    public MainController(MainService mainService){
        this.mainService=mainService;
    }

    // public MainController(PostService postService,PutService putService, GetService getService, DeleteService deleteService){
    //     this.postService=postService;
    //     this.putService=putService;
    //     this.getService=getService;
    //     this.deleteService=deleteService;
    // }
    

    @PostMapping("addData")
    public ResponseEntity<Map<String,String>> addData(@RequestBody @Valid List<Employee> employees) {
        return mainService.postData(employees);
    }

    // @PutMapping("/update-manager")
    // public ResponseEntity<Map<String, String>> updateEmployeeManager(@RequestBody Map<String, Integer> requestBody) {
    //     int employeeId = requestBody.get("employeeId");
    //     int newManagerId = requestBody.get("managerId");
    //     return putService.updateEmployeeManagers(employeeId, newManagerId);
    // }

    @PutMapping("/update-managers")
    public ResponseEntity<Map<String, String>> updateEmployeeManagers(@RequestBody List<UpdateEmployee> employeeManagerPairs) {
        // Pass the list of employee-manager pairs to the service
        return mainService.updateEmployeeManagers(employeeManagerPairs);
    }

    @GetMapping("/get-employees")
    public ResponseEntity<EmployeeDetailsResponse> getEmployeeDetails(@RequestParam(defaultValue = "0")int managerId, @RequestParam(defaultValue = "-1")int yearsOfExperience) {
        // Pass the list of employee-manager pairs to the service
        return mainService.getEmployeeDetails(managerId,yearsOfExperience);
    }

    @DeleteMapping("/deleteEmployee")
    public ResponseEntity<Map<String, Object>> deleteEmployee(@RequestParam int employeeId) {
        return mainService.deleteEmployee(employeeId);
    }


    
}
