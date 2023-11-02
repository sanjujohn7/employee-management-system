package com.example.myassignment1.employeemanagementsystem.service;

import com.example.myassignment1.employeemanagementsystem.contract.DepartmentRequest;
import com.example.myassignment1.employeemanagementsystem.contract.EmployeeRequest;
import com.example.myassignment1.employeemanagementsystem.contract.EmployeeResponse;
import com.example.myassignment1.employeemanagementsystem.exception.DeleteNotSuccessException;
import com.example.myassignment1.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.myassignment1.employeemanagementsystem.model.Department;
import com.example.myassignment1.employeemanagementsystem.model.DepartmentName;
import com.example.myassignment1.employeemanagementsystem.model.Employee;
import com.example.myassignment1.employeemanagementsystem.repository.DepartmentRepository;
import com.example.myassignment1.employeemanagementsystem.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private DepartmentRepository departmentRepository;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testAddNewEmployee(){
        Department department = null;
        DepartmentRequest departmentRequest = DepartmentRequest.builder()
                .name("JAVA")
                .build();
        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("test")
                .lastName("user")
                .email("test@example.com")
                .department(departmentRequest)
                .position("Manager")
                .build();
        if (employeeRequest.getDepartment() != null && employeeRequest.getDepartment().getName() != null){
            DepartmentName reqDepName = DepartmentName.valueOf("JAVA");
            if (reqDepName != null){
                Optional<Department> optionalDepartment = departmentRepository.findByName(reqDepName);
                if(optionalDepartment.isPresent()){
                    department = optionalDepartment.get();
                }else{
                    department = Department.builder()
                            .name(reqDepName)
                            .build();
                }
            }
        }
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Employee savedEmployee = Employee.builder()
                .id(1L)
                .firstName("test")
                .lastName("user")
                .email("test@example.com")
                .department(departmentRequest != null ? Department.builder()
                        .id(6L)
                        .name(DepartmentName.JAVA)
                        .managerId(9L)
                        .build() : null)
                .position("Manager")
                .build();

        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        EmployeeResponse employeeResponse = employeeService.addNewEmployee(employeeRequest);

        assertNotNull(employeeResponse);
        assertEquals(1L, employeeResponse.getId());
        assertEquals("test", employeeResponse.getFirstName());
        assertEquals("user", employeeResponse.getLastName());
        assertEquals("test@example.com", employeeResponse.getEmail());
        assertNotNull(employeeResponse.getDepartment());
        assertEquals("JAVA", employeeResponse.getDepartment().getName());
        assertEquals("Manager", employeeResponse.getPosition());
    }

    @Test
     void testListAllEmployees(){
        List<Employee> employees = new ArrayList<>();
        DepartmentName depName = DepartmentName.NODEJS;
        Department department1 = Department.builder()
                .id(1L)
                .name(depName)
                .build();
        Employee employee1 = Employee.builder()
                .id(1L)
                .firstName("test1")
                .lastName("user1")
                .email("test1@gmail.com")
                .department(department1 != null ? department1: null)
                .position("Engineer")
                .build();
        employees.add(employee1);
        Department department2 = null;
//                Department.builder()
//                .id(2L)
//                .name(depName)
//                .build();
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("test2")
                .lastName("user2")
                .email("test2@gmail.com")
                .department(department2 != null ? department2 : null)
                .position("Developer")
                .build();
        employees.add(employee2);

        when(employeeRepository.findAll()).thenReturn(employees);

        List<EmployeeResponse> responseList = employeeService.listAllEmployees();

        assertEquals(2, responseList.size());
    }

    @Test
     void testListAllEmployees_throws_Exception(){
        List<Employee> employees = new ArrayList<>();

        when(employeeRepository.findAll()).thenReturn(employees);

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.listAllEmployees();
        });
        assertEquals("No employees found", exception.getMessage());
    }

    @Test
     void testFindEmployeeById(){
        DepartmentName departmentName = DepartmentName.PYTHON;
        Department department = null;
//                Department.builder()
//                .id(5L)
//                .name(departmentName)
//                .managerId(4L)
//                .build();
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("test")
                .lastName("user")
                .email("test@gmail.com")
                .department(department != null ? department : null)
                .position("Manager")
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeResponse employeeResponse = employeeService.findEmployeeById(1L);

        assertNotNull(employeeResponse);
        assertEquals(1L, employeeResponse.getId());
        assertEquals("test", employeeResponse.getFirstName());
        assertEquals("user", employeeResponse.getLastName());
        assertEquals("test@gmail.com", employeeResponse.getEmail());
        assertEquals("Manager", employeeResponse.getPosition());
    }

    @Test
     void testFindEmployeeById_throws_Exception() {

        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.findEmployeeById(1L);
        });
        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
     void testDeleteAnEmployeeById() {
        Employee existingEmployee = Employee.builder()
                .id(1L)
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.existsById(1L)).thenReturn(false);

        String result = employeeService.deleteAnEmployeeById(1L);

        assertEquals("Successfully deleted the employee with ID 1", result);
    }

    @Test
     void testDeleteAnEmployeeById_throws_Exception() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.deleteAnEmployeeById(1L);
        });
        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
     void testDeleteAnEmployeeById_throws_Failure() {
        long employeeId = 1L;
        Employee existingEmployee = Employee.builder()
                .id(employeeId)
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.existsById(1L)).thenReturn(true);

        DeleteNotSuccessException exception = assertThrows(DeleteNotSuccessException.class, () -> {
            employeeService.deleteAnEmployeeById(1L);
        });
        assertEquals("Failed to delete the employee with ID " +employeeId, exception.getMessage());
    }

    @Test
     void testUpdateEmployeeById(){
        DepartmentRequest departmentRequest = DepartmentRequest.builder()
                .name("JAVA")
                .build();
        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .email("updated@example.com")
                .department(departmentRequest)
                .position("updatedPosition")
                .build();
        Department department = null;
        if (employeeRequest.getDepartment() != null){
            DepartmentName departmentName = DepartmentName.REACT;
                  department = Department.builder()
                    .id(1L)
                    .name(departmentName)
                    .managerId(2L)
                    .build();
        }

        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Employee existingEmployee = Employee.builder()
                .id(1L)
                .firstName("test")
                .lastName("user")
                .email("test@example.com")
                .position("DevOps")
                .department(department != null ? department: null)
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));

        Employee updatedEmployee = Employee.builder()
                .id(1L)
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .email("updated@example.com")
                .position("updatedPosition")
                .department(department != null ? department: null)
                .build();

        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        EmployeeResponse employeeResponse = employeeService.updateEmployeeById(1L, employeeRequest);

        assertNotNull(employeeResponse);
        assertEquals(1L, employeeResponse.getId());
        assertEquals("updatedFirstName", employeeResponse.getFirstName());
        assertEquals("updatedLastName", employeeResponse.getLastName());
        assertEquals("updated@example.com", employeeResponse.getEmail());
        assertEquals("updatedPosition", employeeResponse.getPosition());
    }

    @Test
     void testUpdateEmployeeById_throws_Exception(){
        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .email("updated@example.com")
                .position("updatedPosition")
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.updateEmployeeById(1L, employeeRequest);
        });
        assertEquals("Employee not found", exception.getMessage());
    }
}
