package com.team1.dojang_crush.domain.place.dto;

import com.team1.dojang_crush.domain.place.domain.Place;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PlaceResponseDto {
    private Long placeId;
    private String theme;
    private String placeName;
    private String address;
    private Long mapId;


    public static PlaceResponseDto from(Place place){
        return new PlaceResponseDto(
                place.getPlaceId(),
                place.getTheme(),
                place.getPlaceName(),
                place.getAddress(),
                place.getMapId()
        );

    }

}
