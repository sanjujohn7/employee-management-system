package com.example.myassignment1.employeemanagementsystem.service;

import com.example.myassignment1.employeemanagementsystem.contract.EmployeeRequest;
import com.example.myassignment1.employeemanagementsystem.contract.EmployeeResponse;
import com.example.myassignment1.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.myassignment1.employeemanagementsystem.model.Department;
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
    public void testAddNewEmployee(){
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("test");
        employeeRequest.setLastName("user");
        employeeRequest.setEmail("test@example.com");
        employeeRequest.setPosition("Manager");
        Department department = Department.builder()
                .name("HR")
                .build();

        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Employee savedEmployee = Employee.builder()
                .id(1L)
                .firstName("test")
                .lastName("user")
                .email("test@example.com")
                .department(department)
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
        assertEquals("HR", employeeResponse.getDepartment().getName());
        assertEquals("Manager", employeeResponse.getPosition());
    }

    @Test
    public void testListAllEmployees(){
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = Employee.builder()
                .id(1L)
                .firstName("test1")
                .lastName("user1")
                .email("test1@gmail.com")
                .position("Engineer")
                .build();
        employees.add(employee1);

        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("test2")
                .lastName("user2")
                .email("test2@gmail.com")
                .position("Developer")
                .build();
        employees.add(employee2);

        when(employeeRepository.findAll()).thenReturn(employees);

        List<EmployeeResponse> responseList = employeeService.listAllEmployees();

        assertEquals(2, responseList.size());
    }

    @Test
    public void testFindEmployeeById(){
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("test")
                .lastName("user")
                .email("test@gmail.com")
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
    public void testFindEmployeeById_NotFound() {

        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.findEmployeeById(1L);
        });
    }

    @Test
    public void testDeleteAnEmployeeById() {
        Employee existingEmployee = Employee.builder()
                .id(1L)
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.existsById(1L)).thenReturn(false);

        String result = employeeService.deleteAnEmployeeById(1L);

        assertEquals("Successfully deleted the employee with ID 1", result);
    }

    @Test
    public void testUpdateEmployeeById(){
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("updatedFirstName");
        employeeRequest.setLastName("updatedLastName");
        employeeRequest.setEmail("updated@example.com");
        employeeRequest.setPosition("updatedPosition");
        Department department = Department.builder()
                .name("HR")
                .build();

        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Employee existingEmployee = Employee.builder()
                .id(1L)
                .firstName("test")
                .lastName("user")
                .email("test@example.com")
                .position("DevOps")
                .department(department)
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));

        Employee updatedEmployee = Employee.builder()
                .id(1L)
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .email("updated@example.com")
                .position("updatedPosition")
                .department(department)
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
}
