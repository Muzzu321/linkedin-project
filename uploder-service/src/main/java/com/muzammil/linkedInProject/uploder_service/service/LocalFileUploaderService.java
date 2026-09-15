package com.muzammil.linkedInProject.uploder_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileUploaderService implements UploaderService {

    @Value("${local.storage-path:/tmp/linkedin-uploads}")
    private String storagePath;

    @Override
    public String upload(MultipartFile file) {
        try {
            Path uploadDirectory = Paths.get(storagePath);
            Files.createDirectories(uploadDirectory);

            String originalFilename = file.getOriginalFilename();

            String safeFilename = originalFilename == null
                    ? "file"
                    : Paths.get(originalFilename).getFileName().toString();

            String fileName = UUID.randomUUID() + "-" + safeFilename;

            Path destination = uploadDirectory.resolve(fileName);

            Files.write(destination, file.getBytes());

            return "/uploads/file/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to save uploaded file locally", e
            );
        }
    }
}