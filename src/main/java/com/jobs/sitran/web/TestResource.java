package com.jobs.sitran.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jobs.sitran.service.RSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestResource {

    @Autowired
    private RSService rsService;

    @GetMapping("")
    public ResponseEntity<?> getTF() {
        List<String> cvHashtags = Arrays.asList("PHP", "OOP", "JAVA", "LARAVEL", "JAVASCRIPT", "MYSQL", "SPRING",
                "POSTGRESQL", "REACT", "MONGODB", "BOOTSTRAP", "GIT", "HTML/CSS", "SQL", "PHP", "PHP", "BOOTSTRAP", "GIT");
        cvHashtags = Lists.newArrayList(Sets.newHashSet(cvHashtags));

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/cv", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadCV(@RequestParam MultipartFile cv) throws IOException {

//        return ResponseEntity.ok(this.rsService.extractHashtags(cv));
        return ResponseEntity.ok(this.rsService.cosineSimilarity(this.rsService.extractHashtags(cv)));
    }

}
