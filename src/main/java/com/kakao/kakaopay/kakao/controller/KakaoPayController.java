package com.kakao.kakaopay.kakao.controller;


import com.kakao.kakaopay.kakao.dto.KakaoApproveResponse;
import com.kakao.kakaopay.kakao.dto.KakaoCancelResponse;
import com.kakao.kakaopay.kakao.dto.KakaoReadyRequest;
import com.kakao.kakaopay.kakao.dto.KakaoReadyResponse;
import com.kakao.kakaopay.exception.CustomException;
import com.kakao.kakaopay.kakao.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kakao.kakaopay.exception.ResponseCode.PAY_CANCEL;
import static com.kakao.kakaopay.exception.ResponseCode.PAY_FAILED;

@RestController
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    /**
     * 결제요청
     */
/*    @PostMapping("/ready")
    public ResponseEntity<KakaoReadyResponse> readyToKakaoPay() {

        return ResponseEntity.ok(kakaoPayService.kakaoPayReady());
    }*/
    @PostMapping("/ready")
    public ResponseEntity<KakaoReadyResponse> readyToKakaoPay(@RequestBody KakaoReadyRequest kakaoReadyRequest) {

        KakaoReadyResponse kakaoReadyResponse = kakaoPayService.kakaoPayReady(kakaoReadyRequest);

        return new ResponseEntity<>(kakaoReadyResponse, HttpStatus.OK);
    }



    /**
     * 결제 성공
     */
    @GetMapping("/DD")
    public ResponseEntity afterPayRequest(@RequestParam("partner_order_id") String partner_order_id,
                                          @RequestParam("partner_user_id") String partner_user_id, @RequestParam("pg_token") String pgToken) {

        KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(partner_order_id,partner_user_id,pgToken);

        return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
    }


    /**
     * 결제 진행 중 취소
     */
    @GetMapping("/cancel")
    public void cancel() {

        throw new CustomException(PAY_CANCEL);
    }

    /**
     * 결제 실패
     */
    @GetMapping("/fail")
    public void fail() {

        throw new CustomException(PAY_FAILED);
    }
    /**
     * 환불
     */
    @PostMapping("/refund")
    public ResponseEntity refund() {

        KakaoCancelResponse kakaoCancelResponse = kakaoPayService.kakaoCancel();

        return new ResponseEntity<>(kakaoCancelResponse, HttpStatus.OK);
    }
}