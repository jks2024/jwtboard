package com.human.jwtboard.service;

import com.human.jwtboard.dto.request.LoginReqDto;
import com.human.jwtboard.dto.request.SignUpReqDto;
import com.human.jwtboard.dto.response.MemberResDto;
import com.human.jwtboard.dto.response.TokenDto;
import com.human.jwtboard.entity.Member;
import com.human.jwtboard.entity.RefreshToken;
import com.human.jwtboard.repository.MemberRepository;
import com.human.jwtboard.repository.RefreshTokenRepository;
import com.human.jwtboard.security.SecurityUtil;
import com.human.jwtboard.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.human.jwtboard.security.SecurityUtil;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public MemberResDto signup(SignUpReqDto dto) {
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }
        Member member = dto.toEntity(passwordEncoder); // 암호화 포함
        return MemberResDto.of(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public TokenDto login(LoginReqDto dto) {
        UsernamePasswordAuthenticationToken authToken = dto.toAuthenticationToken();
        Authentication authentication = managerBuilder.getObject().authenticate(authToken);
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // Refresh Token DB 저장
        Long memberId = Long.parseLong(authentication.getName());
        LocalDateTime expiry = tokenProvider.getRefreshTokenExpiry();

        refreshTokenRepository.findByMemberId(memberId)
                .ifPresentOrElse(
                        rt -> rt.updateToken(tokenDto.getRefreshToken(), expiry),
                        () -> refreshTokenRepository.save(
                                RefreshToken.builder()
                                        .memberId(memberId)
                                        .tokenValue(tokenDto.getRefreshToken())
                                        .expiresAt(expiry)
                                        .build())
                );
        return tokenDto;
    }

    // Access Token 재발급


}
