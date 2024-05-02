package com.example.oneinkedoneproject.service.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
	private final UserRepository userRepository;
	private final PasswordRepository passwordRepository;
	private final BCryptPasswordEncoder encoder;

	//1. 유저 프로필 조회

	//2. 유저 프로필 수정

	//3. 유저 프로필 저장

	//4. 유저 비밀번호 변경

	//5. 회원 탈퇴

	//6. 회원 가입
	public

	//7. 유저 사진 업로드

	//8. 비밀번호 찾기

	//9. 비밀번호 변경
}
