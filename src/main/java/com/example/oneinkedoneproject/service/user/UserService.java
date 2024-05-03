package com.example.oneinkedoneproject.service.user;

import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.ChangePasswordRequestDto;
import com.example.oneinkedoneproject.dto.FindUserRequestDto;
import com.example.oneinkedoneproject.dto.SignupUserRequestDto;
import com.example.oneinkedoneproject.dto.WithdrawUserRequestDto;
import com.example.oneinkedoneproject.utils.regxUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
	private final UserRepository userRepository;
	private final PasswordRepository passwordRepository;
	private final BCryptPasswordEncoder encoder;

	//1. 유저 프로필 조회
	public User findUser(FindUserRequestDto requestDto){
		//1. 조회 조건이 뭐지?(이메일)
		User user = null;

		//이메일이 유효한 형태인가?
		if(!regxUtils.emailRegxCheck(requestDto.getEmail())){
			throw new IllegalArgumentException("email is invalid");
		}

		//email이 실제 존재하는 유저인가?
		user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() ->new IllegalArgumentException("not found email matched user"));

		//탈퇴한 유저인지 판단
		//에러로 던질 것이냐, 활동이 정지된 유저다 표기할것인지
//		if(user.getWithdraw()){
//		}

		return user;
	}

	//2. 유저 프로필 수정


	//3. 유저 프로필 저장

	//4. 유저 비밀번호 변경
	// 기능명세서 수정 예정
	@Transactional
	public User changePassword(ChangePasswordRequestDto requestDto){
		User user;
		//이메일 정규식 체크
		if(!regxUtils.emailRegxCheck(requestDto.getEmail())){
			throw new IllegalArgumentException("email is invalid");
		}

		if(!passwordRepository.existsById(requestDto.getPasswordQuestionId())){
			throw new IllegalArgumentException("password question is invalid");
		}

		user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new IllegalArgumentException("email not found"));

		if(!user.getPasswordAnswer().equals(requestDto.getPasswordQuestionAnswer())){
			throw new IllegalArgumentException("password question answer is not matched");
		}

		//새로운 비밀번호 정규식 테스트
		if(!regxUtils.pwdRegxCheck(requestDto.getNewPassword())){
			throw new IllegalArgumentException("password is invalid");
		}

		user.updatePassword(requestDto.getNewPassword());

		return user;
	}

	//5. 회원 탈퇴
	@Transactional
	public User withDraw(WithdrawUserRequestDto requestDto){
		User user;
		//이메일 정규식 체크
		if(!regxUtils.emailRegxCheck(requestDto.getEmail())){
			throw new IllegalArgumentException("email is invalid");
		}

		//비밀번호 정규식 체크가 올바른지 확인
		if(!regxUtils.pwdRegxCheck(requestDto.getPassword())){
			throw new IllegalArgumentException("password is invaild");
		}

		user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new IllegalArgumentException("not found match id by email"));

		//입력한 인자의 비밀번호가 같지 않다면
		//String.equals사용하면 안됨! BCryptEncoder는 같은 평문이여도 다른 값을 반환
		if(!encoder.matches(requestDto.getPassword(), user.getPassword())){
			//자세한 정보 제공하지 않기 위해
			throw new IllegalArgumentException("password is invalid");
		}

		//update 수행 -> 실제 db에서 삭제 x
		user.updateWithdraw(requestDto.getIsWithdraw());

		return user;
	}


	//6. 회원 가입
	@Transactional
	public User signUp(SignupUserRequestDto requestDto){
		String passwordQuestionId = requestDto.getPasswordQuestionId();
		String passwordQuestionAnswer = requestDto.getPasswordQuestionAnswer();
		String encodePassword = null;
		PasswordQuestion pwdQuestion = passwordRepository.findById(passwordQuestionId).orElseThrow(() -> new IllegalArgumentException("not found password Question: " + passwordQuestionId));
		User user = null;

		//이메일 정규식 체크
		if(!regxUtils.emailRegxCheck(requestDto.getEmail())){
			throw new IllegalArgumentException("email is invalid");
		}

		//이메일 중복 여부 체크
		if(emailDupCheck(requestDto.getEmail())){
			throw new IllegalArgumentException("email is duplicate");
		}

		//비밀번호 질문 응답이 비었는지 확인
		if(passwordQuestionAnswer == null || passwordQuestionAnswer.length() == 0){
			throw new IllegalArgumentException("비밀번호 찾기 질문 응답이 입력되지 않음.");
		}

		//비밀번호 정규식 체크가 올바른지 확인
		if(!regxUtils.pwdRegxCheck(requestDto.getPassword())){
			throw new IllegalArgumentException("password is invaild");
		}

		//비밀번호 인코딩
		encodePassword = encoder.encode(requestDto.getPassword());
		user = requestDto.toEntity(pwdQuestion, encodePassword);
		return userRepository.save(user);
	}

	//7. 유저 사진 업로드

	//8. userEmail duplication check
	public boolean emailDupCheck(String email){
		return userRepository.existsByEmail(email);
	}
}
