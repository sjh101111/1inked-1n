package com.example.oneinkedoneproject.service.user;

import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.*;
import com.example.oneinkedoneproject.utils.regxUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
	private final UserRepository userRepository;
	private final PasswordRepository passwordRepository;
	private final PasswordEncoder encoder;

	public static final int MAX_LENGTH_IDENTITY = 100;
	public static final int MAX_LENGTH_LOCATION = 50;
	public static final int MAX_LENGTH_DESCRIPTION = 2000;


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

	//2. 유저 프로필 저장
	//dto와 file은 multipart/form-data로 분리해서 받는다.
	//RequestPart 어노테이션 사용
	@Transactional
	public User saveProfile(SaveProfileRequestDto requestDto){
		User user = null;

		//1. 이메일 유효성 검증
		// 정규식 테스트로 스킵
		if(!regxUtils.emailRegxCheck(requestDto.getEmail())){
			throw new IllegalArgumentException("email is invalid");
		}

		//2. 이메일로 user를 찾았을 때 실제 존재하는 유저인지 확인
		user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() ->new IllegalArgumentException("not found email matched user"));

		//3.identity 글자수 제한 확인
		if(requestDto.getIdentity().length() > MAX_LENGTH_IDENTITY){
			throw new IllegalArgumentException("identity max length exceed");
		}

		//4. location 글자수 제한 확인
		if(requestDto.getLocation().length() > MAX_LENGTH_LOCATION){
			throw new IllegalArgumentException("location max length exceed");
		}

		//5. description 글자수 제한 확인
		if(requestDto.getDescription().length() > MAX_LENGTH_DESCRIPTION){
			throw new IllegalArgumentException("description max length exceed");
		}

		user.updateIdentity(requestDto.getIdentity());
		user.updateLocation(requestDto.getLocation());
		user.updateDescription(requestDto.getDescription());
		try{
			user.updateImage(requestDto.getFile().getBytes());
		}catch (IOException e){
			//nginx같은 웹서버에서 커버..?
			throw new IllegalArgumentException("file doesn't validate");
		}

		return user;
	}


	//3. 유저 비밀번호 변경
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

	//4. 회원 탈퇴
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

	//5. 회원 가입
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

	//6. 유저 사진 업로드
	@Transactional
	public User uploadImage(UploadUserImageRequestDto request){
		String userEmail = request.getEmail();
		byte[] image = null;

		try{
			//파일 상태를 어떻게 확인하지?
			if(request.getFile() != null){
				image = request.getFile().getBytes();

				//1. 유저 존재하는지 확인
				User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("not found user"));

				//2. 존재한다면, UserEntity 수정
				user.updateImage(image);

				return user;
			}else{
				throw new IllegalArgumentException();
			}
		}catch (IOException e){
			throw new IllegalArgumentException("file is unacceptable");
		}
	}

	//7. userEmail duplication check
	public boolean emailDupCheck(String email){
		return userRepository.existsByEmail(email);
	}
}
