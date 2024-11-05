package com.example.codoceanb.uploadfile.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.codoceanb.auth.dto.UserDTO;
import org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadFileServiceImpl implements UploadFileService {
    private static final Logger log = LogManager.getLogger(UploadFileServiceImpl.class);

    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file, String folderName) {
        assert file.getOriginalFilename() != null;
        String publicValue = generatePublicValue(file.getOriginalFilename());
        log.info("publicValue is: {}", publicValue);
        String extension = getFileName(file.getOriginalFilename())[1];
        log.info("extension is: {}", extension);

        File fileUpload = null;
        try {
            fileUpload = convert(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("fileUpload is: {}", fileUpload);

        try {
            // Upload the file to Cloudinary with the specified folder
            cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id", publicValue, "folder", folderName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cleanDisk(fileUpload);

        // Generate the URL including the folder path
        String fileUrl = cloudinary.url().generate(StringUtils.join(folderName, "/", publicValue, ".", extension));
        log.info("Generated URL: {}", fileUrl);

        return fileUrl;
    }


    @Override
    public String deleteImage(String urlImage) {
        // Extract public_id from the URL
        String publicId = extractPublicId(urlImage);

        if (publicId == null) {
            return "Invalid URL format.";
        }

        try {
            // Delete the image by publicId
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            if ("ok".equals(result.get("result"))) {
                return "Image deleted successfully.";
            } else {
                return "Image deletion failed.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to delete image from Cloudinary";
        }
    }

    private String extractPublicId(String urlImage) {
        // Remove the base URL and version to get only the public_id with path
        String[] parts = urlImage.split("/upload/");
        if (parts.length > 1) {
            String publicIdWithExtension = parts[1];
            // Remove the file extension (e.g., .png, .jpg) from the public_id
            return publicIdWithExtension.substring(0, publicIdWithExtension.lastIndexOf('.'));
        } else {
            throw new IllegalArgumentException("Invalid Cloudinary URL format");
        }
    }

    private File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        File convFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()), getFileName(file.getOriginalFilename())[1]));
        try(InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }
        return convFile;
    }

    private void cleanDisk(File file) {
        try {
            log.info("file.toPath(): {}", file.toPath());
            Path filePath = file.toPath();
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Error");
        }
    }

    public String generatePublicValue(String originalName){
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(), "_", fileName);
    }

    public String[] getFileName(String originalName) {
        return originalName.split("\\.");
    }

}