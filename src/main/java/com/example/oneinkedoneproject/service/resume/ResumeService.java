package com.example.oneinkedoneproject.service.resume;

import com.example.oneinkedoneproject.domain.Resume;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.resume.AddResumeRequestDto;
import com.example.oneinkedoneproject.dto.resume.UpdateResumeRequestDto;
import com.example.oneinkedoneproject.repository.resume.ResumeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository resumeRepository;

    public Resume save(AddResumeRequestDto addResumeRequestDto, User user) {

        return resumeRepository.save(addResumeRequestDto.toEntity(user));
    }

    public Resume findById(String id) {
        return resumeRepository.findById(id).orElseThrow();
    }

    public List<Resume> findByUser(User user) {
        return resumeRepository.findByUser(user);
    }

    @Transactional
    public Resume update(String id, UpdateResumeRequestDto updateResumeRequestDto) {
        Resume resume = resumeRepository.findById(id).orElseThrow();
        resume.updateContents(updateResumeRequestDto.getContents());
        return resume;
    }

    public void delete(String id) {
        resumeRepository.deleteById(id);
    }
}
