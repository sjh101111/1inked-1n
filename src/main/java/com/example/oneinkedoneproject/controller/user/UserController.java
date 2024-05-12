package com.example.oneinkedoneproject.controller.user;

import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.user.*;
import com.example.oneinkedoneproject.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "유저 API")
@Slf4j
public class UserController {
    private final UserService userService;

    //1. 유저 프로필 조회
    @GetMapping("/api/user")
    public ResponseEntity<FindUserResponseDto> findUser(@AuthenticationPrincipal User user){
        //혹여 모를 정보이 stale 경계위한 find
        User returnUser = userService.findUser(new FindUserRequestDto(user.getEmail()));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new FindUserResponseDto(returnUser));
    }

    @GetMapping("/api/user/{email}")
    public ResponseEntity<FindUserResponseDto> findAnotherUser(@PathVariable("email") String email){
        User user = userService.findUser(new FindUserRequestDto(email));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new FindUserResponseDto(user));
    }


    //2. 유저 프로필 저장
    @PostMapping("/api/profile")
    public ResponseEntity<String> saveProfile(@ModelAttribute SaveProfileRequestDto request, @AuthenticationPrincipal User user){
        User returnUser = userService.saveProfile(request, user);

        if(returnUser == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("저장에 실패했습니다.");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("정상적으로 처리되었습니다.");
    }

    //3. 유저 비밀번호 변경
    @PostMapping("/api/password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDto request){
        User user = userService.changePassword(request);

        if(user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("변경에 실패하였습니다.");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("변경에 성공했습니다.");
    }

    //4. 회원 탈퇴
    @PostMapping("/api/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawUserRequestDto request, @AuthenticationPrincipal User authUser){
        User user = userService.withDraw(request, authUser);

        if(user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("변경에 실패하였습니다.");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("변경에 성공했습니다.");
    }

    //5. 회원 가입
    @PutMapping("/api/user")
    public ResponseEntity<String> signup(@RequestBody SignupUserRequestDto request){
        User user = userService.signUp(request);

       if(user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("회원가입에 실패하였습니다.");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("회원가입에 성공했습니다.");
    }



    //6. 유저 사진 업로드
    @PostMapping("/api/user/image")
    public ResponseEntity<String> uploadImage(@ModelAttribute UploadUserImageRequestDto request, @AuthenticationPrincipal User authUser){
        User user = userService.uploadImage(request, authUser);

       if(user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("유저 사진 업로드에 실패하였습니다.");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("유저 사진 업로드에 성공했습니다.");
    }

}
