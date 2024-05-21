package com.example.oneinkedoneproject.controller.exception;

import com.example.oneinkedoneproject.controller.ExControllerAdvice;
import com.example.oneinkedoneproject.dto.auth.ErrorResult;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExControllerAdviceUnitTest {

    private final ExControllerAdvice exControllerAdvice = new ExControllerAdvice();

    @Test
    public void testIllegalArgumentExHandle() {
        // 예외 객체 생성 (IllegalArgumentException 또는 BadRequestException)
        Exception exception = new IllegalArgumentException("Test exception message");

        // 예외 처리 메서드 호출
        ResponseEntity<ErrorResult> responseEntity = exControllerAdvice.badRequestExHandle(exception);

        // 반환된 ResponseEntity 검증
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("잘못된 입력", responseEntity.getBody().getError());
        assertEquals("Test exception message", responseEntity.getBody().getMessage());
    }

    @Test
    public void testBadRequestExHandle() {
        // 예외 객체 생성 (IllegalArgumentException 또는 BadRequestException)
        Exception exception = new BadRequestException("Test exception message");

        // 예외 처리 메서드 호출
        ResponseEntity<ErrorResult> responseEntity = exControllerAdvice.badRequestExHandle(exception);

        // 반환된 ResponseEntity 검증
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("잘못된 입력", responseEntity.getBody().getError());
        assertEquals("Test exception message", responseEntity.getBody().getMessage());
    }

    @Test
    public void testRuntimeExHandle() {
        // 예외 객체 생성 (IllegalArgumentException 또는 BadRequestException)
        RuntimeException exception = new RuntimeException("Test exception message");

        // 예외 처리 메서드 호출
        ResponseEntity<ErrorResult> responseEntity = exControllerAdvice.runtimeExHandle(exception);

        // 반환된 ResponseEntity 검증
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("내부 서버 오류", responseEntity.getBody().getError());
        assertEquals("Test exception message", responseEntity.getBody().getMessage());
    }

    @Test
    public void testNotFoundExHandle() {
        // 예외 객체 생성 (IllegalArgumentException 또는 BadRequestException)
        EntityNotFoundException exception = new EntityNotFoundException("Test exception message");
        // 예외 처리 메서드 호출
        ResponseEntity<ErrorResult> responseEntity = exControllerAdvice.notFoundExHandle(exception);
        // 반환된 ResponseEntity 검증
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("존재하지 않는 데이터", responseEntity.getBody().getError());
        assertEquals("Test exception message", responseEntity.getBody().getMessage());
    }

    @Test
    public void testAccessDeniedExHandle() {
        // 예외 객체 생성 (IllegalArgumentException 또는 BadRequestException)
        AccessDeniedException exception = new AccessDeniedException("Test exception message");

        // 예외 처리 메서드 호출
        ResponseEntity<ErrorResult> responseEntity = exControllerAdvice.accessDeniedExHandle(exception);

        // 반환된 ResponseEntity 검증
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("권한 오류", responseEntity.getBody().getError());
        assertEquals("Test exception message", responseEntity.getBody().getMessage());
    }
}