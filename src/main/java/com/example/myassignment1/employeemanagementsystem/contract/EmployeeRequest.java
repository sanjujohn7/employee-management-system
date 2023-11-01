package com.example.myassignment1.employeemanagementsystem.contract;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequest {
    @NotNull(message = "First name is required")
    @NotEmpty(message = "First name should not be empty")
    private String firstName;

    private String lastName;

    @NotNull(message = "Email is required")
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Department is required")
    @Valid
    private DepartmentRequest department;

    @NotNull(message = "Position is required")
    @NotEmpty(message = "Position should not be empty")
    private String position;
}
