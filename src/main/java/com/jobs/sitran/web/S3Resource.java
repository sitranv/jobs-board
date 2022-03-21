package com.jobs.sitran.web;

import com.jobs.sitran.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class S3Resource {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/uploadfile")
    public String uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return this.s3Service.uploadFile(file);
    }
}
