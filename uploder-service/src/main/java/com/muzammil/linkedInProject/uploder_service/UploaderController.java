package com.muzammil.linkedInProject.uploder_service;

import com.muzammil.linkedInProject.uploder_service.service.UploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class UploaderController {

    private final UploaderService uploaderService;

    @Value("${local.storage-path:/tmp/linkedin-uploads}")
    private String storagePath;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> uploadFile(
            @RequestPart("file") MultipartFile file) {

        String url = uploaderService.upload(file);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/{fileName}")
    ResponseEntity<Resource> getFile(
            @PathVariable String fileName) throws MalformedURLException {

        Path filePath = Paths.get(storagePath)
                .resolve(fileName)
                .normalize();

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}