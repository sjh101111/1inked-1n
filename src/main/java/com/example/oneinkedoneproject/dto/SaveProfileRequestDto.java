package com.example.oneinkedoneproject.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveProfileRequestDto {
    private String email;
    //100자 제한
    private String identity;
    //50자 제한
    private String location;
    //2000자 제한
    private String description;
}
