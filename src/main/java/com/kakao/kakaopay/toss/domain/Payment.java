package com.kakao.kakaopay.toss.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "pay_type")
    @Enumerated(EnumType.STRING)
    private PayType payType;
    @Column(nullable = false, name = "pay_amount")
    private Long amount;
    @Column(nullable = false, name = "pay_name")
    private String orderName;
    @Column(nullable = false, name = "order_id")
    private String orderId;

}
