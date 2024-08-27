package ktb.team6.lunchoverflow.domain.service;

import java.net.URI;
import java.util.List;
import ktb.team6.lunchoverflow.domain.entity.Restaurant;
import ktb.team6.lunchoverflow.domain.repository.RestaurantRepository;
import ktb.team6.lunchoverflow.domain.service.dto.KakaoPlace;
import ktb.team6.lunchoverflow.domain.service.dto.KakaoRestaurantResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoApiService {

    private static final String BASE_URL = "https://dapi.kakao.com/v2/local/search/category.json";
    private static final String KEYWORD_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
    private final WebClient kakaoClient;
    private final RestaurantRepository restaurantRepository;

    @Value("${kakao.api.key}")
    private String key;

    @Value("${kakao.base.x}")
    private Double latitude;

    @Value("${kakao.base.y}")
    private Double longitude;

    @Transactional
    public void getRestaurantsFromCategory(int pageNumber) {
        UriComponentsBuilder uriComponentsBuilder = getDefaultUriBuilder(BASE_URL);

        URI uri = uriComponentsBuilder.queryParam("page", pageNumber).build().toUri();
        log.info("-------- url --------------- {}", uri);

        kakaoClient.get()
                .uri(uri)
                .header("Authorization", "KakaoAK " + key)
                .retrieve()
                .bodyToMono(KakaoRestaurantResponse.class)
                .flatMap(response -> {
                    List<KakaoPlace> places = response.getDocuments();
                    return Mono.fromRunnable(() -> restaurantRepository.saveAll(
                            places.stream().map(Restaurant::from).toList()));
                })
                .subscribe();
    }

    @Transactional
    public void getRestaurantsFromKeyWord(String keyWord, int pageNumber) {
        UriComponentsBuilder defaultUriBuilder = getDefaultUriBuilder(KEYWORD_URL);
        URI uri = defaultUriBuilder
                .queryParam("query", keyWord)
                .queryParam("page", pageNumber)
                .build().toUri();
        log.info("ggggg");
        kakaoClient.get()
                .uri(uri)
                .header("Authorization", "KakaoAK " + key)
                .retrieve()
                .bodyToMono(KakaoRestaurantResponse.class)
                .flatMap(response -> {
                    List<KakaoPlace> places = response.getDocuments();
                    log.info("places.size(): {}", places.size());
                    return Mono.fromRunnable(() -> restaurantRepository.saveAll(
                            places.stream().map(Restaurant::from).toList()));
                })
                .subscribe();
    }

    private UriComponentsBuilder getDefaultUriBuilder(String url) {
        return UriComponentsBuilder.fromUriString(url)
                .queryParam("category_group_code", "FD6")
                .queryParam("radius", 1000)
                .queryParam("x", latitude)
                .queryParam("y", longitude)
                .queryParam("sort", "distance");
    }
}
