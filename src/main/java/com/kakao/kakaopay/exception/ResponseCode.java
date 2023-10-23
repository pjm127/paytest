package com.kakao.kakaopay.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    OK(HttpStatus.OK, "성공"),
    USER_ID_DUPLICATE(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    USER_NICKNAME_DUPLICATE(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호를 확인해 주세요."),

    NOT_FOUND_WEBTOON(HttpStatus.NOT_FOUND, "해당 웹툰을 찾을 수 없습니다."),
    PAY_FAILED(HttpStatus.BAD_REQUEST, "결제에 실패하였습니다."),

    PAY_CANCEL(HttpStatus.BAD_REQUEST, "결제가 취소되었습니다."),
    ALREADY_APPROVED(HttpStatus.BAD_REQUEST, "이미 결제가 완료되었습니다.");
    private final HttpStatus httpStatus;
    private final String detail;
}
