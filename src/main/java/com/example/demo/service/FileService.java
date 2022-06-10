package com.example.demo.service;


import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileService {

    String upload(MultipartFile file, String type) throws IOException;

    ResponseEntity export(File file);
}
