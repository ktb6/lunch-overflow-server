package ktb.team6.lunchoverflow.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${kakao.api.key}")
    private String apiKey;

    @Bean
    public WebClient kakaoClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://dapi.kakao.com")
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.add("Authorization", "Kakao AK " + apiKey);
                })
                .build();
    }
}
