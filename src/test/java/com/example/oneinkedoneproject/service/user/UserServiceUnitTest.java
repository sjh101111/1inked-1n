package com.example.oneinkedoneproject.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordRepository passwordRepository;

	@BeforeEach
	void init(){

	}

	@Test
	@DisplayName("유저 회원 가입 테스트")
	void signupTest(){

	}

}
