package com.example.finalinternship.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if(file !=null && file.getSize()>0) {
            Path path = Paths.get(uploadDir, file.getOriginalFilename());
            Files.write(path, file.getBytes());
            return path.toString();
        }
        return null;
    }

    public boolean deleteFile(String urlFile) {
        File file = new File(String.valueOf(urlFile));
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

}
