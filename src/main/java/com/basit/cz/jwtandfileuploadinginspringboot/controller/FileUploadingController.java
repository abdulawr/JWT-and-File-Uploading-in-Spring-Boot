package com.basit.cz.jwtandfileuploadinginspringboot.controller;

import com.basit.cz.jwtandfileuploadinginspringboot.dto.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileUploadingController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileDTO> uploadFile(@RequestParam("file") MultipartFile file,
                                              @RequestHeader("Authorization") String token) {
        String username = getUsernameFromToken(token);  // Your method to extract username from JWT
        try {
            FileDTO fileDTO = fileService.uploadFile(file, username);
            return ResponseEntity.ok(fileDTO);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/my-files")
    public ResponseEntity<List<FileDTO>> listUserFiles(@RequestHeader("Authorization") String token) {
        String username = getUsernameFromToken(token);
        List<FileDTO> files = fileService.listUserFiles(username);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId, @RequestHeader("Authorization") String token) {
        FileInfo file = fileService.downloadFile(fileId);
        Path filePath = Paths.get(file.getFilePath());
        Resource resource = new FileSystemResource(filePath);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(resource);
    }

    @GetMapping("/view/{fileId}")
    public ResponseEntity<Resource> viewFile(@PathVariable Long fileId, @RequestHeader("Authorization") String token) {
        FileInfo file = fileService.viewFile(fileId);
        Path filePath = Paths.get(file.getFilePath());
        Resource resource = new FileSystemResource(filePath);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(resource);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId, @RequestHeader("Authorization") String token) {
        try {
            fileService.deleteFile(fileId);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/info/{fileId}")
    public ResponseEntity<FileDTO> getFileInfo(@PathVariable Long fileId, @RequestHeader("Authorization") String token) {
        FileDTO fileDTO = fileService.getFileInfo(fileId);
        return ResponseEntity.ok(fileDTO);
    }

    private String getUsernameFromToken(String token) {
        // Implement JWT token extraction logic
        return "extractedUsername"; // Example placeholder
    }

}
