package Soodgarak.Soodgarak.domain.oauth.controller;

import Soodgarak.Soodgarak.domain.oauth.controller.model.KakaoTokenResponse;
import Soodgarak.Soodgarak.domain.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;

}
