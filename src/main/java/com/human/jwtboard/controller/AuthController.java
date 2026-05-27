package com.human.jwtboard.controller;

import com.human.jwtboard.dto.request.LoginReqDto;
import com.human.jwtboard.dto.request.SignUpReqDto;
import com.human.jwtboard.dto.request.TokenReissueReqDto;
import com.human.jwtboard.dto.response.ApiResponse;
import com.human.jwtboard.dto.response.MemberResDto;
import com.human.jwtboard.dto.response.TokenDto;
import com.human.jwtboard.security.SecurityUtil;
import com.human.jwtboard.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberResDto>> signup(
            @RequestBody @Valid SignUpReqDto requestDto) {
        return ResponseEntity.ok(ApiResponse.ok(authService.signup(requestDto)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenDto>> login(
            @RequestBody @Valid LoginReqDto requestDto) {
        return ResponseEntity.ok(ApiResponse.ok(authService.login(requestDto)));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenDto>> reissue(
            @RequestBody TokenReissueReqDto requestDto) {
        return ResponseEntity.ok(ApiResponse.ok(authService.reissue(requestDto)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        authService.logout(memberId);
        return ResponseEntity.ok(ApiResponse.ok("로그아웃 되었습니다.", null));
    }
}
