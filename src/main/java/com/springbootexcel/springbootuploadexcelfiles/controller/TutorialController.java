package com.springbootexcel.springbootuploadexcelfiles.controller;

import com.springbootexcel.springbootuploadexcelfiles.model.Tutorial;
import com.springbootexcel.springbootuploadexcelfiles.repository.TutorialRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TutorialController {

    private final TutorialRepository tutorialRepository;

    public TutorialController(TutorialRepository tutorialRepository) {
        this.tutorialRepository = tutorialRepository;
    }

    private Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Direction.ASC;
        } else if (direction.equals("desc")) {
            return Direction.DESC;
        }
        return Direction.ASC;
    }

    @GetMapping("/tutorials")
    public ResponseEntity<Map<String, Object>> getAllTutorials(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        try {
            List<Tutorial> tutorials = new ArrayList<Tutorial>();
            Pageable paging = PageRequest.of(page, size);

            Page<Tutorial> pageTutorials;
            if (title == null) {
                pageTutorials = tutorialRepository.findAll(paging);
            } else {
                pageTutorials = tutorialRepository.findByTitleContaining(title, paging);
            }

            tutorials = pageTutorials.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("tutorials", tutorials);
            response.put("currentPage", pageTutorials.getNumber());
            response.put("totalItems", pageTutorials.getTotalElements());
            response.put("totalPages", pageTutorials.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/published")
    public ResponseEntity<Map<String, Object>> findByPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        try {
            List<Tutorial> tutorials = new ArrayList<Tutorial>();
            Pageable paging = PageRequest.of(page, size);

            Page<Tutorial> pageTutorials = tutorialRepository.findByPublished(true, paging);
            tutorials = pageTutorials.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("tutorials", tutorials);
            response.put("currentPage", pageTutorials.getNumber());
            response.put("totalItems", pageTutorials.getTotalElements());
            response.put("totalPages", pageTutorials.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sortedtutorials")
    public ResponseEntity<Map<String, Object>> getAllTutorialsPage(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        try {
            List<Order> orders = new ArrayList<>();

            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] sortCriteria = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(sortCriteria[1]), sortCriteria[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
            }

            List<Tutorial> tutorials = new ArrayList<Tutorial>();
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

            Page<Tutorial> pageTutorials;
            if (title == null) {
                pageTutorials = tutorialRepository.findAll(pagingSort);
            } else {
                pageTutorials = tutorialRepository.findByTitleContaining(title, pagingSort);
            }

            tutorials = pageTutorials.getContent();

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("tutorials", tutorials);
            response.put("currentPage", pageTutorials.getNumber());
            response.put("totalItems", pageTutorials.getTotalElements());
            response.put("totalPages", pageTutorials.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
