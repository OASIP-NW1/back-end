package com.example.oasipnw1.controller;

import com.example.oasipnw1.services.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
@RestController
@RequestMapping("/api/downloadFile")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private  FileController fileController;

//    @GetMapping("/{id}/{fileCode}")
    @GetMapping("/{id}/{fileCode}")
//    filecode = namefile download

//    download อยู่ในรูปแบบ opties stream -> media type
    public ResponseEntity<Resource> downloadfile(@PathVariable Integer id , @PathVariable String fileCode) {
    Resource file = fileStorageService.loadFileAsResource(fileCode , id);
    String contentType = "application/octet-stream";
    String headerValue =  "attachment; filename=\"" + file.getFilename() + "\"";
//    pattern downloadfile
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).
               header(HttpHeaders.CONTENT_DISPOSITION , headerValue).body(file);
    }
}

