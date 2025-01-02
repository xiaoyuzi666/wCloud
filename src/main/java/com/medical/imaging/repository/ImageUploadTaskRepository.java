package com.medical.imaging.repository;

import com.medical.imaging.entity.ImageUploadTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageUploadTaskRepository extends JpaRepository<ImageUploadTask, String> {
    List<ImageUploadTask> findByStatus(String status);
    List<ImageUploadTask> findByInstanceId(String instanceId);
} 