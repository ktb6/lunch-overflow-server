package ktb.team6.lunchoverflow.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    KOREAN("한식"),
    CHINESE("중식"),
    WESTERN("양식"),
    JAPANESE("일식"),
    SCHOOL_FOOD("분식"),
    CHICKEN("치킨"),
    PIZZA("피자"),
    HAMBURGER("햄버거"),
    ASIAN("아시안"),
    SEAFOOD("해산물 요리"),
    NOODLE("면 요리"),
    SOUP("찌개/국/탕"),
    MEAT("육류"),
    SANDWICH_SALAD("샌드위치/샐러드"),
    STIR_FLY("볶음")
    ;

    private final String description;
}
