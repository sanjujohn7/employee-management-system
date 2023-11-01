package com.example.myassignment1.employeemanagementsystem.contract;

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
public class DepartmentRequest {
    @NotNull(message = "Department name is required")
    @NotEmpty(message = "Department name should not be empty")
    private String name;
}
