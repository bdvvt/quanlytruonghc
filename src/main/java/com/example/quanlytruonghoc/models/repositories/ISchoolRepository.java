package com.example.quanlytruonghoc.models.repositories;

import com.example.quanlytruonghoc.models.data.entities.Role;
import com.example.quanlytruonghoc.models.data.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISchoolRepository extends JpaRepository<School,Long> {
}
