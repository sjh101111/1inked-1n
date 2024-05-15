package com.example.oneinkedoneproject.dto.user;

import com.example.oneinkedoneproject.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class FindUserResponseDto {
    private String id;
    private String realName;
    private String email;
    private String identity;
    private String location;
    private String description;
    private byte[] image;
    public FindUserResponseDto(User user){
        this.id = user.getId();
        this.realName = user.getRealname();
        this.email = user.getEmail();
        this.identity = user.getIdentity();
        this.location = user.getLocation();
        this.description = user.getDescription();
        this.image = user.getImage();
    }
}
