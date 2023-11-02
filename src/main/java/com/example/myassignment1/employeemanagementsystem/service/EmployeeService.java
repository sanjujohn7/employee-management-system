package com.example.myassignment1.employeemanagementsystem.service;

import com.example.myassignment1.employeemanagementsystem.contract.DepartmentResponse;
import com.example.myassignment1.employeemanagementsystem.contract.EmployeeResponse;
import com.example.myassignment1.employeemanagementsystem.contract.EmployeeRequest;
import com.example.myassignment1.employeemanagementsystem.exception.DeleteNotSuccessException;
import com.example.myassignment1.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.myassignment1.employeemanagementsystem.model.Department;
import com.example.myassignment1.employeemanagementsystem.model.DepartmentName;
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
            DepartmentName requestedDepName =
                    DepartmentName.valueOf(employeeRequest.getDepartment().getName().trim().toUpperCase());

            if (requestedDepName != null){
                Optional<Department> optionalDepartment =
                        departmentRepository.findByName(requestedDepName);

                if (optionalDepartment.isPresent()) {
                    department = optionalDepartment.get();
                }else{
                    department = Department.builder()
                            .name(requestedDepName)
                            .build();
                    department = departmentRepository.save(department);
                }
            }else{
                throw new IllegalArgumentException("Department name is invalid. The valid ones are 'ADMIN', 'HR','STAFF', 'MEMBERS' !");
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
                                .name(newAddedEmployee.getDepartment().getName().toString())
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
                                        .name(employee.getDepartment().getName().toString())
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
                                .name(employee.getDepartment().getName().toString())
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
            DepartmentName depNameToUpdate = DepartmentName.valueOf(employeeRequest.getDepartment().getName().trim().toUpperCase());
          department = Department.builder()
                    .id(employeeToUpdate.getDepartment().getId())
                    .name(depNameToUpdate)
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
                                .name(updatedEmployee.getDepartment().getName().toString())
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
