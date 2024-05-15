package com.example.oneinkedoneproject.service.resume;

import com.example.oneinkedoneproject.domain.Resume;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.resume.AddResumeRequestDto;
import com.example.oneinkedoneproject.dto.resume.ResumeResponseDto;
import com.example.oneinkedoneproject.dto.resume.UpdateResumeRequestDto;
import com.example.oneinkedoneproject.repository.resume.ResumeRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public Resume save(AddResumeRequestDto addResumeRequestDto, User user) {

        return resumeRepository.save(addResumeRequestDto.toEntity(user));
    }

    public Resume findById(String id) {
        return resumeRepository.findById(id).orElseThrow();
    }

    public List<Resume> findByUser(User user) {
        return resumeRepository.findByUser(user);
    }

    public List<ResumeResponseDto> findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다.: " + email)
        );

        return resumeRepository.findByUser(user).stream()
                .map(x -> new ResumeResponseDto(x)).toList();
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
