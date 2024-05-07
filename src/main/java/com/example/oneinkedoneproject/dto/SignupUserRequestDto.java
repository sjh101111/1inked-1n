package com.example.oneinkedoneproject.dto;

import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupUserRequestDto {
	private String realName;
	private String email;
	private String password;
	private String passwordQuestionId;
	private String passwordQuestionAnswer;

	public User toEntity(PasswordQuestion pwdQuestion, String endcodePassword){
		return	User.builder()
			.id(GenerateIdUtils.generateUserId())
			.realname(realName)
			.email(email)
			.password(endcodePassword)
			.passwordQuestion(pwdQuestion)
			.grade(Grade.ROLE_BASIC)
			.withdraw(false)
			.build();
	}
}
