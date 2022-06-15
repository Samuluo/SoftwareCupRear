package com.example.demo.service.impl;

import com.example.demo.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMM");


    // 上传图片
    @Override
    public String upload(MultipartFile file, String type) throws IOException {
        String map = storeFile(file, type);
        return map;
    }

    private static String storeFile(MultipartFile file, String type) throws IOException {
        try {
            String filePath = "static/" + type + "/uploads";
            File upload = new File(filePath);
            String yearMonth = SDF.format(new Date());//当前年月
            String fileName = file.getOriginalFilename();//文件全名
            System.out.println(fileName);
            File parentDir = new File(upload.getAbsolutePath() + "/" + yearMonth);
            if (!upload.exists()) {
                boolean wasSuccessful = upload.mkdirs();
                if (!wasSuccessful) {
                    System.out.println("was not successful.");
                }
            }
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            String suffix = suffix(fileName);//文件后缀
            String relPath = "/" + yearMonth + "/" + "-" + UUID.randomUUID().toString().replaceAll("-", "") + suffix;
            File fileUp = new File(upload.getAbsolutePath() + relPath);
            file.transferTo(fileUp);
            Map<String, String> map = new HashMap();
            map.put("url", filePath + relPath);
            log.info(relPath);
            return filePath + relPath;
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 后缀名或empty："a.png" => ".png"
     */
    private static String suffix(String fileName) {
        int i = fileName.lastIndexOf('.');
        return i == -1 ? "" : fileName.substring(i);
    }

    // 返回图片流
    @Override
    public ResponseEntity export(File file) {
        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + file.getName());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream")).body(new FileSystemResource(file));
    }

    @Override
    public String download(String file, String type) throws IOException {
        String savePath = "static/" + type + "/uploads";
        String[] split = file.split("/");
        String filename = split[split.length - 1];
        String filePath = savePath + "/" + filename;
        //下载图片
        URL url = new URL(file);
        URLConnection con = url.openConnection();
        con.setConnectTimeout(5000);
        InputStream is = con.getInputStream();
        byte[] bs = new byte[1024];
        int len;
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }
        //OutputStream os = new FileOutputStream(sf.getPath() + "\\" + filename);
        OutputStream os = new FileOutputStream(filePath);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
        return filePath;
    }
}
