package com.basit.cz.jwtandfileuploadinginspringboot.service;

import com.basit.cz.jwtandfileuploadinginspringboot.entity.FileInfo;
import com.basit.cz.jwtandfileuploadinginspringboot.entity.User;
import com.basit.cz.jwtandfileuploadinginspringboot.repository.FileRepository;
import com.basit.cz.jwtandfileuploadinginspringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Timestamp;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository.

    private final Path rootLocation = Paths.get("files");

    public FileDTO uploadFile(MultipartFile file, String username) throws IOException {
        if (file.isEmpty()) {
            throw new FileNotFoundException("No file found to upload");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path destinationFile = rootLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

        // Save file info to the database
        User user = userRepository.findByUsername(username);

        FileInfo savedFile = new FileInfo();
        savedFile.setFileName(fileName);
        savedFile.setOriginalFileName(file.getOriginalFilename());
        savedFile.setContentType(file.getContentType());
        savedFile.setFileSize(file.getSize());
        savedFile.setFilePath(destinationFile.toString());
        savedFile.setUploadTime(new Timestamp(System.currentTimeMillis()));
        savedFile.setUploadedBy(user);

        fileRepository.save(savedFile);

        return new FileDTO(savedFile.getFileName(), savedFile.getContentType(), savedFile.getFileSize());
    }

    public List<FileDTO> listUserFiles(String username) {
        User user = userRepository.findByUsername(username);
        List<FileInfo> files = fileRepository.findByUploadedBy(user);
        return files.stream()
                .map(file -> new FileDTO(file.getFileName(), file.getContentType(), file.getFileSize()))
                .collect(Collectors.toList());
    }

    public FileInfo downloadFile(Long fileId) {
        return fileRepository.findById(fileId).orElseThrow(() -> new FileNotFoundException("File not found"));
    }

    public FileDTO getFileInfo(Long fileId) {
        FileInfo file = fileRepository.findById(fileId).orElseThrow(() -> new FileNotFoundException("File not found"));
        return new FileDTO(file.getFileName(), file.getContentType(), file.getFileSize());
    }

    public void deleteFile(Long fileId) throws IOException {
        FileInfo file = fileRepository.findById(fileId).orElseThrow(() -> new FileNotFoundException("File not found"));
        Files.deleteIfExists(Paths.get(file.getFilePath()));
        fileRepository.delete(file);
    }

    public FileInfo viewFile(Long fileId) {
        return fileRepository.findById(fileId).orElseThrow(() -> new FileNotFoundException("File not found"));
    }
}

