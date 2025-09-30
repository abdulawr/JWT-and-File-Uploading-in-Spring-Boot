package com.basit.cz.jwtandfileuploadinginspringboot.entity;


import jakarta.persistence.*;

import java.security.Timestamp;

@Entity
@Table(name = "file_info")
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", length = 255, nullable = false)
    private String fileName;  // System-generated filename

    @Column(name = "original_file_name", length = 255, nullable = false)
    private String originalFileName;  // Original filename

    @Column(name = "content_type", length = 100)
    private String contentType;  // MIME type

    @Column(name = "file_size")
    private Long fileSize;  // File size in bytes

    @Column(name = "file_path", length = 500)
    private String filePath;  // Web-accessible path

    @Column(name = "upload_time", nullable = false, updatable = false)
    private Timestamp uploadTime;  // Upload timestamp

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", referencedColumnName = "id")
    private User uploadedBy;  // Foreign key to users

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    public User getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
