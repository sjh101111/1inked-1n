package com.example.oneinkedoneproject.dto;

import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupUserRequestDto {
	private String userName;
	private String email;
	private String password;
	private String passwordQuestionId;
	private String passwordQuestionAnswer;

	public User toEntity(){
		return	User.builder()
			.id(GenerateIdUtils.generateUserId())
			.realname(userName)
			.email(email)
			.password(password)
			.build();
	}
}
