package com.example.oneinkedoneproject.repository.resume;

import com.example.oneinkedoneproject.domain.Resume;
import com.example.oneinkedoneproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume,String> {
    public List<Resume> findByUser(User user);
}
