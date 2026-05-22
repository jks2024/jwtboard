package com.human.jwtboard.dto.request;

import com.human.jwtboard.constant.Authority;
import com.human.jwtboard.entity.Member;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpReqDto {
    private String email;
    private String password;
    private String name;
    private Authority authority;

    // DTO -> Entity
    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .authority(authority)
                .build();
    }
}
