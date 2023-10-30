package com.example.myassignment1.employeemanagementsystem.controller;

import com.example.myassignment1.employeemanagementsystem.contract.EmployeeResponse;
import com.example.myassignment1.employeemanagementsystem.contract.EmployeeRequest;
import com.example.myassignment1.employeemanagementsystem.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    @Autowired
    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @PostMapping()
    public ResponseEntity<EmployeeResponse> addNewEmployee(@Valid @RequestBody EmployeeRequest employeeRequest){
    EmployeeResponse response = employeeService.addNewEmployee(employeeRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

   @GetMapping()
    public ResponseEntity<List<EmployeeResponse>> listAllEmployees(){
        List<EmployeeResponse> response = employeeService.listAllEmployees();
        return ResponseEntity.ok(response);
    }

   @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> findEmployeeById(@PathVariable Long employeeId){
    EmployeeResponse response = employeeService.findEmployeeById(employeeId);
    return ResponseEntity.ok(response);
   }

   @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> updateEmployeeById(@PathVariable Long employeeId, @RequestBody EmployeeRequest employeeRequest){
        EmployeeResponse response = employeeService.updateEmployeeById(employeeId, employeeRequest);
        return ResponseEntity.ok(response);
   }

   @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteAnEmployeeById(@PathVariable Long employeeId){
        String response = employeeService.deleteAnEmployeeById(employeeId);
        return ResponseEntity.ok(response);
   }
}
