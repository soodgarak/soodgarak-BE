package Soodgarak.Soodgarak.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 엔드포인트에 대해 CORS 허용
                .allowedOriginPatterns("*","http://localhost:5173","https://soodgarak.shop")  // 모든 오리진 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 허용할 HTTP 메서드 지정
                .allowedHeaders("*")      // 모든 헤더 허용
                .allowCredentials(true)    // JWT 사용을 위한 자격 증명 허용
                .exposedHeaders("Authorization");
    }
}