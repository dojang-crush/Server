package com.team1.dojang_crush.domain.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
public class AllPlacesResponseDto {
    private List<PlaceResponseDto> places;
    //private long count;

}