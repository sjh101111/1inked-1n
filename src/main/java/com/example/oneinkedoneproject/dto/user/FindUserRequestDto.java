package com.example.oneinkedoneproject.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
public class FindUserRequestDto {
    private String email;
}
