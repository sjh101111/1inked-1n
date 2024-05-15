package com.example.oneinkedoneproject.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class SaveProfileRequestDto {
    //100자 제한
    private String identity;
    //50자 제한
    private String location;
    //2000자 제한
    private String description;

    private MultipartFile file;
}
