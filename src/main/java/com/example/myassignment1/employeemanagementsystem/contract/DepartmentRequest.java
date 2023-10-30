package com.example.myassignment1.employeemanagementsystem.contract;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentRequest {
    @NotNull(message = "Department name is required")
    @NotEmpty(message = "Department name should not be empty")
    private String name;
}
