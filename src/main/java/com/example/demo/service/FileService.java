package com.example.demo.service;


import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

public interface FileService {

    String upload(MultipartFile file, String type) throws IOException;

    ResponseEntity export(File file);

    String download(String file1, String changeDetection) throws IOException;

    void transform(String filePath, String result_path, String r, String g, String b);
}
