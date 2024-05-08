package com.example.oneinkedoneproject.controller;

import com.example.oneinkedoneproject.dto.auth.ErrorResult;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, BadRequestException.class})
    public ErrorResult badRequestExHandle(Exception e) {
        log.error("[exceptionHandle] badRequestExHandle", e);
        return new ErrorResult("잘못된 입력", e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResult runtimeExHandle(RuntimeException e) {
        log.error("[exceptionHandle] RuntimeExHandle", e);
        return new ErrorResult("잘못된 사용자", e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResult NotFoundExHandle(EntityNotFoundException e) {
        log.error("[exceptionHandle] EntityNotFoundExHandle", e);
        return new ErrorResult("존재하지 않는 데이터", e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResult AccessDeniedExHandle(AccessDeniedException e) {
        log.error("[exceptionHandle] AccessDeniedExHandle", e);
        return new ErrorResult("권한 오류", "접근 권한이 없습니다.");
    }
}