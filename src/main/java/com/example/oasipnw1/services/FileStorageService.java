package com.example.oasipnw1.services;

import com.example.oasipnw1.entites.Event;
import com.example.oasipnw1.exception.FileStorageException;
import com.example.oasipnw1.exception.MyFileNotFoundException;
import com.example.oasipnw1.properties.FileStorageProperties;
import com.example.oasipnw1.repository.EventRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;
    private final EventRepository eventRepository;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties, EventRepository eventRepository) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.eventRepository = eventRepository;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file , Integer id) throws IOException {

//      Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

//      call id -> file
        Path getpathbyid = Paths.get(String.valueOf(this.fileStorageLocation)+ "/" + id);

//      create New Dircertoty
        Files.createDirectories(getpathbyid);

        System.out.println(fileName);
        System.out.println(getpathbyid);

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

//          Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = getpathbyid.resolve(fileName);
//          see targetLocation file
            System.out.println(targetLocation);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName , Integer id) {
        try {
            Path filePath = this.fileStorageLocation.resolve(id.toString()).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public Path getPathFile(Integer id) {
        Path pathbyid = Paths.get(this.fileStorageLocation + "/" + id);
//        return id;
        return pathbyid;
    }
//    delete file
    public void Deletefile(Integer id){
       try{
           FileUtils.deleteDirectory(new File(getPathFile(id).toUri()));
           Event event = eventRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                   id + " does not exist !!!"));
           event.setFileName(null);
           eventRepository.saveAndFlush(event);

       } catch (IOException e) {
           throw new RuntimeException(e);
       }

    }
}