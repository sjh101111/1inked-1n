package com.example.oneinkedoneproject.controller.resume;

import com.example.oneinkedoneproject.domain.Resume;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.resume.AddResumeRequestDto;
import com.example.oneinkedoneproject.dto.resume.ResumeResponseDto;
import com.example.oneinkedoneproject.dto.resume.UpdateResumeRequestDto;
import com.example.oneinkedoneproject.service.resume.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Resume", description = "이력서 API")
@RequestMapping("/api/resume")
public class ResumeApiController {
    private final ResumeService resumeService;

    @PostMapping("")
    @Operation(summary = "이력서 생성", description = "이력서를 생성하는 API")
    @Parameters({
            @Parameter(name = "contents", description = "이력서 내용", example = "이력서 내용 예시")
    })
    @ApiResponse(responseCode = "201", description = "요청에 성공하셨습니다.", content = @Content(mediaType = "application/json"))
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
    @Operation(summary = "이력서 조회", description = "이력서 ID로 이력서를 조회하는 API")
    @Parameters({
            @Parameter(name = "resumeId", description = "이력서 ID", example = "resume_{UUID}")
    })
    @ApiResponse(responseCode = "200", description = "요청에 성공하셨습니다.", content = @Content(mediaType = "application/json"))

    public ResponseEntity<ResumeResponseDto> getResume(
            @PathVariable String resumeId) {
        Resume byId = resumeService.findById(resumeId);
        if (byId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new ResumeResponseDto(byId));
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<ResumeResponseDto>> getResumeOfUserPage(@PathVariable String email) throws Exception {
        String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8.toString());
        return ResponseEntity.ok(resumeService.findByEmail(decodedEmail));
    }

    @GetMapping("/user")
    @Operation(summary = "이력서 조회", description = "사용자별로 이력서를 조회하는 API")
    @ApiResponse(responseCode = "200", description = "요청에 성공하셨습니다.", content = @Content(mediaType = "application/json"))
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
    @Operation(summary = "이력서 수정", description = "이력서를 수정하는 API")
    @Parameters({
            @Parameter(name = "resumeId", description = "이력서 ID", example = "resume_{UUID}"),
            @Parameter(name = "contents", description = "수정할 이력서 내용", example = "이력서 내용 예시")
    })
    @ApiResponse(responseCode = "200", description = "요청에 성공하셨습니다.", content = @Content(mediaType = "application/json"))
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
    @Operation(summary = "이력서 삭제", description = "이력서를 삭제하는 API")
    @Parameters({
            @Parameter(name = "resumeId", description = "이력서 ID", example = "resume_{UUID}")
    })
    @ApiResponse(responseCode = "200", description = "요청에 성공하셨습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<Void> deleteResume(
            @PathVariable String resumeId
    ) {
        resumeService.delete(resumeId);
        return ResponseEntity.ok().build();
    }
}
