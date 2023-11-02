package com.example.myassignment1.employeemanagementsystem.repository;

import com.example.myassignment1.employeemanagementsystem.model.Department;
import com.example.myassignment1.employeemanagementsystem.model.DepartmentName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(DepartmentName name);
}
