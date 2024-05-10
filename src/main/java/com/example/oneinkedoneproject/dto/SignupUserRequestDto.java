package com.example.oneinkedoneproject.dto;

import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupUserRequestDto {
	private String realName;
	private String email;
	private String password;
	private String passwordQuestionId;
	private String passwordQuestionAnswer;

	public User toEntity(PasswordQuestion pwdQuestion, String encodePassword){
		return	User.builder()
			.id(GenerateIdUtils.generateUserId())
			.realname(realName)
			.email(email)
			.password(encodePassword)
			.passwordQuestion(pwdQuestion)
			.passwordAnswer(passwordQuestionAnswer)
			.grade(Grade.ROLE_BASIC)
			.withdraw(false)
			.build();
	}
}
