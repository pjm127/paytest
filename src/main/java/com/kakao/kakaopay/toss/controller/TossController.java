package com.kakao.kakaopay.toss.controller;

import com.kakao.kakaopay.toss.dto.PaymentResponse;
import com.kakao.kakaopay.toss.service.TossService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
@Slf4j
public class TossController {

    private final TossService tossService;

    //결제 승인

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 성공", content = @Content(schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "404", description = "결제 실패") })
    @Operation(summary = "결제 승인")
    @GetMapping("/confirm")
    public ResponseEntity tossPaymentSuccess(@Parameter(name = "paymentKey", required = true) @RequestParam String paymentKey,
                                             @Parameter(name = "orderId", required = true)@RequestParam String orderId,
                                             @Parameter(name = "amount", required = true) @RequestParam Long amount ){

        PaymentResponse paymentSuccessDto = tossService.tossPaymentSuccess(paymentKey,orderId,amount);
        return ResponseEntity.ok(paymentSuccessDto);
    }

    //paymentKey로 결제내역 조회
    @GetMapping("/by_payment_key")
    public ResponseEntity getPaymentDetailsByPaymentKey(@RequestParam String paymentKey){

        PaymentResponse paymentSuccessDto = tossService.getPaymentDetailsByPaymentKey(paymentKey);
        return ResponseEntity.ok(paymentSuccessDto);
    }

    //orderId로 결제내역 조회
    @GetMapping("/by_order_id")
    public ResponseEntity getPaymentDetailsByOrderId(@RequestParam String orderId){
        PaymentResponse paymentSuccessDto = tossService.getPaymentDetailsByOrderId(orderId);
        return ResponseEntity.ok(paymentSuccessDto);
    }

    //결제 취소
    @GetMapping("/cancel")
    public ResponseEntity cancelPayment(@RequestParam String paymentKey, @RequestParam String cancelReason,
    @RequestParam String email){
        PaymentResponse paymentResponse = tossService.cancelPayment(paymentKey, cancelReason, email);
        return ResponseEntity.ok(paymentResponse);
    }
}
