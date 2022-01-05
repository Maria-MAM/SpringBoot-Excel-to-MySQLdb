package com.springbootexcel.springbootuploadexcelfiles.repository;

import com.springbootexcel.springbootuploadexcelfiles.model.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {

}
