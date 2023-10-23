package com.kakao.kakaopay.kakao.dto;

import lombok.*;

/**
 *
 * 결제 요청 시 카카오에게 받음
 */
@Getter
@Setter
@ToString
public class KakaoReadyResponse {

    private String tid; // 결제 고유 번호 20자
    private String next_redirect_mobile_url; // 요청한 클라이언트가 모바일 웹일 경우 카카오톡 결제 페이지 Redirect URL
    private String next_redirect_pc_url; //요청한 클라이언트가 PC 웹일 경우 카카오톡으로 결제 요청 메시지(TMS)를 보내기 위한 사용자 정보 입력 화면 Redirect URL
    private String created_at; //결제 준비 요청 시간
}