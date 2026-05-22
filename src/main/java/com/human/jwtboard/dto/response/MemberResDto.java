package com.human.jwtboard.dto.response;

import com.human.jwtboard.constant.Authority;
import com.human.jwtboard.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResDto {
    private Long      id;
    private String    email;
    private String    name;
    private Authority authority;
    // password 필드 없음 → 응답에서 비밀번호 노출 방지

    public static MemberResDto of(Member member) {
        return MemberResDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .authority(member.getAuthority())
                .build();
    }
}