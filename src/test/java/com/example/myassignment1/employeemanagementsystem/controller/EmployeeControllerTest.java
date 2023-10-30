package com.example.myassignment1.employeemanagementsystem.controller;

import com.example.myassignment1.employeemanagementsystem.contract.DepartmentRequest;
import com.example.myassignment1.employeemanagementsystem.contract.DepartmentResponse;
import com.example.myassignment1.employeemanagementsystem.contract.EmployeeRequest;
import com.example.myassignment1.employeemanagementsystem.contract.EmployeeResponse;
import com.example.myassignment1.employeemanagementsystem.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;

    @Test
    public void testAddNewEmployee() throws Exception{
        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("test");
        request.setLastName("user");
        request.setEmail("test@example.com");
        request.setPosition("Software Engineer");
        DepartmentRequest departmentRequest = DepartmentRequest.builder()
                .name("Java")
                .build();
        request.setDepartment(departmentRequest);

        DepartmentResponse depResponse = DepartmentResponse.builder()
        .id(1L)
        .name("Java")
        .managerId(1L)
        .build();

        EmployeeResponse response = new EmployeeResponse();
        response.setId(1L);
        response.setFirstName(request.getFirstName());
        response.setLastName(request.getLastName());
        response.setEmail(request.getEmail());
        response.setPosition(request.getPosition());
        response.setDepartment(depResponse);

        when(employeeService.addNewEmployee(request)).thenReturn(response);

        String path = "/employees";
        mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("test"))
                .andExpect(jsonPath("$.lastName").value("user"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.department.id").value(1L))
                .andExpect(jsonPath("$.department.name").value("Java"))
                .andExpect(jsonPath("$.department.managerId").value(1L))
                .andExpect(jsonPath("$.position").value("Software Engineer"));
    }

    @Test
    public void testListAllEmployees() throws Exception {
        List<EmployeeResponse> expectedResponse = Arrays.asList(
                new EmployeeResponse(1L, "Ravi", "Kumar", "rk@gmail.com",
                        new DepartmentResponse(1L, "Javascript", 1L), "Engineer"),
                new EmployeeResponse(2L, "Shyam", "Mohan", "sm@gmail.com",
                        new DepartmentResponse(2L, "Python", 2L), "Tester")
        );

        when(employeeService.listAllEmployees()).thenReturn(expectedResponse);
        String path = "/employees";
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testFindEmployeeById() throws Exception{
        Long employeeId = 1L;
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setId(employeeId);
        employeeResponse.setFirstName("Ishan");
        employeeResponse.setLastName("Kumar");
        employeeResponse.setEmail("ik@gmail.com");
        employeeResponse.setPosition("Admin");

        when(employeeService.findEmployeeById(employeeId)).thenReturn(employeeResponse);

        mockMvc.perform(get("/employees/" + employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employeeId))
                .andExpect(jsonPath("$.firstName").value("Ishan"))
                .andExpect(jsonPath("$.lastName").value("Kumar"))
                .andExpect(jsonPath("$.email").value("ik@gmail.com"))
                .andExpect(jsonPath("$.position").value("Admin"));
    }

    @Test
    public void testUpdateEmployeeById() throws Exception{
        Long employeeId = 1L;
        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("test");
        request.setLastName("user");
        request.setEmail("test@gmail.com");
        request.setPosition("HR");

        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setName("Admin");
        request.setDepartment(departmentRequest);

        EmployeeResponse response = new EmployeeResponse();
        response.setId(employeeId);
        response.setFirstName("test");
        response.setLastName("user");
        response.setEmail("test@gmail.com");
        response.setPosition("HR");

        when(employeeService.updateEmployeeById(employeeId, request)).thenReturn(response);

        mockMvc.perform(put("/employees/" + employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void testDeleteAnEmployeeById() throws Exception{
        Long employeeId = 1L;

        when(employeeService.deleteAnEmployeeById(employeeId)).thenReturn("Employee deleted successfully.");

        mockMvc.perform(delete("/employees/" + employeeId))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully."));
    }

}
