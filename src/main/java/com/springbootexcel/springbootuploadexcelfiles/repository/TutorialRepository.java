package com.springbootexcel.springbootuploadexcelfiles.repository;

import com.springbootexcel.springbootuploadexcelfiles.model.Tutorial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.List;


public interface TutorialRepository extends PagingAndSortingRepository<Tutorial, Long>{

    Page<Tutorial> findByPublished(boolean published, Pageable pageable);

    Page<Tutorial> findByTitleContaining(String title, Pageable pageable);

    List<Tutorial> findByTitleContaining(String title, Sort sort);
}
