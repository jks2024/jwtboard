package com.human.jwtboard.controller;

import com.human.jwtboard.dto.request.LoginReqDto;
import com.human.jwtboard.dto.request.SignUpReqDto;
import com.human.jwtboard.dto.response.MemberResDto;
import com.human.jwtboard.dto.response.TokenDto;
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
    public ResponseEntity<MemberResDto> signup(
            @RequestBody @Valid SignUpReqDto requestDto) {
        return ResponseEntity.ok(authService.signup(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(
            @RequestBody @Valid LoginReqDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }
}
