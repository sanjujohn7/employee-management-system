package com.example.myassignment1.employeemanagementsystem.service;

import com.example.myassignment1.employeemanagementsystem.contract.DepartmentResponse;
import com.example.myassignment1.employeemanagementsystem.contract.EmployeeResponse;
import com.example.myassignment1.employeemanagementsystem.contract.EmployeeRequest;
import com.example.myassignment1.employeemanagementsystem.exception.DeleteNotSuccessException;
import com.example.myassignment1.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.myassignment1.employeemanagementsystem.model.Department;
import com.example.myassignment1.employeemanagementsystem.model.Employee;
import com.example.myassignment1.employeemanagementsystem.repository.DepartmentRepository;
import com.example.myassignment1.employeemanagementsystem.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository){
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }
    public EmployeeResponse addNewEmployee(EmployeeRequest employeeRequest) {
        Department department = null;
        if (employeeRequest.getDepartment() != null && employeeRequest.getDepartment().getName() != null){

            Optional<Department> optionalDepartment =
                    departmentRepository.findByNameIgnoreCase(employeeRequest.getDepartment().getName());

            if (optionalDepartment.isPresent()) {
                department = optionalDepartment.get();
            }else{
                department = Department.builder()
                        .name(employeeRequest.getDepartment().getName())
                        .build();
                department = departmentRepository.save(department);
            }
        }
        Employee employee = Employee.builder()
                .firstName(employeeRequest.getFirstName())
                .lastName(employeeRequest.getLastName())
                .email(employeeRequest.getEmail())
                .department(department)
                .position(employeeRequest.getPosition())
                .build();

        Employee newAddedEmployee = employeeRepository.save(employee);

        return EmployeeResponse.builder()
                .id(newAddedEmployee.getId())
                .firstName(newAddedEmployee.getFirstName())
                .lastName(newAddedEmployee.getLastName())
                .email(newAddedEmployee.getEmail())
                .department(newAddedEmployee.getDepartment() != null ?
                        DepartmentResponse.builder()
                                .id(newAddedEmployee.getDepartment().getId())
                                .name(newAddedEmployee.getDepartment().getName())
                                .managerId(newAddedEmployee.getDepartment().getManagerId())
                                .build()
                        : null)
                .position(newAddedEmployee.getPosition())
                .build();
    }

    public List<EmployeeResponse> listAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()){
            throw new EmployeeNotFoundException("No employees found");
        }
        List<EmployeeResponse> responseList = employees.stream()
                .map(employee -> EmployeeResponse.builder()
                        .id(employee.getId())
                        .firstName(employee.getFirstName())
                        .lastName(employee.getLastName())
                        .email(employee.getEmail())
                        .department(employee.getDepartment() != null ?
                                DepartmentResponse.builder()
                                        .id(employee.getDepartment().getId())
                                        .name(employee.getDepartment().getName())
                                        .managerId(employee.getDepartment().getManagerId())
                                        .build()
                                : null)
                        .position(employee.getPosition())
                        .build())
                .collect(Collectors.toList());

        return responseList;
    }


    public EmployeeResponse findEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null){
            throw new EmployeeNotFoundException("Employee not found");
        }
        return EmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .department(employee.getDepartment() != null ?
                        DepartmentResponse.builder()
                                .id(employee.getDepartment().getId())
                                .name(employee.getDepartment().getName())
                                .managerId(employee.getDepartment().getManagerId())
                                .build()
                        : null)
                .position(employee.getPosition())
                .build();
    }


    public EmployeeResponse updateEmployeeById(Long employeeId,EmployeeRequest employeeRequest) {
        Employee employeeToUpdate = employeeRepository.findById(employeeId).orElse(null);
        if (employeeToUpdate == null){
            throw new EmployeeNotFoundException("Employee not found");
        }
        Department department = null;
        if(employeeRequest.getDepartment() != null){
          department = Department.builder()
                    .id(employeeToUpdate.getDepartment().getId())
                    .name(employeeRequest.getDepartment().getName())
                    .managerId(employeeToUpdate.getDepartment().getManagerId())
                    .build();
          department = departmentRepository.save(department);
        }
        employeeToUpdate = Employee.builder()
                .id(employeeToUpdate.getId())
                .firstName(employeeRequest.getFirstName())
                .lastName(employeeRequest.getLastName())
                .email(employeeRequest.getEmail())
                .department(department)
                .position(employeeRequest.getPosition())
                .build();
        Employee updatedEmployee = employeeRepository.save(employeeToUpdate);

        return EmployeeResponse.builder()
                .id(updatedEmployee.getId())
                .firstName(updatedEmployee.getFirstName())
                .lastName(updatedEmployee.getLastName())
                .email(updatedEmployee.getEmail())
                .department(updatedEmployee.getDepartment() != null ?
                        DepartmentResponse.builder()
                                .id(updatedEmployee.getDepartment().getId())
                                .name(updatedEmployee.getDepartment().getName())
                                .managerId(updatedEmployee.getDepartment().getManagerId())
                                .build()
                        : null)
                .position(updatedEmployee.getPosition())
                .build();
    }


    public String deleteAnEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null){
            throw new EmployeeNotFoundException("Employee not found");
        }
        employeeRepository.delete(employee);
        if(employeeRepository.existsById(employeeId)){
            throw new DeleteNotSuccessException("Failed to delete the employee with ID " +employeeId);
        }else{
            return "Successfully deleted the employee with ID " +employeeId;
        }
    }
}
