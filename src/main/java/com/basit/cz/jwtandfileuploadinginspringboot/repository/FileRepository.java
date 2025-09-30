package com.basit.cz.jwtandfileuploadinginspringboot.repository;

import com.basit.cz.jwtandfileuploadinginspringboot.entity.FileInfo;
import com.basit.cz.jwtandfileuploadinginspringboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileInfo, Long> {

    List<FileInfo> findByUploadedBy(User uploadedBy);  // To list all files uploaded by a user

    Optional<FileInfo> findById(Long id);  // Find a file by its ID
}

