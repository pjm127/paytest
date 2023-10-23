package com.kakao.kakaopay.kakao.service;

import com.kakao.kakaopay.kakao.dto.KakaoApproveResponse;
import com.kakao.kakaopay.kakao.dto.KakaoCancelResponse;
import com.kakao.kakaopay.kakao.dto.KakaoReadyRequest;
import com.kakao.kakaopay.kakao.dto.KakaoReadyResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoPayService {

    static final String cid = "TC0ONETIME"; // 가맹점 테스트 코드
    static final String admin_Key = "805cc6659ad032fc774d8c433559a5cb"; // 공개 조심! 본인 애플리케이션의 어드민 키를 넣어주세요
    private KakaoReadyResponse kakaoReady;
    private KakaoReadyRequest kakaoReadyRequest;
    public KakaoReadyResponse kakaoPayReady(KakaoReadyRequest kakaoReadyRequest){

        double total_amount = kakaoReadyRequest.getTotal_amount();
        double vatRate = 0.1;
        int preTaxAmount =(int) (total_amount / (1 + vatRate)); //세전 금액
        int taxAmount =(int) (total_amount - preTaxAmount); // vat

        // 카카오페이 요청 양식
//        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
//        parameters.add("cid", cid);
//        parameters.add("partner_order_id", "가맹점 주문 번호");
//        parameters.add("partner_user_id", "가맹점 회원 ID");
//        parameters.add("item_name", "상품명");
//        parameters.add("quantity", "2");
//        parameters.add("total_amount", "3");
//        parameters.add("vat_amount", "1");
//        parameters.add("tax_free_amount", "1");
//        parameters.add("approval_url", "http://localhost:8080/payment/success"); // 성공 시 redirect url
//        parameters.add("cancel_url", "http://localhost:8080/payment/cancel"); // 취소 시 redirect url
//        parameters.add("fail_url", "http://localhost:8080/payment/fail"); // 실패 시 redirect url
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", kakaoReadyRequest.getPartner_order_id());
        parameters.add("partner_user_id", kakaoReadyRequest.getPartner_user_id());
        parameters.add("item_name", kakaoReadyRequest.getItem_name());
        parameters.add("quantity",String.valueOf(kakaoReadyRequest.getQuantity()));
        parameters.add("total_amount", String.valueOf(kakaoReadyRequest.getTotal_amount())); // 상품 총액
        parameters.add("vat_amount", String.valueOf(taxAmount)); //상품 부가세 금액
        parameters.add("tax_free_amount", String.valueOf(preTaxAmount)); //상품 비과세 금액
        String encodedPartnerUserId = URLEncoder.encode(kakaoReadyRequest.getPartner_user_id(), StandardCharsets.UTF_8);
        String encodedPartnerOrderId = URLEncoder.encode(kakaoReadyRequest.getPartner_order_id(), StandardCharsets.UTF_8);

        String successUrlWithParameters = kakaoReadyRequest.getApproval_url() +
                "?partner_user_id=" + encodedPartnerUserId +
                "&partner_order_id=" + encodedPartnerOrderId;

        log.info("successUrlWithParameter = {}",successUrlWithParameters);
        parameters.add("approval_url",successUrlWithParameters); // 성공 시 redirect url
        parameters.add("cancel_url", kakaoReadyRequest.getCancel_url()); // 취소 시 redirect url
        parameters.add("fail_url", kakaoReadyRequest.getFail_url()); // 실패 시 redirect url

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://kapi.kakao.com/v1/payment/ready" ;
         /*       + "partner_user_id=" + URLEncoder.encode(kakaoReadyRequest.getPartner_user_id(), StandardCharsets.UTF_8) +
                "&partner_order_id=" + URLEncoder.encode(kakaoReadyRequest.getPartner_order_id(), StandardCharsets.UTF_8);*/

        KakaoReadyResponse kakaoReadyResponse = restTemplate.postForObject(
                apiUrl,
                requestEntity,
                KakaoReadyResponse.class);

        return kakaoReadyResponse;
    }
    /**
     * 결제 완료 승인
     */
    public KakaoApproveResponse approveResponse(String partner_order_id, String partner_user_id, String pgToken) {

        // 카카오 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoReady.getTid());

        parameters.add("partner_order_id", partner_order_id);
        parameters.add("partner_user_id",partner_user_id);
        parameters.add("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://kapi.kakao.com/v1/payment/approve";

        log.info("apiUrl = {}",apiUrl);
        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                apiUrl,
                requestEntity,
                KakaoApproveResponse.class);

        return approveResponse;
    }

    /**
     * 결제 환불
     */
    public KakaoCancelResponse kakaoCancel() {

        // 카카오페이 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", "환불할 결제 고유 번호");
        parameters.add("cancel_amount", "환불 금액");
        parameters.add("cancel_tax_free_amount", "환불 비과세 금액");
        parameters.add("cancel_vat_amount", "환불 부가세");

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoCancelResponse cancelResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/cancel",
                requestEntity,
                KakaoCancelResponse.class);

        return cancelResponse;
    }

    /**
     * 카카오 요구 헤더값
     */
    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + admin_Key;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }
}