package com.kakao.kakaopay.toss.dto;

import lombok.Data;

@Data
public class PayCancelRequest {
    private String paymentKey;

    private String cancelReason;
    private int cancelAmount;

    private String refundReceiveAccount ;

    private int taxFreeAmount;
    private String currency;

}
