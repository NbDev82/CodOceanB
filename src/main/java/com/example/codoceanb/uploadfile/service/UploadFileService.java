package com.example.codoceanb.uploadfile.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadFileService {
     String uploadImage(MultipartFile file);
}
