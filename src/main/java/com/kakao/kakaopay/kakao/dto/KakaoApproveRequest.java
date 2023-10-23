package com.kakao.kakaopay.kakao.dto;

import lombok.Data;

@Data
public class KakaoApproveRequest {
    private String partner_order_id;
    private String partner_user_id;
    private String pg_token;

}
