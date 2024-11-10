package com.example.codoceanb.uploadfile.service;

import com.example.codoceanb.auth.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface UploadFileService {
     String uploadImage(MultipartFile file, String folderName);
     String deleteImage(String urlImage);
}
