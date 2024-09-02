package com.Aspire.tasktwo.Repository;

import java.time.LocalDateTime;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.Optional;
import java.util.Optional;

//import java.util.List;

import com.Aspire.tasktwo.Model.Employee;
//import com.Aspire.tasktwo.Model.GetEmployeeResponse;
import com.Aspire.tasktwo.Model.EmployeeDetailsResponse;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.mongodb.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {

    boolean existsById(Integer employeeId);

    boolean existsByDepartmentAndDesignation(String department, String string);
    
    Optional<Employee> findByDesignationAndDepartment(String designation, String department);
    
    Optional<Employee> findById(Integer employeeId);

    List<Employee> findByManagerId(Integer managerId);

    List<Employee> findEmployeesByManagerId(Integer managerId);

    Optional<Employee> findByEmployeeId(int employeeId);

    List<Employee> findAll();

    void deleteById(Integer employeeId);

    List<EmployeeDetailsResponse> findByDepartment(String department);

    List<EmployeeDetailsResponse> findByDepartmentAndDateOfJoiningBefore(String department, LocalDateTime dateOfJoining);

    Optional<Employee> findByMobile(String mobile);

    Optional<Employee> findByEmail(String email);
    
}
