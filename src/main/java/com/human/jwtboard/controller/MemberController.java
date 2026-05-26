package com.human.jwtboard.controller;

import com.human.jwtboard.dto.response.ApiResponse;
import com.human.jwtboard.dto.response.MemberResDto;
import com.human.jwtboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 회원 전체 조회 : GetMapping
    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberResDto>>> findAll() {
        List<MemberResDto> data = memberService.findAll();
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    // 회원 개별 조회 : GetMapping /{id}
    // 없는 id 요청 시 ->
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResDto>> findById(Long id) {
        MemberResDto data = memberService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

}
