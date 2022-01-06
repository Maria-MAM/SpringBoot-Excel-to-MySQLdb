package com.springbootexcel.springbootuploadexcelfiles.service;

import com.springbootexcel.springbootuploadexcelfiles.helper.ExcelHelper;
import com.springbootexcel.springbootuploadexcelfiles.model.Tutorial;
import com.springbootexcel.springbootuploadexcelfiles.repository.TutorialRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {
    private final TutorialRepository repository;

    public ExcelService(TutorialRepository repository) {
        this.repository = repository;
    }

    public void save(MultipartFile file) {
        try {
            List<Tutorial> tutorials = ExcelHelper.excelToTutorials(file.getInputStream());
            repository.saveAll(tutorials);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public List<Tutorial> getAllTutorials() {
        List<Tutorial> tutList = new ArrayList<>();
        repository.findAll().iterator().forEachRemaining(tutList::add);
        return tutList;
    }
}
