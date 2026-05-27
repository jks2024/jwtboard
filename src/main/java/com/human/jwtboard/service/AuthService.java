package com.human.jwtboard.service;

import com.human.jwtboard.dto.request.LoginReqDto;
import com.human.jwtboard.dto.request.SignUpReqDto;
import com.human.jwtboard.dto.request.TokenReissueReqDto;
import com.human.jwtboard.dto.response.MemberResDto;
import com.human.jwtboard.dto.response.TokenDto;
import com.human.jwtboard.entity.Member;
import com.human.jwtboard.entity.RefreshToken;
import com.human.jwtboard.exception.CustomException;
import com.human.jwtboard.repository.MemberRepository;
import com.human.jwtboard.repository.RefreshTokenRepository;
import com.human.jwtboard.security.SecurityUtil;
import com.human.jwtboard.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final CustomUserDetailsService customUserDetailsService;

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
    public TokenDto reissue(TokenReissueReqDto dto) {
        // 1. 만료된 Access Token에서 memberId 추출
        Long memberId = tokenProvider.getMemberIdFromToken(dto.getAccessToken());

        // 2. DB에서 Refresh Token 조회
        RefreshToken savedToken = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));

        // 3. Refresh Token 만료 확인
        if (savedToken.isExpired()) {
            refreshTokenRepository.delete(savedToken);
            throw new CustomException(HttpStatus.UNAUTHORIZED, "세션이 만료되었씁니다. 다시 로그인해주세요.");
        }

        // 4. 전달받은 토큰과 DB 토큰 일치 확인 (탈취 방어)
        if (!savedToken.getTokenValue().equals(dto.getRefreshToken())) {
            refreshTokenRepository.delete(savedToken);
            throw new CustomException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }

        // 5. 새 Access Token 발급
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(memberId.toString());
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        TokenDto newTokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. (선택) Sliding: Refresh Token도 갱신
        savedToken.updateToken(newTokenDto.getRefreshToken(), tokenProvider.getRefreshTokenExpiry());

        return newTokenDto;
    }

    // 로그아웃
    public void logout(Long memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }



}
