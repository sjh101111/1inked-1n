package com.example.oneinkedoneproject.controller;

import com.example.oneinkedoneproject.dto.auth.ErrorResult;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, BadRequestException.class})
    public ResponseEntity<ErrorResult> badRequestExHandle(Exception e) {
        log.error("[exceptionHandle] badRequestExHandle", e);
        return new ResponseEntity<ErrorResult>( new ErrorResult("잘못된 입력", e.getMessage()),HttpStatus.BAD_REQUEST) ;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResult> runtimeExHandle(RuntimeException e) {
        log.error("[exceptionHandle] RuntimeExHandle", e);
        return new ResponseEntity<ErrorResult>( new ErrorResult("잘못된 사용자", e.getMessage()),HttpStatus.BAD_REQUEST) ;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResult> NotFoundExHandle(EntityNotFoundException e) {
        log.error("[exceptionHandle] EntityNotFoundExHandle", e);
        return new ResponseEntity<ErrorResult>(  new ErrorResult("존재하지 않는 데이터", e.getMessage()),HttpStatus.BAD_REQUEST) ;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResult> AccessDeniedExHandle(AccessDeniedException e) {
        log.error("[exceptionHandle] AccessDeniedExHandle", e);
        return new ResponseEntity<ErrorResult>( new ErrorResult("권한 오류", "접근 권한이 없습니다."),HttpStatus.BAD_REQUEST) ;
    }
}