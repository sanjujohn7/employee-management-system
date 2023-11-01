package com.example.myassignment1.employeemanagementsystem.contract;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@AllArgsConstructor
@Builder
public class DepartmentResponse {
    private Long id;
    private String name;
    private Long managerId;
}
