package com.suneth.springbootfileupload.repo;

import com.suneth.springbootfileupload.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student, Integer> {
}
