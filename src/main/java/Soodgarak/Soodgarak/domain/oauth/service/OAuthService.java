package Soodgarak.Soodgarak.domain.oauth.service;

import Soodgarak.Soodgarak.domain.oauth.controller.model.KakaoTokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Value("${kakao.api-key}")
    private String API_KEY;
    @Value("${kakao.redirect-uri}")
    private String REDIRECT_URI;
    @Value("${kakao.secret-key}")
    private String SECRET_KEY;
    @Value("${kakao.token-uri}")
    private String TOKEN_URI;
    @Value("${kakao.info-uri}")
    private String INFO_URI;

    public KakaoTokenResponse getAccessTokenFromKakao(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", API_KEY);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);
        params.add("client_secret", SECRET_KEY);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
        ResponseEntity<String> token = restTemplate.exchange(
                TOKEN_URI, HttpMethod.POST, kakaoTokenRequest, String.class);

        KakaoTokenResponse tokenResponse = objectMapper.readValue(token.getBody(), KakaoTokenResponse.class);
        log.info("accessToken: " + tokenResponse.getAccessToken());

        return tokenResponse;
    }
}
