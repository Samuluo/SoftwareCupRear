package com.example.demo.demo.service;


import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface FileService {

    String upload(MultipartFile file, String type) throws IOException;

    ResponseEntity export(File file);
}
