package com.medical.imaging.repository;

import com.medical.imaging.entity.ImageUploadTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageUploadTaskRepository extends JpaRepository<ImageUploadTask, String> {
} 