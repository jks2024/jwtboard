package com.human.jwtboard.service;

// 🔴 1. 본인 프로젝트의 Member 엔티티 위치를 정확히 맞춰서 임포트해야 합니다.
import com.human.jwtboard.entity.Member;
import com.human.jwtboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

// 🔴 2. 스프링 시큐리티 권한 및 유저 객체 관련 임포트 추가
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    // Spring Security가 로그인 처리 중 자동으로 호출
    // username 파라미터 = LoginReqDto의 email
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(
                        username + " 을 DB에서 찾을 수 없습니다"));
    }

    private UserDetails createUserDetails(Member member) {
        GrantedAuthority authority =
                new SimpleGrantedAuthority(member.getAuthority().toString());

        // ★ username 자리에 memberId를 넣어야 SecurityUtil에서 꺼낼 수 있음
        return new User(
                String.valueOf(member.getId()), // getName() = memberId
                member.getPassword(),           // BCrypt 해시값 → Spring Security가 비교
                Collections.singleton(authority)
        );
    }
}
