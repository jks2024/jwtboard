package com.human.jwtboard.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {
    private SecurityUtil() {}  // 인스턴스 생성 방지
    public static Long getCurrentMemberId() {
        final Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            log.error("Security Context에 인증 정보가 없습니다.");
            throw new RuntimeException("인증 정보가 없습니다.");
        }

        try {
            return Long.parseLong(authentication.getName()); // Subject = memberId
        } catch (NumberFormatException e) {
            throw new RuntimeException("잘못된 인증 정보입니다.");
        }
    }
}
