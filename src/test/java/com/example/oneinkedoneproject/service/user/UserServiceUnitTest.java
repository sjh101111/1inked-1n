package com.example.oneinkedoneproject.service.user;

import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.*;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.example.oneinkedoneproject.service.user.UserService.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordRepository passwordRepository;

	@Spy
	private BCryptPasswordEncoder encoder;

	private PasswordQuestion pwdQuestion;

	//email, password 정규식 테스트는 regxUnitTest에서 진행
	//userService Test에서는 단순 오류 케이스로 커버리지만 확인
	@BeforeEach
	void init(){
		encoder = new BCryptPasswordEncoder();
		pwdQuestion = new PasswordQuestion("1", "질문");
	}

	/**
	 * 회원 조회 TEST START
	 */
	@Test
	@DisplayName("유저 정상 조회 케이스")
	void findUserSuccessTest(){
		//given
		String email = "test@naver.com";
		User user = User.builder()
				.id(GenerateIdUtils.generateUserId())
				.realname("익명")
				.passwordQuestion(pwdQuestion)
				.passwordAnswer("aw")
				.email(email)
				.password(encoder.encode("1aw9!wWem23"))
				.withdraw(false)
				.build();
		FindUserRequestDto request = new FindUserRequestDto(email);

		//when
		doReturn(Optional.of(user))
			.when(userRepository)
			.findByEmail(email);

		User returnUser = userService.findUser(request);

		//then
		assertThat(returnUser.getEmail()).isEqualTo(email);
		assertThat(returnUser.getId()).isEqualTo(user.getId());
	}
	@Test
	@DisplayName("유저 조회 이메일이 유효하지 않은 케이스")
	void findUserInvalidEmailTest(){
		//given
		String email = "test";
		FindUserRequestDto request = new FindUserRequestDto(email);

		//when

		//then
		assertThrows(IllegalArgumentException.class, () ->{
			userService.findUser(request);
		});
	}

	/**
	 * 회원 조회 TEST END
	 */

	/**
	 * 유저 프로필 저장 TEST START
	 */

	@Test
	@DisplayName("유저 프로필 저장 성공 테스트")
	void saveProfileSuccessTest(){
		//given
		String email = "test@naver.com";
		String identity = "test";
		String location = "서울";
		String description = "테스트 설명";

		MultipartFile file = new MockMultipartFile("file",
				"test.png",
				"image/png",
				"test".getBytes(StandardCharsets.UTF_8));
		SaveProfileRequestDto requestDto = new SaveProfileRequestDto(email, identity, location, description, file);
		User user = User.builder()
				.id(GenerateIdUtils.generateUserId())
				.realname("익명")
				.passwordQuestion(pwdQuestion)
				.passwordAnswer("aw")
				.email(email)
				.password(encoder.encode("afweifm"))
				.withdraw(false)
				.build();

		//when
		doReturn(Optional.of(user))
			.when(userRepository)
			.findByEmail(email);
		User returnUser = userService.saveProfile(requestDto);

		//then
		assertThat(returnUser.getIdentity()).isEqualTo(identity);
		assertThat(returnUser.getLocation()).isEqualTo(location);
		assertThat(returnUser.getDescription()).isEqualTo(description);
	}

	@Test
	@DisplayName("유저 프로필 저장 identity 길이 제한 초과")
	void saveProfileIdentityLengthExceedTest(){
		//given
		String email = "test@naver.com";
		String exceedIdentity = RandomStringUtils.random(MAX_LENGTH_IDENTITY + 1);
		String location = "서울";
		String description = "테스트 설명";
		MultipartFile file = new MockMultipartFile("file",
				"test.png",
				"image/png",
				"test".getBytes(StandardCharsets.UTF_8));
		SaveProfileRequestDto requestDto = new SaveProfileRequestDto(email, exceedIdentity, location, description, file);

		User user = User.builder()
				.id(GenerateIdUtils.generateUserId())
				.realname("익명")
				.passwordQuestion(pwdQuestion)
				.passwordAnswer("aw")
				.email(email)
				.password(encoder.encode("afweifm"))
				.withdraw(false)
				.build();
		//when
		doReturn(Optional.of(user))
			.when(userRepository)
			.findByEmail(email);

		//then
		assertThrows(IllegalArgumentException.class, () ->{
			userService.saveProfile(requestDto);
		});
	}

	@Test
	@DisplayName("유저 프로필 저장 위치 글자수 제한 초과")
	void saveProfileLocationLengthExceedTest(){
		//given
		String email = "test@naver.com";
		String identity = "test";
		String exceedLocation = RandomStringUtils.random(MAX_LENGTH_LOCATION + 1);
		String description = "테스트 설명";
		MultipartFile file = new MockMultipartFile("file",
				"test.png",
				"image/png",
				"test".getBytes(StandardCharsets.UTF_8));
		SaveProfileRequestDto requestDto = new SaveProfileRequestDto(email, identity, exceedLocation, description, file);

		User user = User.builder()
				.id(GenerateIdUtils.generateUserId())
				.realname("익명")
				.passwordQuestion(pwdQuestion)
				.passwordAnswer("aw")
				.email(email)
				.password(encoder.encode("afweifm"))
				.withdraw(false)
				.build();
		//when
		doReturn(Optional.of(user))
				.when(userRepository)
				.findByEmail(email);

		//then
		assertThrows(IllegalArgumentException.class, () ->{
			userService.saveProfile(requestDto);
		});
	}

	@Test
	@DisplayName("유저 프로필 저장 description 글자수 제한 초과")
	void saveProfileDescriptionLengthExceedTest(){
		//given
		String email = "test@naver.com";
		String identity = "test";
		String location = "서울";
		String exceedDescription = RandomStringUtils.random(MAX_LENGTH_DESCRIPTION + 1);
		String description = "테스트 설명";
		MultipartFile file = new MockMultipartFile("file",
				"test.png",
				"image/png",
				"test".getBytes(StandardCharsets.UTF_8));
		SaveProfileRequestDto requestDto = new SaveProfileRequestDto(email, identity, location, exceedDescription, file);

		User user = User.builder()
				.id(GenerateIdUtils.generateUserId())
				.realname("익명")
				.passwordQuestion(pwdQuestion)
				.passwordAnswer("aw")
				.email(email)
				.password(encoder.encode("afweifm"))
				.withdraw(false)
				.build();

		//when
		doReturn(Optional.of(user))
			.when(userRepository)
			.findByEmail(email);

		//then
		assertThrows(IllegalArgumentException.class, () ->{
			userService.saveProfile(requestDto);
		});
	}

	/**
	 * 유저 프로필 저장 TEST END
	 */

	/**
	 * 회원가입 TEST START
	 */
	@Test
	@DisplayName("유저 회원 가입 정상 기입 테스트")
	void signupSuccessTest(){
		//given
		SignupUserRequestDto request = new SignupUserRequestDto("real", "tes1231t@naver.com", "Dlxogml!135", "1", "dd");
		User returnUser = request.toEntity(pwdQuestion, encoder.encode(request.getPassword()));
		User saveUser;

		//when
		//정상 회원가입이므로 return User 엔티티 반환
		//비밀번호 레포지토리에 findBy로 조회시 반드시 PasswordQuestion 객체 반환
		doReturn(Optional.of(pwdQuestion))
				.when(passwordRepository)
				.findById(any(String.class));

		doReturn(returnUser)
				.when(userRepository)
				.save(any(User.class));
		//회원가입
		saveUser = userService.signUp(request);

		//then
		assertThat(saveUser.getId()).isEqualTo(returnUser.getId());
	}

	@Test
	@DisplayName("회원가입 이메일 정규식 에러 케이스")
	void signUpInvalidEmailTest(){
		//given
		//1. @이후 주소가 존재하지 않는 경우
		SignupUserRequestDto secPrefixEmptyRequest = new SignupUserRequestDto("real", "tes1231t", "Dlxogml!135", "1", "dd");
		//2. prefix에 특수문자가 존재하는경우
		SignupUserRequestDto specialSymbolRequest = new SignupUserRequestDto("real", "tes12!#31t@naver.com", "Dlxogml!135", "1", "dd");


		//when
		//비밀번호 레포지토리에 findBy로 조회시 반드시 PasswordQuestion 객체 반환
		doReturn(Optional.of(pwdQuestion))
				.when(passwordRepository)
				.findById(any(String.class));

		//then
		assertThrows(IllegalArgumentException.class, () ->{
			userService.signUp(secPrefixEmptyRequest);
		});

		assertThrows(IllegalArgumentException.class, () ->{
			userService.signUp(specialSymbolRequest);
		});
	}

	@Test
	@DisplayName("이메일 중복시 Exception 테스트")
	void signUpDupEmailTest(){
		//given
		//email이 중복된다 가정
		String dupEmail = "testtest@naver.com";
		SignupUserRequestDto dupEmailRequest = new SignupUserRequestDto("real", dupEmail, "Dlxogml!135", "1", "dd");
		SignupUserRequestDto normalRequest = new SignupUserRequestDto("real", "awemfiwl@naver.com", "Dlxogml!135", "1", "dd");
		User returnUser = normalRequest.toEntity(pwdQuestion, encoder.encode(normalRequest.getPassword()));

		//when
		//비밀번호 레포지토리에 findBy로 조회시 반드시 PasswordQuestion 객체 반환
		doReturn(Optional.of(pwdQuestion))
				.when(passwordRepository)
				.findById(any(String.class));

		//dupEmail사용시 existsByEmail 무조건 true반환
		lenient().doReturn(true)
			.when(userRepository)
			.existsByEmail(dupEmail);

		doReturn(returnUser)
				.when(userRepository)
				.save(any(User.class));

		//then
		//정상인 경우 잘 저장되지만 email중복인 경우 저장되지 않는다.
		assertThat(userService.signUp(normalRequest).getEmail()).isEqualTo(normalRequest.getEmail());
		assertThrows(IllegalArgumentException.class, () ->{
			userService.signUp(dupEmailRequest);
		});
	}

	@Test
	@DisplayName("비밀번호 찾기 질문 응답이 입력안되면 에러")
	void signUpPasswordQuestionAnswerTest(){
		//given
		SignupUserRequestDto emptyStringRequest = new SignupUserRequestDto("real", "awemfiwl@naver.com", "Dlxogml!135", "1", "");
		SignupUserRequestDto nullStringRequest = new SignupUserRequestDto("real", "awemfiwl@naver.com", "Dlxogml!135", "1", null);

		//when
		//비밀번호 레포지토리에 findBy로 조회시 반드시 PasswordQuestion 객체 반환
		doReturn(Optional.of(pwdQuestion))
				.when(passwordRepository)
				.findById(any(String.class));

		//then
		//빈 스트링, null String은 userRepository.save()까지 가기전에 막힐거니까!
		assertThrows(IllegalArgumentException.class, () ->{
			userService.signUp(emptyStringRequest);
		});
		assertThrows(IllegalArgumentException.class, () ->{
			userService.signUp(nullStringRequest);
		});
	}

	@Test
	@DisplayName("비밀번호 정규식 통과 못하면 에러")
	void signUpInvalidPasswordTest(){
		//given
		SignupUserRequestDto lowerRequest = new SignupUserRequestDto("real", "awemfiwl@naver.com", "dlxomlawofmi", "1", "awefim");
		SignupUserRequestDto upperRequest = new SignupUserRequestDto("real", "awemfiwl@naver.com", "DEFIWAMDLOQ", "1", "awefim");
		SignupUserRequestDto shortRequest = new SignupUserRequestDto("real", "awemfiwl@naver.com", "a!Q1", "1", "awefim");
		SignupUserRequestDto longRequest = new SignupUserRequestDto("real", "awemfiwl@naver.com", "Q1W!femfi141qaodaifmwaofimweoim", "1", "awefim");
		SignupUserRequestDto exceptSpecialSymbolRequest = new SignupUserRequestDto("real", "awemfiwl@naver.com", "1q2w3e4rqiwmd", "1", "awefim");
		SignupUserRequestDto normalRequest = new SignupUserRequestDto("real", "awemfiwl@naver.com", "aofmEmqo!1285", "1", "awefim");

		User user = normalRequest.toEntity(pwdQuestion, encoder.encode(normalRequest.getPassword()));
		//when
		//비밀번호 레포지토리에 findBy로 조회시 반드시 PasswordQuestion 객체 반환
		doReturn(Optional.of(pwdQuestion))
				.when(passwordRepository)
				.findById(any(String.class));

		doReturn(user)
			.when(userRepository)
			.save(any(User.class));

		//then
		assertThrows(IllegalArgumentException.class, () ->{
			userService.signUp(lowerRequest);
		});
		assertThrows(IllegalArgumentException.class, () ->{
			userService.signUp(upperRequest);
		});
		assertThrows(IllegalArgumentException.class, () ->{
			userService.signUp(shortRequest);
		});
		assertThrows(IllegalArgumentException.class, () ->{
			userService.signUp(longRequest);
		});
		assertThrows(IllegalArgumentException.class, () ->{
			userService.signUp(exceptSpecialSymbolRequest);
		});
		assertThat(userService.signUp(normalRequest).getEmail()).isEqualTo(normalRequest.getEmail());
	}

	/**
	 * 회원가입 TEST END
	 */

	/**
	 * 회원탈퇴 TEST START
	 */

	@Test
	@DisplayName("회원 탈퇴 정상 동작 케이스")
	void withDrawSuccessTest(){
		//given
		String email = "tea13st@naver.com";
		String password = "q2imd!qQ23";

		WithdrawUserRequestDto request = new WithdrawUserRequestDto(email, password, true);
		User user = User.builder()
				.id(GenerateIdUtils.generateUserId())
				.realname("익명")
				.passwordQuestion(pwdQuestion)
				.passwordAnswer("aw")
				.email(email)
				.password(encoder.encode(password))
				.withdraw(false)
				.build();

		//when
		doReturn(Optional.of(user))
				.when(userRepository)
				.findByEmail(email);
		User returnUser = userService.withDraw(request);

		//then
		assertThat(returnUser.getId()).isEqualTo(user.getId());
		assertThat(returnUser.getWithdraw()).isTrue();
	}

	@Test
	@DisplayName("withDraw 이메일 정규식 메핑 안되는 경우")
	void withDrawInvalidEmailTest(){
		//given
		String email = "naver.com";
		String password = "q2imd!qQ23";

		WithdrawUserRequestDto request = new WithdrawUserRequestDto(email, password, true);

		//when

		//then
		assertThrows(IllegalArgumentException.class, () ->{
			userService.withDraw(request);
		});
	}

	@Test
	@DisplayName("withdraw 비밀번호 정규식 테스트 실패 케이스")
	void widthDrawInvalidPasswordTest(){
		//given
		String email = "test@naver.com";
		String password = "123qqd";

		WithdrawUserRequestDto request = new WithdrawUserRequestDto(email, password, true);

		//when

		//then
		assertThrows(IllegalArgumentException.class, () ->{
			userService.withDraw(request);
		});
	}

	@Test
	@DisplayName("회원탈퇴를 위해 입력한 비밀번호가 일치하지 않는 경우")
	void withDrawPasswordNotMatchTest(){
		//given
		String email = "test@naver.com";
		String dtoPassword = "1q!w3Rwmfo123";
		String userPassword = "q2imd!qQ23";
		WithdrawUserRequestDto request = new WithdrawUserRequestDto(email, dtoPassword, true);
		User user = User.builder()
				.id(GenerateIdUtils.generateUserId())
				.realname("익명")
				.passwordQuestion(pwdQuestion)
				.passwordAnswer("aw")
				.email(email)
				.password(encoder.encode(userPassword))
				.withdraw(false)
				.build();

		//when
		doReturn(Optional.of(user))
			.when(userRepository)
			.findByEmail(email);

		//then
		assertThrows(IllegalArgumentException.class, () ->{
			userService.withDraw(request);
		});
	}
	/**
	 * 회원탈퇴 TEST END
	 */

	/**
	 * 회원 비밀번호 변경 TEST START
	 */

	@Test
	@DisplayName("비밀번호 변경 정상 수행 케이스")
	void changePasswordSuccessTest(){
		//given
		String email = "test@naver.com";
		String changePassword = "1q2e!qwimfA42";
		String pwdQuestionAnswer = "음오아예";
		ChangePasswordRequestDto request = new ChangePasswordRequestDto(email, pwdQuestion.getId(), pwdQuestionAnswer, changePassword);

		User user = User.builder()
				.id(GenerateIdUtils.generateUserId())
				.realname("익명")
				.passwordQuestion(pwdQuestion)
				.passwordAnswer(pwdQuestionAnswer)
				.email(email)
				.password(encoder.encode("whatever"))
				.withdraw(false)
				.build();
		//when
		//비밀번호 레포지토리에 findBy로 조회시 반드시 PasswordQuestion 객체 반환
		doReturn(true)
				.when(passwordRepository)
				.existsById(any(String.class));

		doReturn(Optional.of(user))
				.when(userRepository)
				.findByEmail(email);
		User returnUser = userService.changePassword(request);

		//then
		assertThat(encoder.matches("whatever", returnUser.getPassword())).isFalse();
	}

	@Test
	@DisplayName("change Password email 정규식 에러")
	void changePasswordInvalidEmailTest(){
		//given
		String email = "test";
		String changePassword = "1q2e!qwimfA42";
		String pwdQuestionAnswer = "음오아예";
		ChangePasswordRequestDto request = new ChangePasswordRequestDto(email, pwdQuestion.getId(), pwdQuestionAnswer, changePassword);

		//when

		//then
		assertThrows(IllegalArgumentException.class, () ->{
			userService.changePassword(request);
		});
	}

	@Test
	@DisplayName("비밀번호 변경 질문이 db에 존재하지 않는 경우")
	void changePasswordQuestionNotFoundTest(){
		//given
		String email = "test@naver.com";
		String changePassword = "1q2e!qwimfA42";
		String pwdQuestionAnswer = "음오아예";
		ChangePasswordRequestDto request = new ChangePasswordRequestDto(email, pwdQuestion.getId(), pwdQuestionAnswer, changePassword);

		//when
		doReturn(false)
			.when(passwordRepository)
			.existsById(any(String.class));

		//then
		assertThrows(IllegalArgumentException.class, () ->{
			userService.changePassword(request);
		});
	}

	@Test
	@DisplayName("비밀번호 질문 응답이 올바르지 않은 경우")
	void changePasswordQuestionAnswerTest(){
		//given
		String email = "test@naver.com";
		String changePassword = "1q2e!qwimfA42";
		String pwdQuestionAnswer = "음오아예";
		ChangePasswordRequestDto request = new ChangePasswordRequestDto(email, pwdQuestion.getId(), pwdQuestionAnswer, changePassword);
		User user = User.builder()
				.id(GenerateIdUtils.generateUserId())
				.realname("익명")
				.passwordQuestion(pwdQuestion)
				.passwordAnswer("huhuhu")
				.email(email)
				.password(encoder.encode(changePassword))
				.withdraw(false)
				.build();

		//when
		doReturn(true)
				.when(passwordRepository)
				.existsById(any(String.class));

		doReturn(Optional.of(user))
			.when(userRepository)
			.findByEmail(email);

		//then
		assertThrows(IllegalArgumentException.class, () ->{
			userService.changePassword(request);
		});
	}

	@Test
	@DisplayName("비밀번호 변경 비밀번호 정규식 에러")
	void changePasswordInvalidPasswordTest(){
		//given
		String email = "test@naver.com";
		String changePassword = "1q2";
		String pwdQuestionAnswer = "음오아예";
		ChangePasswordRequestDto request = new ChangePasswordRequestDto(email, pwdQuestion.getId(), pwdQuestionAnswer, changePassword);
		User user = User.builder()
				.id(GenerateIdUtils.generateUserId())
				.realname("익명")
				.passwordQuestion(pwdQuestion)
				.passwordAnswer(pwdQuestionAnswer)
				.email(email)
				.password(encoder.encode(changePassword))
				.withdraw(false)
				.build();

		//when
		doReturn(true)
				.when(passwordRepository)
				.existsById(any(String.class));

		doReturn(Optional.of(user))
				.when(userRepository)
				.findByEmail(email);

		//then
		assertThrows(IllegalArgumentException.class, () ->{
			userService.changePassword(request);
		});
	}
	/**
	 * 회원 비밀번호 변경 TEST END
	 */

	/**
	 * 유저 사진 업로드 TEST START
	 */
	@Test
	@DisplayName("유저 사진 업로드 정상 동작")
	void uploadImageSuccessTest(){
		//given
		String email = "test@naver.com";
		MultipartFile file = new MockMultipartFile("file",
				"test.png",
				"image/png",
				"test".getBytes(StandardCharsets.UTF_8));
		UploadUserImageRequestDto request = new UploadUserImageRequestDto(email, file);
		User user = User.builder()
				.id(GenerateIdUtils.generateUserId())
				.realname("익명")
				.passwordQuestion(pwdQuestion)
				.passwordAnswer("aw")
				.email(email)
				.password(encoder.encode("1aw9!wWem23"))
				.withdraw(false)
				.build();
		//when
		doReturn(Optional.of(user))
			.when(userRepository)
			.findByEmail(email);
		User returnUser = userService.uploadImage(request);

		//then
		assertThat(returnUser.getImage()).isNotNull();
	}

	@Test
	@DisplayName("이메일로 user entity를 가져오지 못한 경우")
	void uploadImageInvalidEmailTest(){
		//given
		String email = "test@naver.com";
		MultipartFile file = new MockMultipartFile("file",
				"test.png",
				"image/png",
				"test".getBytes(StandardCharsets.UTF_8));
		UploadUserImageRequestDto request = new UploadUserImageRequestDto(email, file);

		//when
		doReturn(Optional.empty())
			.when(userRepository)
			.findByEmail(email);

		//then
		assertThrows(IllegalArgumentException.class, ()->{
			userService.uploadImage(request);
		});
	}

	/**
	 * 유저 사진 업로드 TEST END
	 */
}
