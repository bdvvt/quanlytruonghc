package com.example.quanlytruonghoc.models.repositories;

import com.example.quanlytruonghoc.models.data.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ICourseRepository extends JpaRepository<Course,Long> {
    @Query("SELECT c FROM Course c WHERE c.title LIKE concat('%',:search,'%') OR c.description LIKE concat('%',:search,'%')")
    List<Course> findAllBySearch(@Param("search") String search);
}
