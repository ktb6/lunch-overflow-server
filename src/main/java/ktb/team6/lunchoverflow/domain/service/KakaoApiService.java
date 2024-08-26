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
//    private static final String BASE_URL = "/v2/local/search/category.json";
    private final WebClient kakaoClient;
    private final RestaurantRepository restaurantRepository;

    @Value("${kakao.base.x}")
    private Double latitude;

    @Value("${kakao.base.y}")
    private Double longitude;

//    @Transactional
//    public void foo(int pageNumber) {
//
//        UriComponentsBuilder uriComponentsBuilder = getDefaultUriBuilder();
//
//        URI uri = uriComponentsBuilder.queryParam("page", pageNumber).build().toUri();
//        log.info("-------- url --------------- {}", uri);
//
//        Mono<KakaoRestaurantResponse> responseMono = kakaoClient.get()
//                .uri(uri)
//                .retrieve()
//                .bodyToMono(KakaoRestaurantResponse.class);
//
//        responseMono.subscribe(response -> {
//            List<KakaoPlace> places = response.getDocuments();
//            restaurantRepository.saveAll(places.stream()
//                    .map(Restaurant::from)
//                    .toList());
//        });
//    }

    public void foo(int pageNumber) {

        UriComponentsBuilder uriComponentsBuilder = getDefaultUriBuilder();

        URI uri = uriComponentsBuilder.queryParam("page", pageNumber).build().toUri();
        log.info("-------- url --------------- {}", uri);

        kakaoClient.get()
                .uri(uri)
                .header("Authorization", "KakaoAK 35e4876bc465c3d872cd66eab74d0a2d")
                .retrieve()
                .bodyToMono(KakaoRestaurantResponse.class)
                .flatMap(response -> {
                    List<KakaoPlace> places = response.getDocuments();
                    return Mono.fromRunnable(() -> restaurantRepository.saveAll(
                            places.stream().map(Restaurant::from).toList()));
                })
                .subscribe();
    }


    private UriComponentsBuilder getDefaultUriBuilder() {
        return UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("category_group_code", "FD6")
                .queryParam("radius", 1000)
                .queryParam("x", latitude)
                .queryParam("y", longitude)
                .queryParam("sort", "distance");
    }


}
