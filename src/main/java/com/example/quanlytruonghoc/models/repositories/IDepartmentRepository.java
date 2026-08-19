package com.example.quanlytruonghoc.models.repositories;

import com.example.quanlytruonghoc.models.data.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDepartmentRepository extends JpaRepository<Department,Long> {
}
