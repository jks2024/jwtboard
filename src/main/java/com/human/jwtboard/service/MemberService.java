package com.human.jwtboard.service;

import com.human.jwtboard.dto.response.MemberResDto;
import com.human.jwtboard.entity.Member;
import com.human.jwtboard.exception.CustomException;
import com.human.jwtboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // 생성자를 통한 의존성 주입을 간단하게 표기하도록 해줌
@Transactional (readOnly = true)  // 읽기 전용으로 선언하면 영속성 컨텍스트의 변경 감지를 비활성화 (성능 향상)
public class MemberService {
    private final MemberRepository memberRepository;

    // 전체 조회 : Entity -> DTO 변환 (스트림 사용)
    public List<MemberResDto> findAll() {
        return memberRepository.findAll()
                .stream() // 스트림 생성
                .map(MemberResDto::of) // map은 입력 받은 갯수 만큼의 데이터를 변환해서 반환
                .toList();
    }

    // 개별 조회 : 없으면 CustomException 발생
    public MemberResDto findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다. id=" + id));
        return MemberResDto.of(member);
    }
    // 이메일로 개별 회원 조회


}
