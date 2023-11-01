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
        DepartmentRequest departmentRequest = DepartmentRequest.builder()
                .name("Java")
                .build();
        EmployeeRequest request = EmployeeRequest.builder()
                .firstName("test")
                .lastName("user")
                .email("test@example.com")
                .department(departmentRequest)
                .position("Software Engineer")
                .build();

        DepartmentResponse depResponse = DepartmentResponse.builder()
        .id(1L)
        .name("Java")
        .managerId(1L)
        .build();

        EmployeeResponse response = EmployeeResponse.builder()
                .id(1L)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .department(depResponse)
                .position(request.getPosition())
                .build();

        when(employeeService.addNewEmployee(request)).thenReturn(response);

        String path = "/employees";
        mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated());
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
        EmployeeResponse employeeResponse = EmployeeResponse.builder()
                .id(employeeId)
                .firstName("Ishan")
                .lastName("Kumar")
                .email("ik@gmail.com")
                .position("Admin")
                .build();

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
        DepartmentRequest departmentRequest = DepartmentRequest.builder()
                .name("Admin")
                .build();
        EmployeeRequest request = EmployeeRequest.builder()
                .firstName("test")
                .lastName("user")
                .email("test@gmail.com")
                .department(departmentRequest)
                .position("HR")
                .build();

        EmployeeResponse response = EmployeeResponse.builder()
                .id(employeeId)
                .firstName("test")
                .lastName("user")
                .email("test@gmail.com")
                .position("HR")
                .build();

        when(employeeService.updateEmployeeById(employeeId, request)).thenReturn(response);

        mockMvc.perform(put("/employees/" + employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$").exists());
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
