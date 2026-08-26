package com.example.quanlytruonghoc.models.repositories;

import com.example.quanlytruonghoc.models.data.entities.Department;
import com.example.quanlytruonghoc.models.data.entities.School;
import com.example.quanlytruonghoc.models.data.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDepartmentRepository extends JpaRepository<Department,Long> {
    Optional<Department> findByName(String name);

    @Query("SELECT d FROM Department d WHERE d.school = :school ")
    Page<Department> findAllBySchool(@Param("school") School school, Pageable pageable);
}
