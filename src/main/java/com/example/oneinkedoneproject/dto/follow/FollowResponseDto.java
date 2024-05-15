package com.example.oneinkedoneproject.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowResponseDto {
    private String id;

    private String realname;

    private String identity;

    private byte[] image;

    private String email;

    private String userId;
}
