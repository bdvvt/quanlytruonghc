package com.example.quanlytruonghoc.models.repositories;

import com.example.quanlytruonghoc.models.data.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ICourseRepository extends JpaRepository<Course,Long> {

}
