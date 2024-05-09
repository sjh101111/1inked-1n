package com.example.oneinkedoneproject.controller.resume;

import com.example.oneinkedoneproject.domain.Resume;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.resume.AddResumeRequestDto;
import com.example.oneinkedoneproject.dto.resume.ResumeResponseDto;
import com.example.oneinkedoneproject.dto.resume.UpdateResumeRequestDto;
import com.example.oneinkedoneproject.service.resume.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/resume")
public class ResumeApiController {
    private final ResumeService resumeService;
    private ResumeService ResumeService;

    @PostMapping("")
    public ResponseEntity<ResumeResponseDto> saveResume(
            @RequestBody AddResumeRequestDto addResumeRequestDto,
            @AuthenticationPrincipal User user) {
        Resume saved = resumeService.save(addResumeRequestDto, user);

        if (saved == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResumeResponseDto(saved));
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<ResumeResponseDto> getResume(
            @PathVariable String resumeId) {
        Resume byId = resumeService.findById(resumeId);
        if (byId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new ResumeResponseDto(byId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<ResumeResponseDto>> getResumeByUser(
            @AuthenticationPrincipal User user) {
        List<Resume> resumeList = resumeService.findByUser(user);
        List<ResumeResponseDto> resumeResponseDtoList = new ArrayList<>();

        for (Resume resume : resumeList) {
            resumeResponseDtoList.add(new ResumeResponseDto(resume));
        }

        return ResponseEntity.ok(resumeResponseDtoList);
    }

    @PatchMapping("/{resumeId}")
    public ResponseEntity<ResumeResponseDto> updateResume(
            @PathVariable String resumeId,
            @RequestBody UpdateResumeRequestDto updateResumeRequestDto
    ) {
        Resume updated = resumeService.update(resumeId, updateResumeRequestDto);
        if (updated == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new ResumeResponseDto(updated));
    }

    @DeleteMapping("/{resumeId}")
    public ResponseEntity<Void> deleteResume(
            @PathVariable String resumeId
    ) {
        resumeService.delete(resumeId);
        return ResponseEntity.ok().build();
    }
}
