package com.example.oneinkedoneproject.repository;

import com.example.oneinkedoneproject.domain.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume,String> {
}
